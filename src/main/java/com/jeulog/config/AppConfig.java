package com.jeulog.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Base64;

@ConfigurationProperties(prefix = "jeujeu")
public class AppConfig {
    private byte[] jwtKey;
    public AppConfig(String jwtKey) {
        this.jwtKey = Base64.getDecoder().decode(jwtKey);
    }
    public byte[] getJwtKey() {
        return jwtKey;
    }
}
