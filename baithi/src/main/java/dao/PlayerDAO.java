package dao;

import model.Player;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerDAO {

    // Lấy tất cả cầu thủ
    public static List<Player> getAllPlayers() {
        List<Player> players = new ArrayList<>();
        String query = "SELECT * FROM player";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                players.add(new Player(
                        rs.getInt("player_id"),
                        rs.getString("name"),
                        rs.getString("full_name"),
                        rs.getInt("age"),
                        rs.getInt("index_id")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return players;
    }

    // Thêm cầu thủ mới
    public static boolean addPlayer(Player player) {
        String query = "INSERT INTO player (name, full_name, age, index_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, player.getName());
            stmt.setString(2, player.getFullName());
            stmt.setInt(3, player.getAge());
            stmt.setInt(4, player.getIndexId());

            return stmt.executeUpdate() > 0; // Trả về true nếu thành công
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Cập nhật cầu thủ
    public static boolean updatePlayer(Player player) {
        String query = "UPDATE player SET name = ?, full_name = ?, age = ?, index_id = ? WHERE player_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, player.getName());
            stmt.setString(2, player.getFullName());
            stmt.setInt(3, player.getAge());
            stmt.setInt(4, player.getIndexId());
            stmt.setInt(5, player.getPlayerId());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Xóa cầu thủ
    public static boolean deletePlayer(int playerId) {
        String query = "DELETE FROM player WHERE player_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, playerId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static Player getPlayerById(int playerId) {
        String query = "SELECT * FROM player WHERE player_id = ?";
        Player player = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                player = new Player(
                        rs.getInt("player_id"),
                        rs.getString("name"),
                        rs.getString("full_name"),
                        rs.getInt("age"),
                        rs.getInt("index_id")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return player;
    }

}
