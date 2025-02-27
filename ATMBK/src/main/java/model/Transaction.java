package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private int transactionId;
    private int accountId;
    private String type;
    private double amount;
    private Integer targetAccountId;
    private Timestamp transactionDate;

    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }
    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }
    public Integer getTargetAccountId() { return targetAccountId; }
    public void setTargetAccountId(Integer targetAccountId) { this.targetAccountId = targetAccountId; }
    public Timestamp getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Timestamp transactionDate) { this.transactionDate = transactionDate; }

    public static List<Transaction> getByAccountId(int accountId) throws Exception {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE account_id = ? ORDER BY transaction_date DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accountId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Transaction t = new Transaction();
                t.setTransactionId(rs.getInt("transaction_id"));
                t.setAccountId(rs.getInt("account_id"));
                t.setType(rs.getString("type"));
                t.setAmount(rs.getDouble("amount"));
                t.setTargetAccountId(rs.getInt("target_account_id") == 0 ? null : rs.getInt("target_account_id"));
                t.setTransactionDate(rs.getTimestamp("transaction_date"));
                transactions.add(t);
            }
        }
        return transactions;
    }

    public static boolean withdraw(int accountId, double amount) throws Exception {
        Account account = Account.getById(accountId);
        double maxWithdraw = 5000.0;

        if (account != null && amount > 0 && amount <= maxWithdraw && amount <= account.getBalance()) {
            double newBalance = account.getBalance() - amount;
            new Account().updateBalance(accountId, newBalance);

            String query = "INSERT INTO transactions (account_id, type, amount) VALUES (?, 'withdraw', ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, accountId);
                ps.setDouble(2, amount);
                ps.executeUpdate();
            }
            return true;
        }
        return false;
    }

    public static boolean transfer(int fromAccountId, int toAccountId, double amount) throws Exception {
        Account fromAccount = Account.getById(fromAccountId);
        Account toAccount = Account.getById(toAccountId);

        if (fromAccount != null && toAccount != null && amount > 0 && amount <= fromAccount.getBalance()) {
            double fromNewBalance = fromAccount.getBalance() - amount;
            double toNewBalance = toAccount.getBalance() + amount;

            new Account().updateBalance(fromAccountId, fromNewBalance);
            new Account().updateBalance(toAccountId, toNewBalance);

            String query = "INSERT INTO transactions (account_id, type, amount, target_account_id) VALUES (?, 'transfer', ?, ?)";
            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, fromAccountId);
                ps.setDouble(2, amount);
                ps.setInt(3, toAccountId);
                ps.executeUpdate();
            }
            return true;
        }
        return false;
    }

    public void create(int accountId, String type, double amount, Integer targetAccountId) throws Exception {
        String query = "INSERT INTO transactions (account_id, type, amount, target_account_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, accountId);
            ps.setString(2, type);
            ps.setDouble(3, amount);
            if (targetAccountId == null) ps.setNull(4, Types.INTEGER);
            else ps.setInt(4, targetAccountId);
            ps.executeUpdate();
        }
    }

    public void delete(int transactionId) throws Exception {
        String query = "DELETE FROM transactions WHERE transaction_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, transactionId);
            ps.executeUpdate();
        }
    }
}