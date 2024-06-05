package org.smelovd.mailsender.models.cron;

import jakarta.validation.constraints.NotBlank;

public record UpdateCronJobDto(@NotBlank(message = "Expression should be not empty") String expression) {
}
