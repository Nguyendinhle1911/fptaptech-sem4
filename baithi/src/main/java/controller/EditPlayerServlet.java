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
        int playerId = Integer.parseInt(request.getParameter("id"));
        PlayerDAO playerDAO = new PlayerDAO();
        Player player = playerDAO.getPlayerById(playerId);
        request.setAttribute("player", player);
        request.getRequestDispatcher("editPlayer.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Player player = new Player();
        player.setPlayerId(Integer.parseInt(request.getParameter("playerId")));
        player.setName(request.getParameter("name"));
        player.setFullName(request.getParameter("fullName"));
        player.setAge(request.getParameter("age"));
        player.setIndexId(Integer.parseInt(request.getParameter("indexId")));

        PlayerDAO playerDAO = new PlayerDAO();
        playerDAO.updatePlayer(player);

        response.sendRedirect("listPlayers");
    }
}