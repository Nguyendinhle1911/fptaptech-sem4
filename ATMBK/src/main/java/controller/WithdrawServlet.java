package controller;

import model.Transaction;
import model.Account;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;

@WebServlet("/withdraw")
public class WithdrawServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String accountIdStr = request.getParameter("accountId");
        String amountStr = request.getParameter("amount");

        if (accountIdStr == null || accountIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Account is missing");
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
            return;
        }

        int accountId;
        try {
            accountId = Integer.parseInt(accountIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid account ID");
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
            return;
        }

        if (amountStr == null || amountStr.trim().isEmpty()) {
            request.setAttribute("error", "Amount is required");
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                request.setAttribute("error", "Amount must be greater than 0");
                request.getRequestDispatcher("withdraw.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format");
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
            return;
        }

        try {
            if (Transaction.withdraw(accountId, amount)) {
                session.setAttribute("accounts", Account.getAccountsByUserId(user.getUserId()));
                response.sendRedirect("dashboard.jsp");
            } else {
                request.setAttribute("error", "Invalid amount or insufficient balance");
                request.getRequestDispatcher("withdraw.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during withdrawal");
            request.getRequestDispatcher("withdraw.jsp").forward(request, response);
        }
    }
}