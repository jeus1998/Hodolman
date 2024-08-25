package com.jeulog.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "accessToken"})
public class Session {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "session_id")
    private Long id;
    private String accessToken;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Builder
    public Session(String accessToken, User user) {
        this.accessToken = accessToken;
        this.user = user;
    }
}
