package com.jeulog.config;

import com.jeulog.domain.User;
import com.jeulog.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

@Configuration
@EnableWebSecurity(debug = true) // todo: 운영 환경에서는 사용 금지
public class SecurityConfig {
    @Bean // 함수형 인터페이스
    public WebSecurityCustomizer webSecurityCustomizer(){
        return web -> {
            web.ignoring()
                    .requestMatchers("/favicon.ico")
                    .requestMatchers("/error")
                    .requestMatchers(toH2Console()); // static import 필요
                  //.requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
        };
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .authorizeHttpRequests(
                        authorize -> authorize
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/signup").permitAll()
                        .anyRequest().authenticated())
                .csrf(AbstractHttpConfigurer::disable) // todo: csrf? 공부하기 or (csrf -> csrf.disable())
                .formLogin(login -> login
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/")
                )
                .rememberMe(rm -> rm
                            .rememberMeParameter("remember")
                            .alwaysRemember(false) // default: false 사용자가 체크박스를 활성화하지 않아도 항상 실행
                            .tokenValiditySeconds(2592000))
                .build();
    }
    @Bean
    public UserDetailsService userDetailService(UserRepository userRepository) {
         return username -> {
             User user =  userRepository.findByEmail(username)
                      .orElseThrow(() -> new UsernameNotFoundException(username + "을 찾을 수 없습니다."));
             return new UserPrincipal(user);
         };
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
