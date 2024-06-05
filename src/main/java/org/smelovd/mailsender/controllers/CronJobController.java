package org.smelovd.mailsender.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smelovd.mailsender.entities.CronJob;
import org.smelovd.mailsender.models.PaginationResponse;
import org.smelovd.mailsender.models.cron.CreateCronJobDto;
import org.smelovd.mailsender.models.cron.UpdateCronJobDto;
import org.smelovd.mailsender.services.CronJobService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("cron-jobs")
public class CronJobController {

    private final CronJobService cronJobService;

    @GetMapping
    public PaginationResponse<CronJob> getAllCronJobPaginate(
            @RequestParam @Min(1) final int page,
            @RequestParam @Min(1) final int count
    ) {
        return cronJobService.findAllPaginate(page, count);
    }

    @GetMapping("{id}")
    public CronJob getCronJobById(@PathVariable("id") final int id) {
        return cronJobService.findById(id);
    }

    @DeleteMapping("{id}")
    public void deleteCronJobById(@PathVariable("id") final int id) {
        cronJobService.deleteById(id);
    }

    @PutMapping("{id}")
    public CronJob updateCronJobById(@PathVariable("id") final int id, @Valid @RequestBody final UpdateCronJobDto updateCronJobDto) {
        return cronJobService.updateById(id, updateCronJobDto);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CronJob createCronJob(@Valid @RequestBody final CreateCronJobDto createCronJobDto) {
        return cronJobService.createCronJob(createCronJobDto);
    }
}
