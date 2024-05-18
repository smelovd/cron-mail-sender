package org.smelovd.mailsender.services;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.smelovd.mailsender.entities.User;
import org.smelovd.mailsender.models.PaginationResponse;
import org.smelovd.mailsender.models.user.CreateUserDto;
import org.smelovd.mailsender.models.user.UpdateUserDto;
import org.smelovd.mailsender.repositories.UserRepository;
import org.springframework.data.crossstore.ChangeSetPersister;
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

    public PaginationResponse<User> findAllPaginate(int page, int count, String email, String username) {
        log.info("Finding users page: " + page + ", with count: " + count);
        Pageable pageable = PageRequest.of(page - 1, count, Sort.by("createdOn").descending());
        Page<User> paginatedResponse = findAllOrFilteredIfEmailOrUsernameExist(pageable, email, username);

        return PaginationResponse.<User>builder()
                .content(paginatedResponse.getContent())
                .totalPages(paginatedResponse.getTotalPages())
                .currentPage(page)
                .totalElements(paginatedResponse.getTotalElements())
                .elementsPerPage(count)
                .build();
    }

    private Page<User> findAllOrFilteredIfEmailOrUsernameExist(Pageable pageable, String email, String username) {
        if (email != null || username != null) {
            return userRepository.findAllByEmailStartingWithOrUsernameStartingWith(pageable, email, username);
        }

        return userRepository.findAll(pageable);
    }

    @SneakyThrows
    @Transactional
    public User createUser(CreateUserDto createUserDto) {
        log.info("try to create new user: " + createUserDto.getEmail() + ", " + createUserDto.getUsername());
        if (userRepository.existsByEmail(createUserDto.getEmail())) {
            log.warn("Email is invalid: " + createUserDto.getEmail());
            throw new BadRequestException("Email already in use: " + createUserDto.getEmail());
        }

        if (userRepository.existsByUsername(createUserDto.getUsername())) {
            log.warn("Username is invalid: " + createUserDto.getUsername());
            throw new BadRequestException("Username already in use: " + createUserDto.getUsername());
        }

        User newUser = userRepository.save(User.builder()
                .username(createUserDto.getUsername())
                .email(createUserDto.getEmail())
                .build());
        log.info("User created with id: " + newUser.getId());
        return newUser;
    }

    @SneakyThrows
    public User findById(int id) {
        log.info("Finding user by id: " + id);
        return userRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);
    }

    public void deleteById(int id) {
        log.info("Deleting user by id (if exist): " + id);
        userRepository.deleteById(id);
    }

    @SneakyThrows
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public User updateUserById(int id, UpdateUserDto updateUserDto) {
        log.info("try to update email/username user with id: " + id);
        User user = userRepository.findById(id).orElseThrow(ChangeSetPersister.NotFoundException::new);

        if (!isShouldUpdate(updateUserDto)) {
            log.warn("Nothing to update user with id: " + id);
            return user;
        }

        user.update(updateUserDto);
        userRepository.save(user);
        log.info("Updated user with id: " + id);

        return user;
    }

    private boolean isShouldUpdate(UpdateUserDto updateUserDto) {
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
