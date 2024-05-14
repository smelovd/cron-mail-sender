package org.smelovd.mailsender.repositories;

import org.smelovd.mailsender.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);

    Page<User> findAllByEmailStartingWithOrUsernameStartingWith(Pageable pageable, @Param("email") String email, @Param("username") String username);
}
