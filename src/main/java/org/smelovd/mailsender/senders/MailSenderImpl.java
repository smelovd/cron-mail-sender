package org.smelovd.mailsender.senders;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MailSenderImpl {

    private final JavaMailSender mailSender;

    @Retryable(backoff = @Backoff(delay = 1000))
    public void send(SimpleMailMessage... message) {
        mailSender.send(message);
    }
    //TODO specific mail exception handler
}
