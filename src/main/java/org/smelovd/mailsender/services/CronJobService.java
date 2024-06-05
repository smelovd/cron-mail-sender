package org.smelovd.mailsender.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smelovd.mailsender.entities.CronJob;
import org.smelovd.mailsender.exceptions.BadRequestException;
import org.smelovd.mailsender.exceptions.NotFoundException;
import org.smelovd.mailsender.models.PaginationResponse;
import org.smelovd.mailsender.models.cron.CreateCronJobDto;
import org.smelovd.mailsender.models.cron.UpdateCronJobDto;
import org.smelovd.mailsender.repositories.CronJobRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CronJobService {

    private final CronJobRepository cronJobRepository;
    private final SchedulerService schedulerService;

    public PaginationResponse<CronJob> findAllPaginate(final int page, final int count) {
        log.info("Finding cron jobs page: {}, with count: {}", page, count);
        final Pageable pageable = PageRequest.of(page - 1, count, Sort.by("createdOn").descending()); //TODO maybe created_on
        final Page<CronJob> paginatedResponse = cronJobRepository.findAll(pageable);

        return PaginationResponse.<CronJob>builder()
                .content(paginatedResponse.getContent())
                .totalPages(paginatedResponse.getTotalPages())
                .currentPage(page)
                .totalElements(paginatedResponse.getTotalElements())
                .elementsPerPage(count)
                .build();
    }

    @Transactional
    public CronJob createCronJob(final CreateCronJobDto createCronJobDto) {
        log.info("try to create new cron job: {}", createCronJobDto.expression());
        if (!CronExpression.isValidExpression(createCronJobDto.expression())) {
            log.error("Cron Job is invalid: {}", createCronJobDto.expression());
            throw new BadRequestException("Cron Job is invalid");
        }
        final CronJob newCronJob = cronJobRepository.save(CronJob.builder().expression(createCronJobDto.expression()).build());
        log.info("New cron job created with id: {}", newCronJob.getId());

        schedulerService.scheduleSendingTemplatesToAllUsers(newCronJob);
        return newCronJob;
    }

    public CronJob findById(final int id) {
        log.info("Finding cron job by id: {}", id);
        return cronJobRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteById(final int id) {
        log.info("Deleting cron job by id (if exist): {}", id);
        cronJobRepository.deleteById(id);
        schedulerService.stopScheduledTaskByCronJobId(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public CronJob updateById(final int id, final UpdateCronJobDto updateCronJobDto) {
        log.info("try to update cron job with id: {}", id);
        final CronJob cronJob = cronJobRepository.findById(id).orElseThrow(NotFoundException::new);

        if (!isShouldUpdate(cronJob, updateCronJobDto)) {
            log.warn("Nothing to update with id: {}", id);
            return cronJob;
        }

        cronJob.update(updateCronJobDto);
        cronJobRepository.save(cronJob);
        log.info("Updated cron: {}", cronJob);

        schedulerService.stopScheduledTaskByCronJobId(id);
        schedulerService.scheduleSendingTemplatesToAllUsers(cronJob);

        return cronJob;
    }

    private boolean isShouldUpdate(final CronJob cronJob, final UpdateCronJobDto updateUserDto) {
        return !Objects.equals(cronJob.getExpression(), updateUserDto.expression()) && CronExpression.isValidExpression(updateUserDto.expression());
    }

    public List<CronJob> findAll() {
        return cronJobRepository.findAll();
    }

    @PostConstruct
    public void startUpScheduling() {
        log.info("Scheduling cron jobs");
        final List<CronJob> cronJobs = findAll();

        cronJobs.forEach(schedulerService::scheduleSendingTemplatesToAllUsers);
    }
}
