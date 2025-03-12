package view;

import database.TransactionDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Transaction;

import java.io.IOException;
import java.util.List;

public class MainViewController {

    @FXML private Label incomeTotalLabel;
    @FXML private Label expenseTotalLabel;
    @FXML private Label balanceLabel;
    
    @FXML private TableView<Transaction> recentTransactionsTable;
    @FXML private TableColumn<Transaction, String> dateColumn;
    @FXML private TableColumn<Transaction, String> typeColumn;
    @FXML private TableColumn<Transaction, String> categoryColumn;
    @FXML private TableColumn<Transaction, Double> amountColumn;
    
    @FXML private TableView<Transaction> transactionsTable;
    @FXML private TableColumn<Transaction, Integer> idColumn;
    @FXML private TableColumn<Transaction, String> transDateColumn;
    @FXML private TableColumn<Transaction, String> transTypeColumn;
    @FXML private TableColumn<Transaction, String> transCategoryColumn;
    @FXML private TableColumn<Transaction, Double> transAmountColumn;
    
    @FXML private TextField searchField;
    
    private ObservableList<Transaction> transactionsList = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        // Set up table columns
        setupTableColumns();
        
        // Load transaction data
        loadTransactionData();
        
        // Calculate and display summaries
        updateSummary();
    }
    
    private void setupTableColumns() {
        // Dashboard recent transactions table
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        amountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        // Format amount column to show currency
        amountColumn.setCellFactory(column -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });
        
        // Transactions tab table
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        transDateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        transTypeColumn.setCellValueFactory(new PropertyValueFactory<>("type"));
        transCategoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        transAmountColumn.setCellValueFactory(new PropertyValueFactory<>("amount"));
        
        // Format amount column to show currency
        transAmountColumn.setCellFactory(column -> new TableCell<Transaction, Double>() {
            @Override
            protected void updateItem(Double amount, boolean empty) {
                super.updateItem(amount, empty);
                if (empty || amount == null) {
                    setText(null);
                } else {
                    setText(String.format("$%.2f", amount));
                }
            }
        });
    }
    
    private void loadTransactionData() {
        List<Transaction> transactions = TransactionDAO.getAllTransactions();
        transactionsList.setAll(transactions);
        
        transactionsTable.setItems(transactionsList);
        
        // Show only recent transactions in dashboard (e.g., last 5)
        ObservableList<Transaction> recentTransactions = FXCollections.observableArrayList();
        int count = Math.min(transactions.size(), 5);
        for (int i = 0; i < count; i++) {
            recentTransactions.add(transactions.get(i));
        }
        recentTransactionsTable.setItems(recentTransactions);
    }
    
    private void updateSummary() {
        double incomeTotal = 0;
        double expenseTotal = 0;
        
        for (Transaction t : transactionsList) {
            if (t.getType().equalsIgnoreCase("Income")) {
                incomeTotal += t.getAmount();
            } else if (t.getType().equalsIgnoreCase("Expense")) {
                expenseTotal += t.getAmount();
            }
        }
        
        double balance = incomeTotal - expenseTotal;
        
        incomeTotalLabel.setText(String.format("$%.2f", incomeTotal));
        expenseTotalLabel.setText(String.format("$%.2f", expenseTotal));
        balanceLabel.setText(String.format("$%.2f", balance));
    }
    
    @FXML
    private void handleAddTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/AddTransactionView.fxml"));
            Parent root = loader.load();
            
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Transaction");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(root));
            
            AddTransactionController controller = loader.getController();
            controller.setMainController(this);
            
            dialogStage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Could not load Add Transaction view.");
        }
    }
    
    @FXML
    private void handleEditTransaction() {
        Transaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showAlert(Alert.AlertType.INFORMATION, "Select Transaction", "Please select a transaction to edit.");
            return;
        }
        
        // Add code to handle editing the transaction
    }
    
    @FXML
    private void handleDeleteTransaction() {
        Transaction selectedTransaction = transactionsTable.getSelectionModel().getSelectedItem();
        if (selectedTransaction == null) {
            showAlert(Alert.AlertType.INFORMATION, "Select Transaction", "Please select a transaction to delete.");
            return;
        }
        
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Delete");
        confirmation.setHeaderText("Delete Transaction");
        confirmation.setContentText("Are you sure you want to delete this transaction?");
        
        if (confirmation.showAndWait().get() == ButtonType.OK) {
            TransactionDAO.deleteTransaction(selectedTransaction.getId());
            loadTransactionData();
            updateSummary();
        }
    }
    
    @FXML
    private void handleViewTransactions() {
        // This functionality is already covered by the Transactions tab
    }
    
    @FXML
    private void handleReports() {
        showAlert(Alert.AlertType.INFORMATION, "Coming Soon", "Reports feature is coming soon!");
    }
    
    @FXML
    private void handleBudgets() {
        showAlert(Alert.AlertType.INFORMATION, "Coming Soon", "Budgets feature is coming soon!");
    }
    
    @FXML
    private void handleSearch() {
        String searchText = searchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            loadTransactionData();
            return;
        }
        
        List<Transaction> allTransactions = TransactionDAO.getAllTransactions();
        ObservableList<Transaction> filteredList = FXCollections.observableArrayList();
        
        for (Transaction t : allTransactions) {
            if (t.getType().toLowerCase().contains(searchText) ||
                t.getCategory().toLowerCase().contains(searchText) ||
                t.getDate().toLowerCase().contains(searchText) ||
                String.valueOf(t.getAmount()).contains(searchText)) {
                filteredList.add(t);
            }
        }
        
        transactionsTable.setItems(filteredList);
    }
    
    public void refreshData() {
        loadTransactionData();
        updateSummary();
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}