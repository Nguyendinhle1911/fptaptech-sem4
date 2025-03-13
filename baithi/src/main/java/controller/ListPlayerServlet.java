package controller;

import dao.PlayerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Player;

import java.io.IOException;
import java.util.List;

@WebServlet("/listPlayers")
public class ListPlayerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy danh sách cầu thủ từ database
        List<Player> players = PlayerDAO.getAllPlayers();

        // Gửi dữ liệu qua index.jsp
        request.setAttribute("players", players);
        request.getRequestDispatcher("index.jsp").forward(request, response);
    }
}
