package com.c195.dbclientapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The MainMenuController class is the controller for the main menu screen. It contains event handlers for the buttons
 * on the main menu screen that allow the user to navigate to the appointments, customers, or reports screens.
 */
public class MainMenuController {

    /**
     * The OnActionExit event handler is triggered when the user clicks the "Exit" button on the main menu screen. It
     * closes the application.
     * @param event the action event that triggered this handler
     */
    @FXML
    void OnActionExit(ActionEvent event) {
        System.exit(0);
    }

    /**
     * The OnActionAppointments event handler is triggered when the user clicks the "Appointments" button on the main
     * menu screen. It loads the appointments screen.
     * @param event the action event that triggered this handler
     * @throws IOException if there is an error loading the appointments screen
     */
    @FXML
    void OnActionAppointments(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "appointments.fxml", "Appointments");
    }

    /**
     * The OnActionCustomers event handler is triggered when the user clicks the "Customers" button on the main menu
     * screen. It loads the customers screen.
     * @param event the action event that triggered this handler
     * @throws IOException if there is an error loading the customers screen
     */
    @FXML
    void OnActionCustomers(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "customers.fxml", "Customers");
    }

    /**
     * The OnActionReports event handler is triggered when the user clicks the "Reports" button on the main menu screen.
     * It loads the reports screen.
     * @param event the action event that triggered this handler
     * @throws IOException if there is an error loading the reports screen
     */
    @FXML
    void OnActionReports(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "reports.fxml", "Reports");
    }

}