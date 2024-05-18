package org.smelovd.mailsender.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smelovd.mailsender.entities.User;
import org.smelovd.mailsender.entities.enums.SEND_TYPE;
import org.smelovd.mailsender.senders.MailSenderImpl;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final MailSenderImpl mailSender;
    private final UserService userService;
    private final LogService logService;

    public void send(User user) {
        log.info("Sending mail template to: " + user.getId());
        mailSender.send(getSimpleMailMessage(user));
        log.info("Mail sent to user with id: " + user.getId());

        logService.save(user, SEND_TYPE.REST);
    }

    @Async("threadPoolTaskExecutorScheduler")
    public void sendTemplateToAllUsers() {
        log.info("Sending mail templates to all users");
        Date createdOn = new Date();
        List<User> users = userService.findAll();

        mailSender.send(getSimpleMailMessages(users));
        log.info("Mail sent to all users");

        logService.saveAll(users, SEND_TYPE.CRON, createdOn);
    }

    private SimpleMailMessage[] getSimpleMailMessages(List<User> users) {
        return users.stream()
                .map(this::getSimpleMailMessage)
                .toArray(SimpleMailMessage[]::new);
    }

    private SimpleMailMessage getSimpleMailMessage(User user) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Hello!");
        message.setText(getTemplate(user));

        return message;
    }

    private String getTemplate(User user) {
        return "Username: " + user.getUsername() +
                "\nCreation date: " + user.getCreatedOn();
    }
}
