package org.smelovd.mailsender.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.smelovd.mailsender.entities.enums.SEND_TYPE;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "logs")
public class Log {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private User user;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SEND_TYPE type;

    @CreationTimestamp
    private Date createdOn;
}
