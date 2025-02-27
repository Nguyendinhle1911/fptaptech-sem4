package model;

import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private int id;
    private String name;
    private int quantity;

    public Product() {}

    public Product(int id, String name, int quantity) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
    }

    public Product(int id, String name, double price, String description) {
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public static boolean addProduct(Product product) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "INSERT INTO products (name, quantity) VALUES (?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, product.getName());
        pstmt.setInt(2, product.getQuantity());
        boolean result = pstmt.executeUpdate() > 0;
        DBUtil.close(conn, pstmt, null);
        return result;
    }

    public static List<Product> getAllProducts() throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "SELECT * FROM products";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        List<Product> productList = new ArrayList<>();
        while (rs.next()) {
            productList.add(new Product(rs.getInt("id"), rs.getString("name"), rs.getInt("quantity")));
        }
        DBUtil.close(conn, stmt, rs);
        return productList;
    }

    public static boolean updateProduct(Product product) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "UPDATE products SET name = ?, quantity = ? WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, product.getName());
        pstmt.setInt(2, product.getQuantity());
        pstmt.setInt(3, product.getId());
        boolean result = pstmt.executeUpdate() > 0;
        DBUtil.close(conn, pstmt, null);
        return result;
    }

    public static boolean deleteProduct(int id) throws SQLException, ClassNotFoundException {
        Connection conn = DBUtil.getConnection();
        String sql = "DELETE FROM products WHERE id = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setInt(1, id);
        boolean result = pstmt.executeUpdate() > 0;
        DBUtil.close(conn, pstmt, null);
        return result;
    }
}
