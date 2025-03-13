package entity;

import util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Player {
    private int playerId;
    private String name;
    private String fullName;
    private int age;  // Changed back to int to match original functionality
    private int indexId;

    // Constructors
    public Player() {}

    public Player(String name, String fullName, int age, int indexId) {
        this.name = name;
        this.fullName = fullName;
        this.age = age;
        this.indexId = indexId;
    }

    // Getters and Setters
    public int getPlayerId() { return playerId; }
    public void setPlayerId(int playerId) { this.playerId = playerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public int getIndexId() { return indexId; }
    public void setIndexId(int indexId) { this.indexId = indexId; }

    // Nested PlayerIndex class
    public static class PlayerIndex {
        private int id;
        private String playerName;
        private int age;
        private String indexName;
        private float value;

        public PlayerIndex(int id, String playerName, int age, String indexName, float value) {
            this.id = id;
            this.playerName = playerName;
            this.age = age;
            this.indexName = indexName;
            this.value = value;
        }

        // Getters
        public int getId() { return id; }
        public String getPlayerName() { return playerName; }
        public int getAge() { return age; }
        public String getIndexName() { return indexName; }
        public float getValue() { return value; }
    }

    // Get all players
    public List<PlayerIndex> getAll() {
        List<PlayerIndex> playerIndices = new ArrayList<>();
        String sql = "SELECT p.player_id, p.name AS player_name, p.age, i.name AS index_name, pi.value " +
                "FROM player p " +
                "LEFT JOIN player_index pi ON p.player_id = pi.player_id " +
                "LEFT JOIN indexer i ON pi.index_id = i.index_id";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String ageStr = rs.getString("age");
                int age = 0;
                try {
                    age = ageStr != null ? Integer.parseInt(ageStr) : 0;
                } catch (NumberFormatException e) {
                    System.err.println("Invalid age format for player: " + rs.getString("player_name"));
                }

                PlayerIndex playerIndex = new PlayerIndex(
                        rs.getInt("player_id"),
                        rs.getString("player_name"),
                        age,
                        rs.getString("index_name"),
                        rs.getFloat("value")
                );
                playerIndices.add(playerIndex);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return playerIndices;
    }

    public boolean addPlayer(String name, String fullName, int age, int indexId, float value) {
        String insertPlayerSQL = "INSERT INTO player (name, full_name, age, index_id) VALUES (?, ?, ?, ?)";
        String insertPlayerIndexSQL = "INSERT INTO player_index (player_id, index_id, value) VALUES (?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement playerStmt = conn.prepareStatement(insertPlayerSQL, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement playerIndexStmt = conn.prepareStatement(insertPlayerIndexSQL)) {

            // Thêm Player vào bảng player
            playerStmt.setString(1, name);
            playerStmt.setString(2, name);
            playerStmt.setInt(3, age);
            playerStmt.setInt(4, indexId);
            int affectedRows = playerStmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating player failed, no rows affected.");
            }

            // Lấy player_id vừa được tạo
            try (ResultSet generatedKeys = playerStmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int playerId = generatedKeys.getInt(1);

                    // Thêm giá trị vào bảng player_index
                    playerIndexStmt.setInt(1, playerId);
                    playerIndexStmt.setInt(2, indexId);
                    playerIndexStmt.setFloat(3, value);
                    playerIndexStmt.executeUpdate();

                    return true;
                } else {
                    throw new SQLException("Creating player failed, no ID obtained.");
                }
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean editPlayer(int playerId, String name, String fullName, int age, int indexId, float value) {
        String updatePlayerSQL = "UPDATE player SET name = ?, full_name = ?, age = ?, index_id = ? WHERE player_id = ?";
        String updatePlayerIndexSQL = "UPDATE player_index SET index_id = ?, value = ? WHERE player_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement playerStmt = conn.prepareStatement(updatePlayerSQL);
             PreparedStatement playerIndexStmt = conn.prepareStatement(updatePlayerIndexSQL)) {

            // Cập nhật thông tin Player trong bảng player
            playerStmt.setString(1, name);
            playerStmt.setString(2, fullName);
            playerStmt.setInt(3, age);
            playerStmt.setInt(4, indexId);
            playerStmt.setInt(5, playerId);
            int playerRowsAffected = playerStmt.executeUpdate();

            // Cập nhật thông tin trong bảng player_index
            playerIndexStmt.setInt(1, indexId);
            playerIndexStmt.setFloat(2, value);
            playerIndexStmt.setInt(3, playerId);
            int playerIndexRowsAffected = playerIndexStmt.executeUpdate();

            // Kiểm tra xem có bản ghi nào được cập nhật không
            if (playerRowsAffected > 0 && playerIndexRowsAffected > 0) {
                return true;
            } else {
                throw new SQLException("Updating player failed, no rows affected.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean deletePlayer(int playerId) {
        String deletePlayerIndexSQL = "DELETE FROM player_index WHERE player_id = ?";
        String deletePlayerSQL = "DELETE FROM player WHERE player_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement playerIndexStmt = conn.prepareStatement(deletePlayerIndexSQL);
             PreparedStatement playerStmt = conn.prepareStatement(deletePlayerSQL)) {

            // Xóa thông tin trong bảng player_index trước
            playerIndexStmt.setInt(1, playerId);
            int playerIndexRowsAffected = playerIndexStmt.executeUpdate();

            // Xóa thông tin trong bảng player
            playerStmt.setInt(1, playerId);
            int playerRowsAffected = playerStmt.executeUpdate();

            // Kiểm tra xem có bản ghi nào được xóa không
            if (playerRowsAffected > 0) {
                return true;
            } else {
                throw new SQLException("Deleting player failed, no rows affected.");
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }
}