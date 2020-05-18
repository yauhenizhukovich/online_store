package com.gmail.yauhenizhukovich.app.service.impl;

import java.lang.invoke.MethodHandles;

import com.gmail.yauhenizhukovich.app.service.UserService;
import com.gmail.yauhenizhukovich.app.service.model.user.AppUser;
import com.gmail.yauhenizhukovich.app.service.model.user.LoginUserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final UserService userService;

    public AppUserDetailsService(UserService userService) {this.userService = userService;}

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        LoginUserDTO user = userService.getUserByEmail(email);
        if (user == null) {
            logger.debug("User '" + email + "' was not found.");
            throw new UsernameNotFoundException("User '" + email + "' was not found.");
        }
        return new AppUser(user);
    }

}
