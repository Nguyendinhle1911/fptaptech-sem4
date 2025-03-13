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
        // Lấy dữ liệu từ form
        int playerId = Integer.parseInt(request.getParameter("playerId"));
        String name = request.getParameter("name");
        String fullName = request.getParameter("fullName");
        int age = Integer.parseInt(request.getParameter("age"));
        int indexId = Integer.parseInt(request.getParameter("indexId"));
        float value = Float.parseFloat(request.getParameter("value"));

        // Cập nhật thông tin Player
        boolean isUpdated = playerModel.editPlayer(playerId, name, fullName, age, indexId, value);

        // Chuyển hướng về trang chính sau khi cập nhật
        if (isUpdated) {
            request.setAttribute("message", "Player updated successfully!");
        } else {
            request.setAttribute("message", "Failed to update player.");
        }

        // Chuyển hướng về trang chính
        response.sendRedirect(request.getContextPath() + "/player");
    }
}