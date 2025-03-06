package com.fptaptech.mvc2jsplibary.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("AuthServlet: doGet called"); // Debug
        String action = request.getParameter("action");
        HttpSession session = request.getSession();

        if ("logout".equals(action)) {
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/auth");
        } else {
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }


        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            System.out.println("AuthServlet: doPost called"); // Debug
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            if ("admin".equals(username) && "123".equals(password)) {  // 🚨 Đổi mật khẩu cứng thành "123"
                HttpSession session = request.getSession();
                session.setAttribute("user", username);
                response.sendRedirect(request.getContextPath() + "/books");
            } else {
                request.setAttribute("error", "Sai tài khoản hoặc mật khẩu!");
                request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            }
        }

    }