package com.jeulog.config;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import java.util.List;

public class UserPrincipal extends User {
    private final Long userId;
    public UserPrincipal(com.jeulog.domain.User user){
        super(user.getEmail(), user.getPassword(), List.of(new SimpleGrantedAuthority("ADMIN")));
        userId = user.getId();
    }
    public Long getUserId() {
        return userId;
    }
}
