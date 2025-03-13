package controller;

import entity.Player;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "DeleteServlet", urlPatterns = {"/delete"})
public class DeleteServlet extends HttpServlet {
    private Player playerModel;

    @Override
    public void init() throws ServletException {
        playerModel = new Player();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Lấy playerId từ request
        int playerId = Integer.parseInt(request.getParameter("playerId"));

        // Xóa Player
        boolean isDeleted = playerModel.deletePlayer(playerId);

        // Chuyển hướng về trang chính sau khi xóa
        if (isDeleted) {
            request.setAttribute("message", "Player deleted successfully!");
        } else {
            request.setAttribute("message", "Failed to delete player.");
        }

        // Chuyển hướng về trang chính
        response.sendRedirect(request.getContextPath() + "/player");
    }
}