package dao;

import model.Indexer;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IndexerDAO {

    // Lấy danh sách tất cả chỉ số
    public static List<Indexer> getAllIndexers() {
        List<Indexer> indexers = new ArrayList<>();
        String query = "SELECT * FROM indexer";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                indexers.add(new Indexer(
                        rs.getInt("index_id"),
                        rs.getString("name"),
                        rs.getFloat("valueMin"),
                        rs.getFloat("valueMax")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return indexers;
    }

    // Lấy chỉ số theo ID
    public static Indexer getIndexerById(int indexId) {
        String query = "SELECT * FROM indexer WHERE index_id = ?";
        Indexer indexer = null;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, indexId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                indexer = new Indexer(
                        rs.getInt("index_id"),
                        rs.getString("name"),
                        rs.getFloat("valueMin"),
                        rs.getFloat("valueMax")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return indexer;
    }
}
