package controller;

import model.Transaction;
import model.Account;
import model.User;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.util.List;

@WebServlet("/transfer")
public class TransferServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String fromAccountIdStr = request.getParameter("fromAccountId");
        String toAccountNumber = request.getParameter("toAccountNumber");
        String amountStr = request.getParameter("amount");

        if (fromAccountIdStr == null || fromAccountIdStr.trim().isEmpty()) {
            request.setAttribute("error", "Source account is missing");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
            return;
        }

        int fromAccountId;
        try {
            fromAccountId = Integer.parseInt(fromAccountIdStr);
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid source account ID");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
            return;
        }

        if (amountStr == null || amountStr.trim().isEmpty()) {
            request.setAttribute("error", "Amount is required");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
            return;
        }

        double amount;
        try {
            amount = Double.parseDouble(amountStr);
            if (amount <= 0) {
                request.setAttribute("error", "Amount must be greater than 0");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid amount format");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
            return;
        }

        if (toAccountNumber == null || toAccountNumber.trim().isEmpty()) {
            request.setAttribute("error", "Target account number is required");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
            return;
        }

        try {
            List<Account> allAccounts = Account.getAllAccounts();
            Integer toAccountId = null;
            for (Account account : allAccounts) {
                if (account.getAccountNumber().equals(toAccountNumber)) {
                    toAccountId = account.getAccountId();
                    break;
                }
            }

            if (toAccountId == null) {
                request.setAttribute("error", "Target account does not exist");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }

            if (toAccountId == fromAccountId) {
                request.setAttribute("error", "Cannot transfer to the same account");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
                return;
            }

            if (Transaction.transfer(fromAccountId, toAccountId, amount)) {
                session.setAttribute("accounts", Account.getAccountsByUserId(user.getUserId()));
                response.sendRedirect("dashboard.jsp");
            } else {
                request.setAttribute("error", "Invalid amount or insufficient balance");
                request.getRequestDispatcher("transfer.jsp").forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "An error occurred during transfer");
            request.getRequestDispatcher("transfer.jsp").forward(request, response);
        }
    }
}