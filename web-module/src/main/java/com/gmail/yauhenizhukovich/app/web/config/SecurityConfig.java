package com.gmail.yauhenizhukovich.app.web.config;

import com.gmail.yauhenizhukovich.app.web.security.LoginAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

import static com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService.ADMINISTRATOR;
import static com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService.CUSTOMER_USER;
import static com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService.SALE_USER;
import static com.gmail.yauhenizhukovich.app.service.model.user.RoleEnumService.SECURE_API_USER;
import static org.springframework.http.HttpMethod.GET;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {this.userDetailsService = userDetailsService;}

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new LoginAccessDeniedHandler();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/users/**", "/reviews")
                .hasAnyRole(ADMINISTRATOR.name())
                .antMatchers(GET, "/articles", "/articles/*", "/items", "/items/*")
                .hasAnyRole(CUSTOMER_USER.name(), SALE_USER.name())
                .antMatchers("/reviews/add")
                .hasAnyRole(CUSTOMER_USER.name())
                .antMatchers(HttpMethod.POST, "/orders", "/reviews/add")
                .hasAnyRole(CUSTOMER_USER.name())
                .antMatchers("/articles/**", "/items/**", "/orders/**")
                .hasAnyRole(SALE_USER.name())
                .antMatchers("/api/**")
                .hasAnyRole(SECURE_API_USER.name())
                .antMatchers("login")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/")
                .permitAll()
                .and()
                .logout()
                .permitAll()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .csrf()
                .disable();
    }

}
