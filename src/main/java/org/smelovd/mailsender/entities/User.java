package org.smelovd.mailsender.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.smelovd.mailsender.models.user.UpdateUserDto;

import java.util.Date;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue
    private int id;

    @NotBlank(message = "Username should be not empty")
    @Column(nullable = false, unique = true)
    private String username;

    @Email(message = "Email Is not email")
    @NotBlank(message = "Email should be not empty")
    @Column(nullable = false, unique = true)
    private String email;

    @CreationTimestamp
    private Date createdOn;

    public void update(UpdateUserDto updateUserDto) {
        if (updateUserDto.getEmail() != null) {
            if (!updateUserDto.getEmail().isBlank()) {
                setEmail(updateUserDto.getEmail());
            }
        }

        if (updateUserDto.getUsername() != null) {
            if (!updateUserDto.getUsername().isBlank()) {
                setUsername(updateUserDto.getUsername());
            }
        }
    }
}
