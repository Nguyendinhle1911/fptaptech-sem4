package controller;

import entity.Player;
import entity.Player.PlayerIndex;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "PlayerServlet", urlPatterns = {"/player"})
public class PlayerServlet extends HttpServlet {
    private Player playerModel;

    @Override
    public void init() throws ServletException {
        playerModel = new Player();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Fetch all player-index data
        List<PlayerIndex> playerIndices = playerModel.getAll();
        request.setAttribute("playerIndices", playerIndices);

        // Forward to JSP
        request.getRequestDispatcher("player.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String name = request.getParameter("name");
        String fullName = request.getParameter("fullName");
        int age = Integer.parseInt(request.getParameter("age"));
        int indexId = Integer.parseInt(request.getParameter("indexId"));
        float value = Float.parseFloat(request.getParameter("value"));

        // Thêm Player và PlayerIndex vào database
        boolean isSuccess = playerModel.addPlayer(name, fullName, age, indexId, value);

        // Chuyển hướng về trang chính sau khi thêm
        if (isSuccess) {
            request.setAttribute("message", "Player and value added successfully!");
        } else {
            request.setAttribute("message", "Failed to add player and value.");
        }

        // Fetch lại dữ liệu và hiển thị
        List<PlayerIndex> playerIndices = playerModel.getAll();
        request.setAttribute("playerIndices", playerIndices);
        request.getRequestDispatcher("player.jsp").forward(request, response);
    }

}