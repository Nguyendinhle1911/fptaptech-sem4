package controller;

import model.User;
import model.Account;
import util.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.Date;
import java.util.List;

@WebServlet({"/login", "/logout"}) // Thêm cả /login và /logout vào cùng servlet
public class LoginServlet extends HttpServlet {

    // Xử lý yêu cầu POST cho cả login và logout
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String servletPath = request.getServletPath(); // Lấy đường dẫn để phân biệt /login hay /logout

        if ("/login".equals(servletPath)) {
            // Xử lý đăng nhập
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            try {
                User user = User.authenticate(username, password);
                if (user != null) {
                    // Tạo JWT token
                    String token = JwtUtil.generateToken(user.getUsername());
                    Cookie jwtCookie = new Cookie("jwt", token);
                    jwtCookie.setHttpOnly(true);
                    jwtCookie.setPath("/");
                    response.addCookie(jwtCookie);

                    // Thêm cookie riêng cho userId
                    Cookie userIdCookie = new Cookie("userId", String.valueOf(user.getUserId()));
                    userIdCookie.setHttpOnly(true);
                    userIdCookie.setPath("/");
                    response.addCookie(userIdCookie);

                    // Lưu thông tin user và accounts vào session
                    HttpSession session = request.getSession();
                    session.setAttribute("user", user);
                    session.setAttribute("accounts", Account.getAccountsByUserId(user.getUserId()));

                    // Lấy thời gian hết hạn của token và lưu vào session
                    Date expiration = JwtUtil.getClaims(token).getExpiration();
                    session.setAttribute("tokenExpiration", expiration);

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
        } else if ("/logout".equals(servletPath)) {
            // Xử lý đăng xuất
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.invalidate(); // Xóa toàn bộ session, bao gồm token
            }

            // Xóa cookie
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("jwt".equals(cookie.getName()) || "userId".equals(cookie.getName())) {
                        cookie.setMaxAge(0); // Đặt thời gian sống của cookie về 0 để xóa
                        cookie.setPath("/");
                        response.addCookie(cookie);
                    }
                }
            }

            response.sendRedirect("login.jsp"); // Chuyển hướng về trang login
        }
    }
}