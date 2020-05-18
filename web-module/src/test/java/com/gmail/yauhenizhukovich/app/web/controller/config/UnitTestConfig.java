package com.gmail.yauhenizhukovich.app.web.controller.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.mockito.Mockito.mock;

@Configuration
public class UnitTestConfig {

    @Bean
    public UserDetailsService userDetailsService() {return mock(UserDetailsService.class);}

}
