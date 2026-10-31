/**
 * Title: Magazine Service JavaFX Application Entry Point
 * Author: Chang Yun Shiang
 * Student ID: 35702674
 * Date: October 2026
 * File Name: Main.java
 * Purpose: Launches the JavaFX GUI application for the Magazine Service, 
 *          loading the main dashboard view, controller, and sample demo data.
 * 
 * @author Chang Yun Shiang
 * @version 2.0
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            // Load the FXML view file (Ensure your FXML file is named MainDashboardView.fxml in the same package)
            FXMLLoader loader = new FXMLLoader(getClass().getResource("MainDashboardView.fxml"));
            Parent root = loader.load();

            // Retrieve controller instance and pass dependencies
            MainDashboardController controller = loader.getController();
            controller.setStage(primaryStage);

            // Create and pre-populate a demonstration Magazine service for testing & grading
            Magazine magazine = new Magazine(10.00); // $10.00 base weekly cost
            setupDemoData(magazine);
            controller.setMagazine(magazine);

            // Configure and display the primary application window
            primaryStage.setTitle("Magazine Service Management System - ICT373 Assignment 2");
            primaryStage.setScene(new Scene(root, 950, 700));
            primaryStage.setResizable(true);
            primaryStage.show();

        } catch (Exception e) {
            System.err.println("[ERROR] Failed to initialize JavaFX application window: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Sets up pre-populated demonstration data to satisfy the assignment requirement 
     * for a demonstration Magazine service and User Guide walkthrough.
     * 
     * @param magazine the target Magazine instance to populate
     */
    private void setupDemoData(Magazine magazine) {
        // 1. Add Default Supplements
        Supplement tech = new Supplement("Tech & Gadgets", 4.50);
        Supplement health = new Supplement("Health & Fitness", 3.00);
        Supplement gaming = new Supplement("Gaming Monthly", 5.00);
        Supplement cooking = new Supplement("Gourmet Cooking", 2.50);

        magazine.addSupplement(tech);
        magazine.addSupplement(health);
        magazine.addSupplement(gaming);
        magazine.addSupplement(cooking);

        // 2. Add Default Addresses, Payment Methods & Customers
        Address addr1 = new Address("123 Main St", "Perth", "WA", "6000");
        PaymentMethod card1 = new CreditCard("Alice Smith", "4111222233334444", "12/28");
        PayingCustomer alice = new PayingCustomer("Alice Smith", "alice@example.com", addr1, card1);
        alice.addSupplement(tech);
        alice.addSupplement(health);

        Address addr2 = new Address("456 High St", "Fremantle", "WA", "6160");
        PaymentMethod bank1 = new BankAccount("Charlie Brown", "123-456", "98765432");
        PayingCustomer charlie = new PayingCustomer("Charlie Brown", "charlie@example.com", addr2, bank1);
        charlie.addSupplement(gaming);

        Address addr3 = new Address("789 Subiaco Rd", "Subiaco", "WA", "6008");
        AssociateCustomer bob = new AssociateCustomer("Bob Smith", "bob@example.com", addr3, alice);
        bob.addSupplement(gaming);
        alice.addAssociateCustomer(bob);

        magazine.addCustomer(alice);
        magazine.addCustomer(bob);
        magazine.addCustomer(charlie);
    }

    /**
     * Standard Java application entry point.
     * 
     * @command-line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}