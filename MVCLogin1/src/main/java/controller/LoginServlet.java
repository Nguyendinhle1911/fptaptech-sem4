package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import model.User;
import util.JwtUtil;

import java.io.IOException;

@WebServlet(name = "LoginServlet", value = "/login")
public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doPost(req, resp);
        //Step 1
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        //Step 2
        User user = new User();
        boolean isValid = user.checkLogin(username , password);

        //Step 3
        if(isValid) {
//            req.setAttribute("username", username);
//            req.getRequestDispatcher("welcome.jsp").forward(req, resp);
//            HttpSession session = req.getSession();
//            session.setAttribute("username", username);
//            resp.sendRedirect("welcome.jsp");

            String token = JwtUtil.generateToken(username);

            Cookie cookie = new Cookie("jwt_token", token);
            cookie.setHttpOnly(true);
            cookie.setMaxAge(60 * 60 *24);
            cookie.setPath("/");

            resp.addCookie(cookie);
            resp.sendRedirect("welcome.jsp");
        }else {
            req.setAttribute("error", "Invalid username or password");
            req.getRequestDispatcher("login.jsp").forward(req, resp);
        }
    }
}
