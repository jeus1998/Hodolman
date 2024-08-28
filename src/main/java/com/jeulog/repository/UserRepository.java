package com.jeulog.repository;

import com.jeulog.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(@Param("Email") String Email);
    Optional<User> findByEmailAndPassword(@Param("Email") String Email, @Param("Password") String Password);
}
