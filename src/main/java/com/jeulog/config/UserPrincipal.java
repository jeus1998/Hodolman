package com.jeulog.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;

/**
 * role: 역할 -> 관리자, 사용자, 매니저                  ROLE_MANAGER
 * authority: 권한 -> 글쓰기, 글 읽기, 사용자 정지        READ
 */
public class UserPrincipal extends User {
    private final Long userId;
    public UserPrincipal(com.jeulog.domain.User user){
        super(user.getEmail(), user.getPassword(), List.of(
                new SimpleGrantedAuthority("ROLE_USER")
        ));
        userId = user.getId();
    }
    public Long getUserId() {
        return userId;
    }
}
