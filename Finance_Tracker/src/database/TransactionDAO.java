package database;

import model.Transaction;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/FinanceTrackerDB";
    private static final String USER = "root";
    private static final String PASSWORD = "database";

    // Insert transaction
    public static void addTransaction(String type, double amount, String category, String date) {
        String sql = "INSERT INTO transactions (type, amount, category, date) VALUES (?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, category);
            pstmt.setString(4, date);
            pstmt.executeUpdate();
            System.out.println("Transaction added!");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Retrieve all transactions
    public static List<Transaction> getAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("id"),
                        rs.getString("type"),
                        rs.getDouble("amount"),
                        rs.getString("category"),
                        rs.getString("date")
                );
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return transactions;
    }

    // Delete a transaction by ID
    public static void deleteTransaction(int id) {
        String sql = "DELETE FROM transactions WHERE id = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Transaction deleted.");
            } else {
                System.out.println("Transaction not found.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Delete all transactions
    public static void deleteAllTransactions() {
    	String sql = "DELETE FROM transactions";
    	
    	try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);
    		 Statement stmt = conn.createStatement()) {
    		
    		int affectedRows = stmt.executeUpdate(sql);
    		if (affectedRows >= 0) {
    			System.out.println("All transactions deleted.");
    		}
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
    }
}