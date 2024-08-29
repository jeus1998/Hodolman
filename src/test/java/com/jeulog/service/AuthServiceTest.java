package com.jeulog.service;

import com.jeulog.crypto.PasswordEncoder;
import com.jeulog.domain.User;
import com.jeulog.exception.AlreadyExistsEmailException;
import com.jeulog.exception.InvalidSignInformation;
import com.jeulog.repository.UserRepository;
import com.jeulog.request.Login;
import com.jeulog.request.SignUp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthService authService;
    @Test
    @DisplayName("회원가입 성공")
    void test1(){
        // given
        SignUp signUp = SignUp.builder()
                .name("jeu")
                .email("baejeu@naver.com")
                .password("1234")
                .build();
        // when
        authService.signUp(signUp);

        // then
        assertThat(userRepository.count()).isEqualTo(1);
        assertThat(userRepository.findAll())
                .extracting(User::getId, User::getEmail, User::getName)
                .containsExactly(tuple(1L, "baejeu@naver.com", "jeu"));
        // 암호화 체크
        assertThat(userRepository.findAll().get(0).getPassword()).isNotEqualTo("1234");
    }
    @Test
    @DisplayName("회원가입시 중복된 이메일은 실패")
    void test2(){
        // given
        User user = User.builder()
                .name("jeu12")
                .email("baejeu@naver.com")
                .password("1234")
                .build();

        userRepository.save(user);

        SignUp signUp = SignUp.builder()
                .name("jeu")
                .email("baejeu@naver.com")
                .password("1234")
                .build();

        // expected
        assertThatThrownBy(() -> authService.signUp(signUp))
                .isInstanceOf(AlreadyExistsEmailException.class);
    }
    @Test
    @DisplayName("로그인 성공")
    void test3(){
        // given
        String encodedPassword = PasswordEncoder.encode("1234");
        User user = User.builder()
                .name("jeu")
                .email("baejeu@naver.com")
                .password(encodedPassword)
                .build();

        userRepository.save(user);

        Login login = Login.builder()
                .email("baejeu@naver.com")
                .password("1234")
                .build();

        // when
        Long userId = authService.login(login);

        // then
        assertThat(userId).isNotNull();
    }
    @Test
    @DisplayName("틀린 비밀번호 입력")
    void test4(){
        // given
        SignUp signUp = SignUp.builder()
                        .name("jeu")
                        .email("baejeu@naver.com")
                        .password("1234")
                        .build();
        authService.signUp(signUp);

        Login login = Login.builder()
                .email("baejeu@naver.com")
                .password("123456")
                .build();

        // expected
        assertThatThrownBy(()-> authService.login(login))
                .isInstanceOf(InvalidSignInformation.class);
    }
}