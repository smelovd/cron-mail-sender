package org.smelovd.mailsender.senders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smelovd.mailsender.entities.User;
import org.smelovd.mailsender.entities.enums.SEND_TYPE;
import org.smelovd.mailsender.services.LogService;
import org.smelovd.mailsender.services.UserService;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final UserService userService;
    private final LogService logService;

    private void send(User to, String subject, String text) { //TODO async?
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to.getEmail());
            message.setSubject(subject);
            message.setText(text);

            mailSender.send(message);
            log.info("Mail sent to: " + to.getId());
        } catch (MailException e) {
            log.error("Mail sending failed: " + e.getMessage());
            throw new RuntimeException("Mail sending failed: " + e.getMessage());
        }
    }

    private void sendTemplate(User user) {
        String text =
                "Username: " + user.getUsername() +
                "\nCreation date: " + user.getCreatedOn();
        send(user, "Hello!", text);
    }

    public void sendTemplate(User user, SEND_TYPE type) {
        log.info("Sending template mail to: " + user.getId());
        sendTemplate(user);
        logService.save(user, type);
    }

    public void sendTemplateToAllUsers(SEND_TYPE type) {
        log.info("Sending template mail to all users");
        List<User> users = userService.findAll();
        Date createdOn = new Date();

        List<User> successSentUsers = users.stream()
                .filter(user -> {
                    try {
                        sendTemplate(user);
                        return true;
                    } catch (RuntimeException e) {
                        return false;
                    }
                }).toList();

        logService.saveAll(successSentUsers, type, createdOn);
    }
}
