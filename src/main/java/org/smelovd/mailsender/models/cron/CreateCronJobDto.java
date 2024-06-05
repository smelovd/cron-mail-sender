package org.smelovd.mailsender.models.cron;

import jakarta.validation.constraints.NotBlank;

public record CreateCronJobDto(@NotBlank(message = "Expression should be not empty") String expression) {
}
