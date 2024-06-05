package org.smelovd.mailsender.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.smelovd.mailsender.models.cron.UpdateCronJobDto;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cron_jobs")
public class CronJob {

    @Id
    @GeneratedValue
    private int id;

    @NotNull
    @NotEmpty
    @NotBlank(message = "Invalid expression")
    private String expression;

    @CreationTimestamp
    private Date createdOn;

    public void update(UpdateCronJobDto updateCronJobDto) {
        setExpression(updateCronJobDto.expression());
    }
}
