package org.smelovd.mailsender.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smelovd.mailsender.entities.CronJob;
import org.smelovd.mailsender.entities.enums.SEND_TYPE;
import org.smelovd.mailsender.senders.MailService;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final TaskScheduler scheduler;
    private final MailService mailService;
    private final Map<Integer, ScheduledFuture<?>> tasks = new HashMap<>();

    public void scheduleSendingToAllUsers(CronJob cronJob) {
        log.info("Scheduling sending to all users with cron job id: " + cronJob.getId());
        tasks.put(cronJob.getId(),
                scheduler.schedule(() -> mailService.sendTemplateToAllUsers(SEND_TYPE.CRON),
                        new CronTrigger(cronJob.getExpression())));
    }

    public void stopTaskByCronJobId(int id) {
        log.info("Stop scheduled task with id: " + id);
        tasks.remove(id).cancel(false);
    }
}
