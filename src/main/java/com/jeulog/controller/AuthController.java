package com.jeulog.controller;

import com.jeulog.config.AppConfig;
import com.jeulog.request.Login;
import com.jeulog.response.SessionResponse;
import com.jeulog.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final AppConfig appConfig;
    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){

        Long userId = authService.login(login);
        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(SignatureAlgorithm.HS256, appConfig.getJwtKey())
                .issuedAt(new Date()) // 발급 날짜
                // TODO + 만료 날짜 추가 / AuthResolver 검증 추가
                .compact();

        return new SessionResponse(jws);
    }
}
/*
// SecretKey 발급 1회성
// SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
// byte[] encodedKey = secretKey.getEncoded();
// String secretStrKey = Base64.getEncoder().encodeToString(encodedKey);
 */