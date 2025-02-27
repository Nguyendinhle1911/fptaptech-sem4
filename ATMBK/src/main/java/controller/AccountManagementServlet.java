package controller;

import model.Account;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/accountManagement")
public class AccountManagementServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String action = request.getParameter("action");
        Account account = new Account();

        try {
            if ("create".equals(action)) {
                int userId = Integer.parseInt(request.getParameter("userId"));
                String accountNumber = request.getParameter("accountNumber");
                double balance = Double.parseDouble(request.getParameter("balance"));
                account.create(userId, accountNumber, balance);
            } else if ("delete".equals(action)) {
                int accountId = Integer.parseInt(request.getParameter("accountId"));
                account.delete(accountId);
            }
            session.setAttribute("accounts", Account.getAccountsByUserId(user.getUserId()));
            response.sendRedirect("dashboard.jsp");
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during account management");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
    }
}