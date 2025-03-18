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
        try {
            int playerId = Integer.parseInt(request.getParameter("playerId").trim());

            boolean isDeleted = playerModel.deletePlayer(playerId);

            if (isDeleted) {
                request.getSession().setAttribute("message", "Player deleted successfully!");
            } else {
                request.getSession().setAttribute("message", "Failed to delete player.");
            }

            response.sendRedirect("player");
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("message", "Invalid player ID!");
            response.sendRedirect("player");
        }
    }
}
