package org.smelovd.mailsender.models.cron;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateCronJobDto {

    @NotBlank(message = "Expression should be not empty")
    private String expression;
}
