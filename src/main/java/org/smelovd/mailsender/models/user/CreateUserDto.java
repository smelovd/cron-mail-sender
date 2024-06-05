package org.smelovd.mailsender.models.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateUserDto(@NotBlank(message = "Username should be not empty") String username,
                            @Email(message = "Email Is not email") @NotBlank(message = "Email should be not empty") String email) {

}
