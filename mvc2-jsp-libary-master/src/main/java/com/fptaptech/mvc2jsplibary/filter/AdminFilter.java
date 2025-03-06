package com.fptaptech.mvc2jsplibary.filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

@WebFilter({"/books/*", "/members/*", "/loans/*", "/dashboard"})
public class AdminFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false); // Lấy session hiện tại, không tạo mới nếu không tồn tại

        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        // Giả định rằng session có một thuộc tính "role" để xác định vai trò người dùng
        String userRole = (String) session.getAttribute("role");

        // Kiểm tra xem người dùng có phải là admin hay không
        if (userRole != null && userRole.equals("admin")) {
            // Nếu là admin, cho phép tiếp tục truy cập
            chain.doFilter(request, response);
        } else {
            // Nếu không phải admin, chuyển hướng về trang không được phép hoặc trang chủ
            response.sendRedirect(request.getContextPath() + "/access-denied");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Không cần khởi tạo gì đặc biệt
    }

    @Override
    public void destroy() {
        // Không cần dọn dẹp gì đặc biệt
    }
}