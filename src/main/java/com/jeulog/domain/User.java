package com.jeulog.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "users")
@ToString(of ={"id", "name", "email", "password", "createdAt"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;
    private String name;
    private String email;
    private String password;
    private LocalDateTime createdAt;
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Session> sessions = new ArrayList<>();

    @Builder
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.createdAt = LocalDateTime.now();
    }
    public Session addSession(){
        Session session = Session.builder()
                .user(this)
                .accessToken(UUID.randomUUID().toString())
                .build();
        sessions.add(session);
        return session;
    }
}
