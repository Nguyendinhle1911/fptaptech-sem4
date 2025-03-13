package controller;

import dao.PlayerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Player;

import java.io.IOException;

@WebServlet("/editPlayer")
public class EditPlayerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int playerId = Integer.parseInt(request.getParameter("id"));
            Player player = PlayerDAO.getPlayerById(playerId);

            if (player != null) {
                request.setAttribute("player", player);
                request.setAttribute("editMode", true); // Đánh dấu đang sửa cầu thủ
            } else {
                request.setAttribute("error", "Không tìm thấy cầu thủ!");
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ!");
        }

        // Chuyển về index.jsp để hiển thị form sửa
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
