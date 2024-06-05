package org.smelovd.mailsender.repositories;

import org.smelovd.mailsender.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    boolean existsByUsername(final String username);

    boolean existsByEmail(final String email);

    Page<User> findAllByEmailStartingWithOrUsernameStartingWith(final Pageable pageable, @Param("email") final String email, @Param("username") final String username);
}
