package controller;

import model.User;
import model.Account;
import util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            User user = User.authenticate(username, password);
            if (user != null) {
                String token = JwtUtil.generateToken(user.getUsername());
                Cookie jwtCookie = new Cookie("jwt", token);
                jwtCookie.setHttpOnly(true);
                jwtCookie.setPath("/");
                response.addCookie(jwtCookie);

                // Lưu thông tin user và accounts vào session
                HttpSession session = request.getSession();
                session.setAttribute("user", user);
                session.setAttribute("accounts", Account.getAccountsByUserId(user.getUserId()));

                response.sendRedirect("dashboard.jsp");
            } else {
                request.setAttribute("error", "Invalid credentials");
                request.getRequestDispatcher("login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred");
            request.getRequestDispatcher("login.jsp").forward(request, response);
        }
    }
}