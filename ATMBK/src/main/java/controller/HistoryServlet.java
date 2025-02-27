package controller;

import model.Transaction;
import model.Account;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/history")
public class HistoryServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        int accountId;
        try {
            accountId = Integer.parseInt(request.getParameter("accountId"));
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid account ID");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
            return;
        }

        try {
            request.setAttribute("transactions", Transaction.getByAccountId(accountId));
            request.setAttribute("accounts", Account.getAccountsByUserId(user.getUserId()));
            request.setAttribute("user", user);
            request.getRequestDispatcher("history.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred while fetching history");
            request.getRequestDispatcher("dashboard.jsp").forward(request, response);
        }
    }
}