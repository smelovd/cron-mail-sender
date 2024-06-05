package org.smelovd.mailsender.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.smelovd.mailsender.entities.User;
import org.smelovd.mailsender.exceptions.BadRequestException;
import org.smelovd.mailsender.exceptions.NotFoundException;
import org.smelovd.mailsender.models.PaginationResponse;
import org.smelovd.mailsender.models.user.CreateUserDto;
import org.smelovd.mailsender.models.user.UpdateUserDto;
import org.smelovd.mailsender.repositories.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public PaginationResponse<User> findAllPaginate(final int page, final int count, final String email, final String username) {
        log.info("Finding users page: {}, with count: {}", page, count);
        final Pageable pageable = PageRequest.of(page - 1, count, Sort.by("createdOn").descending());
        final Page<User> paginatedResponse = findAllOrFilteredIfEmailOrUsernameExist(pageable, email, username);

        return PaginationResponse.<User>builder()
                .content(paginatedResponse.getContent())
                .totalPages(paginatedResponse.getTotalPages())
                .currentPage(page)
                .totalElements(paginatedResponse.getTotalElements())
                .elementsPerPage(count)
                .build();
    }

    private Page<User> findAllOrFilteredIfEmailOrUsernameExist(final Pageable pageable, final String email, final String username) {
        if (email != null || username != null) {
            return userRepository.findAllByEmailStartingWithOrUsernameStartingWith(pageable, email, username);
        }

        return userRepository.findAll(pageable);
    }

    @Transactional
    public User createUser(final CreateUserDto createUserDto) {
        log.info("try to create new user: {}, {}", createUserDto.email(), createUserDto.username());
        if (userRepository.existsByEmail(createUserDto.email())) {
            log.warn("Email is invalid: {}", createUserDto.email());
            throw new BadRequestException("Email already in use: " + createUserDto.email());
        }

        if (userRepository.existsByUsername(createUserDto.username())) {
            log.warn("Username is invalid: {}", createUserDto.username());
            throw new BadRequestException("Username already in use: " + createUserDto.username());
        }

        final User newUser = userRepository.save(User.builder()
                .username(createUserDto.username())
                .email(createUserDto.email())
                .build());
        log.info("User created with id: {}", newUser.getId());
        return newUser;
    }

    public User findById(final int id) {
        log.info("Finding user by id: {}", id);
        return userRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    public void deleteById(final int id) {
        log.info("Deleting user by id (if exist): {}", id);
        userRepository.deleteById(id);
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public User updateUserById(final int id, final UpdateUserDto updateUserDto) {
        log.info("try to update email/username user with id: {}", id);
        final User user = userRepository.findById(id).orElseThrow(NotFoundException::new);

        if (!isShouldUpdate(updateUserDto)) {
            log.warn("Nothing to update user with id: {}", id);
            return user;
        }

        user.update(updateUserDto);
        userRepository.save(user);
        log.info("Updated user with id: {}", id);

        return user;
    }

    private boolean isShouldUpdate(final UpdateUserDto updateUserDto) {
        if (updateUserDto.getEmail() != null) {
            if (!updateUserDto.getEmail().isBlank()) {
                return true;
            }
        }

        if (updateUserDto.getUsername() != null) {
            if (!updateUserDto.getUsername().isBlank()) {
                return true;
            }
        }

        return false;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
