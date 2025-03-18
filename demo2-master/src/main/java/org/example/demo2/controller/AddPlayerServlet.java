package org.example.demo2.controller;

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

@WebServlet("/addPlayer")
public class AddPlayerServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String playerName = request.getParameter("playerName");
        String playerAge = request.getParameter("playerAge");
        String indexName = request.getParameter("indexName");
        String value = request.getParameter("value");

        if (validateData(playerName, playerAge, indexName, value)) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                // Thêm vào bảng player
                String sql = "INSERT INTO player (name, full_name, age, index_id) VALUES (?, ?, ?, ?)";
                PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
                pstmt.setString(1, playerName);
                pstmt.setString(2, playerName); // full_name tạm thời
                pstmt.setString(3, playerAge);
                pstmt.setInt(4, getIndexId(indexName));
                pstmt.executeUpdate();

                // Lấy playerId vừa chèn
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                int playerId = 0;
                if (generatedKeys.next()) {
                    playerId = generatedKeys.getInt(1);
                }

                // Thêm vào player_index
                sql = "INSERT INTO player_index (player_id, index_id, value) VALUES (?, ?, ?)";
                pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, playerId);
                pstmt.setInt(2, getIndexId(indexName));
                pstmt.setFloat(3, Float.parseFloat(value));
                pstmt.executeUpdate();

                response.sendRedirect("player.jsp");
            } catch (SQLException e) {
                e.printStackTrace();
                response.sendRedirect("player.jsp?error=1");
            }
        } else {
            response.sendRedirect("player.jsp?error=validation");
        }
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
            return 1; // Giả định index_id mặc định
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