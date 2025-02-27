package filter;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import util.JwtUtil;

import java.io.IOException;

@WebFilter("/*")
public class JwtFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;


        String token = null;
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("jwt_token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null) {
            String username = JwtUtil.validateToken(token);
            if (username != null) {

                chain.doFilter(request, response);
                return;
            }
        }

        if (!req.getRequestURI().endsWith("login.jsp") && !req.getRequestURI().endsWith("login")) {
            resp.sendRedirect("login.jsp");
        } else {
            chain.doFilter(request, response);
        }
    }
}
