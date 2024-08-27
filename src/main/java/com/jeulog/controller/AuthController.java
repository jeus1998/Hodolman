package com.jeulog.controller;

import com.jeulog.request.Login;
import com.jeulog.response.SessionResponse;
import com.jeulog.service.AuthService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.SecretKey;
import java.util.Base64;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    public static final String KEY = "kUJBh4N8N7jI7WnaA5YsI0qbIx/EgNNvlmpjgorIWPQ=";
    @PostMapping("/auth/login")
    public SessionResponse login(@RequestBody Login login){

        Long userId = authService.login(login);

        // SecretKey 발급 1회성
        // SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // byte[] encodedKey = secretKey.getEncoded();
        // String secretStrKey = Base64.getEncoder().encodeToString(encodedKey);

        byte[] decodeKey = Base64.getDecoder().decode(KEY);

        String jws = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .signWith(SignatureAlgorithm.HS256, decodeKey)
                .compact();

        return new SessionResponse(jws);
    }
}
