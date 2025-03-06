package com.fptaptech.demo2.controller;

import com.fptaptech.demo2.model.User;
import com.fptaptech.demo2.utils.JWTUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "AuthServlet", urlPatterns = {"/auth"})
public class AuthServlet extends HttpServlet {

    private static final User adminUser = new User("admin", "admin", "ADMIN");
    private static final User normalUser = new User("user", "user", "USER");

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User matchedUser = null;
        if (adminUser.getUsername().equals(username) && adminUser.getPassword().equals(password)) {
            matchedUser = adminUser;
        } else if (normalUser.getUsername().equals(username) && normalUser.getPassword().equals(password)) {
            matchedUser = normalUser;
        }

        if (matchedUser != null) {
            // Tạo JWT file
            String token = JWTUtils.generateToken(matchedUser.getUsername(), matchedUser.getRole());
            // Lưu token vào cookie
            Cookie jwtCookie = new Cookie("jwt_token", token);
            jwtCookie.setMaxAge(10 * 60); // 10 phút
            jwtCookie.setPath("/");
            response.addCookie(jwtCookie);

            if ("ADMIN".equals(matchedUser.getRole())) {
                response.sendRedirect(request.getContextPath() + "/admin.jsp");
            } else {
                response.sendRedirect(request.getContextPath() + "/user.jsp");
            }
        } else {
            request.setAttribute("error", "Invalid credentials!");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}