package org.smelovd.mailsender;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MailSenderApplication {

    public static void main(String[] args) {
        SpringApplication.run(MailSenderApplication.class, args);
    }

}
