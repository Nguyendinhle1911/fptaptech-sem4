package controller;

import dao.PlayerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/deletePlayer")
public class DeletePlayerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int playerId = Integer.parseInt(request.getParameter("id"));

            boolean success = PlayerDAO.deletePlayer(playerId);

            if (success) {
                response.sendRedirect("index.jsp?success=Xóa cầu thủ thành công");
            } else {
                response.sendRedirect("index.jsp?error=Không thể xóa cầu thủ");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("index.jsp?error=ID không hợp lệ");
        }
    }
}
