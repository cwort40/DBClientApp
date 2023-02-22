package com.c195.dbclientapp;

import com.c195.dbclientapp.database.AppointmentAccess;
import com.c195.dbclientapp.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import static com.c195.dbclientapp.DialogBox.displayAlert;
import static com.c195.dbclientapp.DialogBox.displayConfirmation;


/**
 *
 * The AppointmentsController class is the controller for the appointments scene. It controls the actions that can be
 * taken in the scene, such as adding, updating, and deleting appointments, as well as returning to the main menu. It
 * also handles populating the table view with the appointments from the database.
 * */
public class AppointmentsController implements Initializable {

    // Declare all the FXML Controls
    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, Integer> contactCol;
    @FXML
    private TableColumn<Appointment, Integer> customerCol;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> endCol;
    @FXML
    private TableColumn<Appointment, Integer> idCol;
    @FXML
    private TableColumn<Appointment, String> locationCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> startCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<Appointment, Integer> userCol;
    @FXML
    private RadioButton allRadioButton;
    @FXML
    private RadioButton monthRadioButton;
    @FXML
    private RadioButton weekRadioButton;
    @FXML
    private ToggleGroup toggle;

    /**
     *
     * Event handler for the Add button. When the Add button is clicked, the scene is changed to the Add Appointment
     * scene.
     * @param event the action event that triggered the handler
     * @throws IOException if there is an error loading the main menu scene
     */
    @FXML
    void OnActionAdd(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "addAppointment.fxml", "Add Appointment");
    }

    /**
     *
     * Event handler for the Return button. When the Return button is clicked, the scene is changed to the main menu
     * scene.
     * @param event the action event that triggered the handler
     * @throws IOException if there is an error loading the main menu scene
     */
    @FXML
    void OnActionReturn(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "mainMenu.fxml", "Main Menu");
    }

    /**
     *
     * Event handler for the Update button. When the Update button is clicked, this method sends the selected appointment
     * to the UpdateAppointmentController and loads the update appointment scene. If no appointment is selected, it does
     * nothing.
     * @param event the action event that triggered the handler
     * @throws IOException if there is an error loading the update appointment scene
     */
    @FXML
    void OnActionUpdate(ActionEvent event) throws java.io.IOException {
        // Load update controller if an appointment is selected
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment != null) {
            // Send selected part to UpdateAppointmentController
            FXMLLoader loader = new FXMLLoader(getClass().getResource("updateAppointment.fxml"));
            Parent root = loader.load();
            UpdateAppointmentController UAppointmentController = loader.getController();
            UAppointmentController.sendAppointment(selectedAppointment);

            // Set the scene for the update appointment dialog
            Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            stage.setTitle("Update Appointment");
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    /**
     *
     * Event handler for the Cancel button. When the Cancel button is clicked, this method checks if an appointment is
     * selected and, if so, confirms the cancellation with the user. If the user confirms, the selected appointment is
     * deleted from the database and removed from the table view. If no appointment is selected, an error message is
     * displayed.
     * @param event the event that triggered this method
     * @throws IOException if there is an error loading a scene
     */
    @FXML
    void OnActionCancel(ActionEvent event) throws java.io.IOException {
        Appointment selectedAppointment = appointmentTableView.getSelectionModel().getSelectedItem();
        if (selectedAppointment == null) {
            displayAlert("Error", "No appointment selected.");
            return;
        }

        boolean confirm = displayConfirmation("Cancel Appointment", "Are you sure you want to cancel the selected appointment?");
        if (!confirm) {
            return;
        }

        try {
            AppointmentAccess.deleteAppointment(selectedAppointment.getAppointmentId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        appointmentTableView.getItems().remove(selectedAppointment);
        DialogBox.displayAlert("Confirmation", selectedAppointment.getTitle() + " of type " + selectedAppointment.getType() + " has been cancelled.");
    }

    /**
     *
     * Event handler for the 'All' radio button.
     * When the 'All' radio button is selected, this method retrieves all appointments from the database
     * and displays them in the table view.
     * @param event the action event that triggered the handler
     * @throws SQLException if there is an error executing the SQL statements
     */
    @FXML
    void allRadioButton(ActionEvent event) throws SQLException {
        // Loads table view with all appointments
        if (allRadioButton.isSelected()) {
            ObservableList<Appointment> apptList = AppointmentAccess.getAllAppointments();
            appointmentTableView.setItems(apptList);

        }
    }

    /**
     *
     * Event handler for the monthRadioButton. When the monthRadioButton is selected, this method queries the database for
     * all appointments that occur within the current month and sets the items in the appointmentTableView to the returned
     * list of appointments.
     * @param event the event that triggered this method
     * @throws SQLException if there is an error executing the SQL statements
     */
    @FXML
    void monthRadioButton(ActionEvent event) throws SQLException {
        // Loads table view with desired appointments
        if (monthRadioButton.isSelected()) {
            ObservableList<Appointment> apptList = AppointmentAccess.getAllAppointmentsByMonth();
            appointmentTableView.setItems(apptList);
        }
    }

    /**
     *
     * Event handler for the weekRadioButton. When the weekRadioButton is selected, this method queries the database for
     * all appointments that occur within the current week and sets the items in the appointmentTableView to the returned
     * list of appointments.
     * @param event the event that triggered this method
     * @throws SQLException if there is an error executing the SQL statements
     */
    @FXML
    void weekRadioButton(ActionEvent event) throws SQLException {
        // Loads table view with desired appointments
        if (weekRadioButton.isSelected()) {
            ObservableList<Appointment> apptList = AppointmentAccess.getAllAppointmentsByWeek();
            appointmentTableView.setItems(apptList);
        }
    }

    /**
     *
     * Initializes the table view in the appointments scene.
     * Populates the table view with a list of all appointments and sets the value of each column with the corresponding
     * appointment property.
     * @param url the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle the resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        try {
            // Retrieve appointments from the database
            ObservableList<Appointment> appointments = AppointmentAccess.getAllAppointments();

            // Set up table columns
            idCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
            titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
            descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
            locationCol.setCellValueFactory(new PropertyValueFactory<>("location"));
            typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
            startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
            endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
            customerCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            userCol.setCellValueFactory(new PropertyValueFactory<>("userId"));
            contactCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));

            // Set table data
            appointmentTableView.setItems(FXCollections.observableList(appointments));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
