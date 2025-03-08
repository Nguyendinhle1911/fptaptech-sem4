package com.fptaptech.demo2.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

@WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
public class LogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Xóa session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Xóa cookie JWT
        Cookie jwtCookie = new Cookie("jwt_token", "");
        jwtCookie.setPath("/"); // Đảm bảo path giống lúc set cookie
        jwtCookie.setMaxAge(0); // Hết hạn ngay lập tức
        response.addCookie(jwtCookie);

        // Chuyển hướng về trang đăng nhập
        response.sendRedirect("login.jsp");
    }
}
