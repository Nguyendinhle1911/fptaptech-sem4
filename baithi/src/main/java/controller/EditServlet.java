package controller;

import entity.Player;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "EditServlet", urlPatterns = {"/edit"})
public class EditServlet extends HttpServlet {
    private Player playerModel;

    @Override
    public void init() throws ServletException {
        playerModel = new Player();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Lấy dữ liệu từ form, kiểm tra tránh lỗi
            int playerId = Integer.parseInt(request.getParameter("playerId").trim());
            String name = request.getParameter("name");
            String fullName = request.getParameter("fullName");
            int age = Integer.parseInt(request.getParameter("age").trim());
            int indexId = Integer.parseInt(request.getParameter("indexId").trim());
            float value = Float.parseFloat(request.getParameter("value").trim());

            if (name == null || fullName == null || name.isEmpty() || fullName.isEmpty()) {
                throw new IllegalArgumentException("Tên không được để trống!");
            }

            boolean isUpdated = playerModel.editPlayer(playerId, name, fullName, age, indexId, value);

            if (isUpdated) {
                request.getSession().setAttribute("message", "Player updated successfully!");
            } else {
                request.getSession().setAttribute("message", "Failed to update player.");
            }

            response.sendRedirect("player");
        } catch (NumberFormatException | IllegalArgumentException e) {
            request.getSession().setAttribute("message", "Invalid input: " + e.getMessage());
            response.sendRedirect("player");
        }
    }
}
