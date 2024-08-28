package com.jeulog;

import com.jeulog.config.AppConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties(AppConfig.class)
@SpringBootApplication
public class JeulogApplication {
    public static void main(String[] args) {
        SpringApplication.run(JeulogApplication.class, args);
    }
}
