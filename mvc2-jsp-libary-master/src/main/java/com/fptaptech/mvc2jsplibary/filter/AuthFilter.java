package com.fptaptech.mvc2jsplibary.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter({"/books/*", "/members/*", "/loans/*", "/dashboard"})
public class AuthFilter implements Filter {

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (request.getSession().getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
        } else {
            chain.doFilter(request, response);
        }
    }
}