package com.example.spring_security.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthentication implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        var authorities = AuthorityUtils.authorityListToSet(authentication.getAuthorities());

        if (authorities.contains("ROLE_ADMIN")) {
            response.sendRedirect("/admin");
        } else if (authorities.contains("ROLE_USER")) {
            response.sendRedirect("/user");
        } else {
            response.sendRedirect("/public");
        }
    }
}
