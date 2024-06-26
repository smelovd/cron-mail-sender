package org.smelovd.mailsender.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smelovd.mailsender.entities.User;
import org.smelovd.mailsender.models.PaginationResponse;
import org.smelovd.mailsender.models.user.CreateUserDto;
import org.smelovd.mailsender.models.user.UpdateUserDto;
import org.smelovd.mailsender.services.MailService;
import org.smelovd.mailsender.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final MailService mailService;

    @GetMapping
    public PaginationResponse<User> getAllUsersPaginate(
            @RequestParam @Min(1) final int page,
            @RequestParam @Min(1) final int count,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String username
    ) {
        return userService.findAllPaginate(page, count, email, username);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User createUser(@Valid @RequestBody final CreateUserDto createUserDto) {
        return userService.createUser(createUserDto);
    }

    @GetMapping("{id}")
    public User getUserById(@PathVariable("id") final int id) {
        return userService.findById(id);
    }

    @DeleteMapping("{id}")
    public void deleteUserById(@PathVariable("id") final int id) {
        userService.deleteById(id);
    }

    @PutMapping("{id}")
    public User updateUserById(@PathVariable("id") final int id, @Valid @RequestBody final UpdateUserDto updateUserDto) {
        return userService.updateUserById(id, updateUserDto);
    }

    @PostMapping("{id}/send")
    public void sendMail(@PathVariable("id") final int id) {
        mailService.send(userService.findById(id));
    }
}
