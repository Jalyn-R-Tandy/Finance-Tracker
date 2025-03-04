package main;

import database.TransactionDAO;
import model.Transaction;
import java.util.List;

public class FinanceTracker {
    public static void main(String[] args) {
        // Add test transactions
//        TransactionDAO.addTransaction("Income", 500.00, "Salary", "2025-03-03");
//        TransactionDAO.addTransaction("Expense", 20.00, "Food", "2025-03-03");

        // Retrieve all transactions
        List<Transaction> transactions = TransactionDAO.getAllTransactions();
        System.out.println("Transaction History:");
        for (Transaction t : transactions) {
            System.out.println(t);
        }

        // Delete a transaction
        TransactionDAO.deleteAllTransactions();
    }
}