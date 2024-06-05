package org.smelovd.mailsender.models.user;

import jakarta.validation.constraints.Email;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public final class UpdateUserDto {

    private final String username;

    @Email(message = "Email Is not email")
    private final String email;
}
