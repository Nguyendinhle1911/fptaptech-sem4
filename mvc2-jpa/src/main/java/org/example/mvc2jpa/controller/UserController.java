package org.example.mvc2jpa.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.mvc2jpa.dao.UserDAO;
import org.example.mvc2jpa.model.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/users")
public class UserController extends HttpServlet {

    private UserDAO userDAO ;

    @Override
    public void init() throws ServletException {
        userDAO = new UserDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> users = userDAO.getAllUsers();
        req.setAttribute("users", users);
        req.getRequestDispatcher("user-list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        User user = new User(username,email);
        userDAO.addUser(user);
        resp.sendRedirect("user-list.jsp");
    }
}
