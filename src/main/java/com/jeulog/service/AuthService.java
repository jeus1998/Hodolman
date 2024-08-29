package com.jeulog.service;

import com.jeulog.domain.User;
import com.jeulog.exception.AlreadyExistsEmailException;
import com.jeulog.exception.InvalidSignInformation;
import com.jeulog.repository.UserRepository;
import com.jeulog.request.Login;
import com.jeulog.request.SignUp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import com.jeulog.crypto.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    @Transactional
    public Long login(Login login){
        User user = userRepository.findByEmail(login.getEmail())
                       .orElseThrow(InvalidSignInformation::new);


        boolean matches = PasswordEncoder.matches(login.getPassword(), user.getPassword());
        if(!matches) throw new InvalidSignInformation(); // TODO 이때 '비밀번호가 틀렸습니다'라고 클라이언트에게 알려야할까?

        return user.getId();
    }
    public void signUp(SignUp signup) {

        Optional<User> findDuplicateUser = userRepository.findByEmail(signup.getEmail());
        if(findDuplicateUser.isPresent()){
            throw new AlreadyExistsEmailException();
        }

        String encryptedPassword = PasswordEncoder.encode(signup.getPassword());

        User user = User.builder()
                .name(signup.getName())
                .password(encryptedPassword)
                .email(signup.getEmail())
                .build();
        userRepository.save(user);
    }
}
