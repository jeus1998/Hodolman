package com.jeulog.crypto;

import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

public final class PasswordEncoder {
    private static final SCryptPasswordEncoder encoder = new SCryptPasswordEncoder(
                           16,
                           8,
                           1,
                           32,
                           64);
    public static String encode(String rawPassword){
        return encoder.encode(rawPassword);
    }
    public static boolean matches(String rawPassword, String encodedPassword){
        return encoder.matches(rawPassword, encodedPassword);
    }
}
