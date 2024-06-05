package org.smelovd.mailsender.exceptions;

public final class BadRequestException extends RuntimeException {
    public BadRequestException(String cronJobIsInvalid) {
        super(cronJobIsInvalid);
    }
}
