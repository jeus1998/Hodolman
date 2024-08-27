package com.jeulog.service;

import com.jeulog.domain.Session;
import com.jeulog.domain.User;
import com.jeulog.exception.InvalidSignInformation;
import com.jeulog.repository.UserRepository;
import com.jeulog.request.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    @Transactional
    public Long login(Login login){
        log.info("AuthService");
        User user = userRepository.findByEmailAndPassword(login.getEmail(), login.getPassword())
                       .orElseThrow(InvalidSignInformation::new);

        return user.getId();
    }
}
