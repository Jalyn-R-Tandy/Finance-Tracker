package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FinanceTrackerApp extends Application {
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the main view
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainView.fxml"));
        
        // Set up the scene
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/view/styles.css").toExternalForm());
        
        // Configure the stage
        primaryStage.setTitle("Finance Tracker");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(500);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}