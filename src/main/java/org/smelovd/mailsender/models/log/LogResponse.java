package org.smelovd.mailsender.models.log;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class LogResponse {

    private String username;
    private String email;
    private Count count;
    private Date first;
    private Date last;

    public LogResponse(String username, String email, Long rest, Long cron, Date first, Date last) {
        this.username = username;
        this.email = email;
        this.count = Count.builder().rest(rest).cron(cron).build();
        this.first = first;
        this.last = last;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Count {
        private long rest;
        private long cron;
    }
}
