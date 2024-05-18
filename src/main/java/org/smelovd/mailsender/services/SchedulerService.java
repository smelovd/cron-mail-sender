package org.smelovd.mailsender.services;

import lombok.extern.slf4j.Slf4j;
import org.smelovd.mailsender.entities.CronJob;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
public class SchedulerService {

    private final TaskScheduler scheduler;
    private final MailService mailService;
    private final Map<Integer, ScheduledFuture<?>> scheduledTasks = new HashMap<>();

    public SchedulerService(@Qualifier("threadPoolTaskScheduler") TaskScheduler scheduler, MailService mailService) {
        this.scheduler = scheduler;
        this.mailService = mailService;
    }

    public void scheduleSendingTemplatesToAllUsers(CronJob cronJob) {
        log.info("Scheduling sending to all users with cron job id: " + cronJob.getId());
        scheduledTasks.put(cronJob.getId(),
                scheduler.schedule(mailService::sendTemplateToAllUsers,
                        new CronTrigger(cronJob.getExpression())));
    }

    public void stopScheduledTaskByCronJobId(int id) {
        log.info("Stop scheduled task with id: " + id);
        scheduledTasks.remove(id).cancel(false);
    }
}
