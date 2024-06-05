package org.smelovd.mailsender.models.log;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
public final class LogResponse {

    private final String username;
    private final String email;
    private final Count count;
    private final Date first;
    private final Date last;

    public LogResponse(String username, String email, Long rest, Long cron, Date first, Date last) {
        this.username = username;
        this.email = email;
        this.count = Count.builder().rest(rest).cron(cron).build();
        this.first = first;
        this.last = last;
    }

    @Builder
    public record Count(long rest, long cron) {
    }
}
