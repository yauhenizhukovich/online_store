package com.gmail.yauhenizhukovich.app.web.security;

import java.io.IOException;
import java.lang.invoke.MethodHandles;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

public class LoginAccessDeniedHandler implements AccessDeniedHandler {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static final String ACCESS_DENIED_MESSAGE = "You don't have access to this resource.";

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            logger.info(
                    authentication.getName() +
                            " was trying to access protected resource: " +
                            httpServletRequest.getRequestURI()
            );
        }
        httpServletResponse.sendRedirect(
                httpServletRequest.getContextPath() + "/welcome?message=" + ACCESS_DENIED_MESSAGE
        );
    }

}
