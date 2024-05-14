package org.smelovd.mailsender.models.user;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UpdateUserDto {

    private String username;

    @Email(message = "Email Is not email")
    private String email;
}
