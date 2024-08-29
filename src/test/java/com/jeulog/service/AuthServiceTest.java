package com.jeulog.service;

import com.jeulog.domain.User;
import com.jeulog.exception.AlreadyExistsEmailException;
import com.jeulog.repository.UserRepository;
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

}