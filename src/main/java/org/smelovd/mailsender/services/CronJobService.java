package org.smelovd.mailsender.services;

import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.smelovd.mailsender.entities.CronJob;
import org.smelovd.mailsender.models.PaginationResponse;
import org.smelovd.mailsender.models.cron.CreateCronJobDto;
import org.smelovd.mailsender.models.cron.UpdateCronJobDto;
import org.smelovd.mailsender.repositories.CronJobRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CronJobService {

    private final CronJobRepository cronJobRepository;
    private final SchedulerService schedulerService;

    public PaginationResponse<CronJob> findAllPaginate(int page, int count) {
        Pageable pageable = PageRequest.of(page - 1, count, Sort.by("createdOn").descending()); //TODO maybe created_on
        Page<CronJob> paginatedResponse = cronJobRepository.findAll(pageable);

        return PaginationResponse.<CronJob>builder()
                .content(paginatedResponse.getContent())
                .totalPages(paginatedResponse.getTotalPages())
                .currentPage(page)
                .totalElements(paginatedResponse.getTotalElements())
                .elementsPerPage(count)
                .build();
    }

    @SneakyThrows
    @Transactional
    public CronJob createCronJob(@Valid CreateCronJobDto createCronJobDto) {
        log.info("try to create new cron job: " + createCronJobDto.getExpression());
        if (!CronExpression.isValidExpression(createCronJobDto.getExpression())) {
            log.error("Cron Job is invalid: " + createCronJobDto.getExpression());
            throw new BadRequestException("Cron Job is invalid");
        }
        CronJob newCronJob = cronJobRepository.save(CronJob.builder().expression(createCronJobDto.getExpression()).build());
        log.info("New cron job created with id: " + newCronJob.getId());

        schedulerService.scheduleSendingToAllUsers(newCronJob);
        return newCronJob;
    }

    @SneakyThrows
    public CronJob findById(int id) {
        return cronJobRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public void deleteById(int id) {
        log.info("Deleting user by id (if exist): " + id);
        cronJobRepository.deleteById(id);
        schedulerService.stopTaskByCronJobId(id);
    }

    @SneakyThrows
    @Transactional
    public CronJob updateById(int id, @Valid UpdateCronJobDto updateCronJobDto) {
        log.info("try to update cron job with id: " + id);
        CronJob cronJob = cronJobRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

        if (!isShouldUpdate(cronJob, updateCronJobDto)) {
            log.warn("Nothing to update with id: " + id);
            return cronJob;
        }

        cronJob.update(updateCronJobDto);
        cronJobRepository.save(cronJob);
        log.info("Updated cron: " + cronJob);

        schedulerService.stopTaskByCronJobId(id);
        schedulerService.scheduleSendingToAllUsers(cronJob);

        return cronJob;
    }

    private boolean isShouldUpdate(CronJob cronJob, @Valid UpdateCronJobDto updateUserDto) {
        return !Objects.equals(cronJob.getExpression(), updateUserDto.getExpression()) && CronExpression.isValidExpression(updateUserDto.getExpression());
    }

    public List<CronJob> findAll() {
        return cronJobRepository.findAll();
    }

    @PostConstruct
    public void startUpScheduling() {
        log.info("Scheduling cron jobs");
        List<CronJob> cronJobs = findAll();

        cronJobs.forEach(schedulerService::scheduleSendingToAllUsers);
    }
}