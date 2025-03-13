package controller;


import dao.PlayerDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Player;

import java.io.IOException;


@WebServlet("/addPlayer")
public class AddPlayerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Player player = new Player();
        player.setName(request.getParameter("name"));
        player.setFullName(request.getParameter("fullName"));
        player.setAge(request.getParameter("age"));
        player.setIndexId(Integer.parseInt(request.getParameter("indexId")));

        PlayerDAO playerDAO = new PlayerDAO();
        playerDAO.addPlayer(player);

        response.sendRedirect("listPlayers");
    }
}