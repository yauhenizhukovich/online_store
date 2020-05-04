package com.gmail.yauhenizhukovich.app.web;

import java.util.TimeZone;
import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class, scanBasePackages = {
        "com.gmail.yauhenizhukovich.app.web",
        "com.gmail.yauhenizhukovich.app.service",
        "com.gmail.yauhenizhukovich.app.repository"
})
public class WebModuleApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebModuleApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

}
