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
        List<PlayerIndex> playerIndices = playerModel.getAll();
        request.setAttribute("playerIndices", playerIndices);
        request.getRequestDispatcher("player.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ form, kiểm tra tránh lỗi
            String name = request.getParameter("name");
            String fullName = request.getParameter("fullName");
            int age = Integer.parseInt(request.getParameter("age").trim());
            int indexId = Integer.parseInt(request.getParameter("indexId").trim());
            float value = Float.parseFloat(request.getParameter("value").trim());

            if (name == null || fullName == null || name.isEmpty() || fullName.isEmpty()) {
                throw new IllegalArgumentException("Tên không được để trống!");
            }

            boolean isSuccess = playerModel.addPlayer(name, fullName, age, indexId, value);

            if (isSuccess) {
                request.getSession().setAttribute("message", "Player added successfully!");
            } else {
                request.getSession().setAttribute("message", "Failed to add player.");
            }

            response.sendRedirect("player");
        } catch (NumberFormatException | IllegalArgumentException e) {
            request.getSession().setAttribute("message", "Invalid input: " + e.getMessage());
            response.sendRedirect("player");
        }
    }
}
