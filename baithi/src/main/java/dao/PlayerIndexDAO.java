package dao;

import model.PlayerIndex;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PlayerIndexDAO {

    // Lấy danh sách chỉ số của một cầu thủ
    public static List<PlayerIndex> getPlayerIndexes(int playerId) {
        List<PlayerIndex> playerIndexes = new ArrayList<>();
        String query = "SELECT * FROM player_index WHERE player_id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, playerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                playerIndexes.add(new PlayerIndex(
                        rs.getInt("id"),
                        rs.getInt("player_id"),
                        rs.getInt("index_id"),
                        rs.getFloat("value")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return playerIndexes;
    }
}
