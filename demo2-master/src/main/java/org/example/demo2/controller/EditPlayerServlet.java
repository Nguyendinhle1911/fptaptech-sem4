package org.example.demo2.controller;

import org.example.demo2.model.Player;
import org.example.demo2.utils.DatabaseConnection;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@WebServlet("/edit")
public class EditPlayerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        Player player = getPlayerById(id);
        request.setAttribute("player", player);
        request.getRequestDispatcher("/edit.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String playerName = request.getParameter("playerName");
        String playerAge = request.getParameter("playerAge");
        String indexName = request.getParameter("indexName");
        String value = request.getParameter("value");

        if (validateData(playerName, playerAge, indexName, value)) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                // Cập nhật bảng player
                String sql = "UPDATE player SET name = ?, age = ?, index_id = ? WHERE player_id = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, playerName);
                pstmt.setString(2, playerAge);
                pstmt.setInt(3, getIndexId(indexName));
                pstmt.setInt(4, id);
                pstmt.executeUpdate();

                // Cập nhật player_index
                sql = "UPDATE player_index SET value = ? WHERE player_id = ? AND index_id = ?";
                pstmt = conn.prepareStatement(sql);
                pstmt.setFloat(1, Float.parseFloat(value));
                pstmt.setInt(2, id);
                pstmt.setInt(3, getIndexId(indexName));
                pstmt.executeUpdate();

                response.sendRedirect("player.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("edit.jsp?id=" + id + "&error=1");
            }
        } else {
            response.sendRedirect("edit.jsp?id=" + id + "&error=validation");
        }
    }

    private Player getPlayerById(int id) {
        Player player = new Player();
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT p.*, i.name, pi.value FROM player p " +
                    "JOIN player_index pi ON p.player_id = pi.player_id " +
                    "JOIN indexer i ON pi.index_id = i.index_id " +
                    "WHERE p.player_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                player.setPlayerId(rs.getInt("player_id"));
                player.setName(rs.getString("name"));
                player.setAge(rs.getString("age"));
                player.setIndexId(rs.getInt("index_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return player;
    }

    private int getIndexId(String indexName) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT index_id FROM indexer WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, indexName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("index_id");
            }
            return 1;
        }
    }

    private boolean validateData(String playerName, String playerAge, String indexName, String value) {
        if (playerName == null || playerName.trim().isEmpty() || playerAge == null || playerAge.trim().isEmpty()) {
            return false;
        }
        try {
            float val = Float.parseFloat(value);
            float min = getMinValue(indexName);
            float max = getMaxValue(indexName);
            return val >= min && val <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private float getMinValue(String indexName) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT valueMin FROM indexer WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, indexName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("valueMin");
            }
            return 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    private float getMaxValue(String indexName) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT valueMax FROM indexer WHERE name = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, indexName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getFloat("valueMax");
            }
            return 100;
        } catch (SQLException e) {
            e.printStackTrace();
            return 100;
        }
    }
}