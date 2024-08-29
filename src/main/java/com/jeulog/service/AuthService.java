package com.jeulog.service;

import com.jeulog.domain.Session;
import com.jeulog.domain.User;
import com.jeulog.exception.AlreadyExistsEmailException;
import com.jeulog.exception.InvalidRequest;
import com.jeulog.exception.InvalidSignInformation;
import com.jeulog.exception.Unauthorized;
import com.jeulog.repository.UserRepository;
import com.jeulog.request.Login;
import com.jeulog.request.SignUp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.rmi.AlreadyBoundException;
import java.util.Optional;

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
    public void signUp(SignUp signup) {

        Optional<User> findDuplicateUser = userRepository.findByEmail(signup.getEmail());
        if(findDuplicateUser.isPresent()){
            throw new AlreadyExistsEmailException();
        }

        SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
                16,
                8,
                1,
                32,
                64);

        String encryptedPassword = encoder.encode(signup.getPassword());
        // log.info("encryptedPassword:{}", encryptedPassword);

        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
