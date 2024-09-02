package com.jeulog.service;

import com.jeulog.domain.User;
import com.jeulog.exception.AlreadyExistsEmailException;
import com.jeulog.repository.UserRepository;
import com.jeulog.request.SignUp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    public void signUp(SignUp signup) {

        Optional<User> findDuplicateUser = userRepository.findByEmail(signup.getEmail());
        if(findDuplicateUser.isPresent()){
            throw new AlreadyExistsEmailException();
        }
        String encryptedPassword = passwordEncoder.encode(signup.getPassword());

        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
