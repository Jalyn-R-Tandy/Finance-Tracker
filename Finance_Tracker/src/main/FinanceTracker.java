package main;

import database.TransactionDAO;
import model.Transaction;
import java.util.List;
import java.util.Scanner;

public class FinanceTracker {
	// Prints welcome screen
	public static void welcomeScreen() {
		System.out.println("----------------------------");
		System.out.println("FINANCE TRACKER");
		System.out.println("----------------------------");
		System.out.println("(1) Add Transaction (2) Delete Transaction (by ID) (3) Delete All Transactions (4) View Transactions (5) Exit");
		System.out.print("Enter: ");
	}
	
	// Prompts user to select transaction choice
    public static int getUserChoice() {
        Scanner scnr = new Scanner(System.in);
        int usrChoice;
        
        while (true) {
            if (scnr.hasNextInt()) {
                usrChoice = scnr.nextInt();
                if (usrChoice >= 1 && usrChoice <= 5) {
                    return usrChoice;
                } else {
                    System.out.println("Error: " + usrChoice + " is an invalid input");
                }
            } else {
                System.out.println("Please enter a valid number");
                scnr.next();
            }
        }
    }
    
    // Prints final transactions table
    public static void printTransactions() {
    	 List<Transaction> transactions = TransactionDAO.getAllTransactions();
         System.out.println("Transaction History:");
         for (Transaction t : transactions) {
             System.out.println(t);
         }
    }
    
    public static void main(String[] args) {
        Scanner scnr = new Scanner(System.in);
        
        
        /*
         * FIX THIS
         * Transaction table isn't printing at all.
         */
        while (true) {
            welcomeScreen();
            int usrChoice = getUserChoice();
            
            switch (usrChoice) {
                case 1:
                    System.out.println("Income OR Expense? ");
                    String type = scnr.next();
                    System.out.println("Amount: ");
                    int amount = scnr.nextInt();
                    System.out.println("What category? ");
                    String category = scnr.next();
                    System.out.println("Date? ");
                    String date = scnr.next();
                    
                    TransactionDAO.addTransaction(type, amount, category, date);
                    printTransactions();
                    break;
                    
                case 2:
                    System.out.println("Enter transaction ID to delete: ");
                    int id = scnr.nextInt();
                    TransactionDAO.deleteTransaction(id);
                    printTransactions();
                    break;
                    
                case 3:
                    TransactionDAO.deleteAllTransactions();
                    printTransactions();
                    break;
                    
                case 4:
                    printTransactions();
                    break;
                    
                case 5:
                    System.out.println("Thank you for using Finance Tracker!");
                    scnr.close();
                    return;
                    
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
            
            System.out.println("\n"); // Add some spacing between operations
        }
    }
}