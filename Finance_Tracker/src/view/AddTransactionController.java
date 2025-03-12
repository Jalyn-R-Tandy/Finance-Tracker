package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import database.TransactionDAO;
import java.time.LocalDate;

public class AddTransactionController {
    @FXML private ComboBox<String> typeComboBox;
    @FXML private TextField amountField;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private DatePicker datePicker;
    
    private MainViewController mainController;
    
    @FXML
    public void initialize() {
        // Initialize type options
        typeComboBox.getItems().addAll("Income", "Expense");
        
        // Initialize category options (you can customize these)
        categoryComboBox.getItems().addAll(
            "Salary", "Investments", "Food", "Transportation", 
            "Utilities", "Entertainment", "Shopping", "Other"
        );
        
        // Set default date to today
        datePicker.setValue(LocalDate.now());
    }
    
    public void setMainController(MainViewController controller) {
        this.mainController = controller;
    }
    
    @FXML
    private void handleSave() {
        if (!validateInput()) {
            return;
        }
        
        String type = typeComboBox.getValue();
        double amount = Double.parseDouble(amountField.getText());
        String category = categoryComboBox.getValue();
        String date = datePicker.getValue().toString();
        
        TransactionDAO.addTransaction(type, amount, category, date);
        
        if (mainController != null) {
            mainController.refreshData();
        }
        
        closeDialog();
    }
    
    @FXML
    private void handleCancel() {
        closeDialog();
    }
    
    private boolean validateInput() {
        String errorMessage = "";
        
        if (typeComboBox.getValue() == null) {
            errorMessage += "Please select a transaction type.\n";
        }
        
        if (amountField.getText() == null || amountField.getText().trim().isEmpty()) {
            errorMessage += "Please enter an amount.\n";
        } else {
            try {
                Double.parseDouble(amountField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Amount must be a valid number.\n";
            }
        }
        
        if (categoryComboBox.getValue() == null || categoryComboBox.getValue().trim().isEmpty()) {
            errorMessage += "Please select or enter a category.\n";
        }
        
        if (datePicker.getValue() == null) {
            errorMessage += "Please select a date.\n";
        }
        
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Input");
            alert.setHeaderText(null);
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
    
    private void closeDialog() {
        Stage stage = (Stage) typeComboBox.getScene().getWindow();
        stage.close();
    }
} 