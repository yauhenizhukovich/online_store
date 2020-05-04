package com.gmail.yauhenizhukovich.app.service.util;

import java.util.Collection;

import com.gmail.yauhenizhukovich.app.repository.model.RoleEnumRepository;
import com.gmail.yauhenizhukovich.app.repository.model.User;
import com.gmail.yauhenizhukovich.app.repository.model.UserDetails;
import org.mockito.Mockito;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_EMAIL;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_FIRSTNAME;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_LASTNAME;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_PASSWORD;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_PATRONYMIC;
import static com.gmail.yauhenizhukovich.app.service.constant.ServiceUnitTestConstant.VALID_ROLE;
import static org.mockito.Mockito.when;

public class ServiceUnitTestUtil {

    public static void mockAuthentication(String authName) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn(authName);
    }

    public static void mockAuthentication(Collection authorities) {
        Authentication authentication = Mockito.mock(Authentication.class);
        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getAuthorities()).thenReturn(authorities);
    }

    public static User getUser() {
        User user = new User();
        user.setEmail(VALID_EMAIL);
        user.setPassword(VALID_PASSWORD);
        user.setRole(RoleEnumRepository.valueOf(VALID_ROLE.name()));
        UserDetails userDetails = new UserDetails();
        userDetails.setFirstName(VALID_FIRSTNAME);
        userDetails.setLastName(VALID_LASTNAME);
        userDetails.setPatronymic(VALID_PATRONYMIC);
        user.setUserDetails(userDetails);
        return user;
    }

}
