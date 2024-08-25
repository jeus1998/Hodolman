package com.jeulog.repository;

import com.jeulog.domain.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Long> {
    Optional<Session> findByAccessToken(@Param("accessToken") String accessToken);
}
