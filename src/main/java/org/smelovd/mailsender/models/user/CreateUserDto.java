package org.smelovd.mailsender.models.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CreateUserDto {

    @NotBlank(message = "Username should be not empty")
    private String username;

    @Email(message = "Email Is not email")
    @NotBlank(message = "Email should be not empty")
    private String email;
}
