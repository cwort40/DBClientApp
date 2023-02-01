package com.c195.dbclientapp;

import com.c195.dbclientapp.database.AppointmentAccess;
import com.c195.dbclientapp.database.ContactAccess;
import com.c195.dbclientapp.database.CustomerAccess;
import com.c195.dbclientapp.database.UserAccess;
import com.c195.dbclientapp.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ResourceBundle;
import java.util.function.Supplier;

/**
 *
 * The AddAppointmentController class is responsible for the implementation of the Add Appointment screen and its
 * associated functionality. It allows the user to input the details of a new appointment, checks that all required
 * fields are not empty, that the start time is before the end time, that the start and end times are within business
 * hours, and that the appointment does not overlap with any existing appointments for the selected customer. If all
 * of these conditions are met, it adds the appointment to the database and returns to the main calendar screen. If
 * any of these conditions are not met, it displays an error message.
 */
public class AddAppointmentController implements Initializable {

    // Initialize idTotal to be used for generating uniqueId
    private static int idTotal = 1;

    //Declare FXML Controls
    @FXML
    private ComboBox<Integer> contactIdComboBox;
    @FXML
    private ComboBox<Integer> customerIdComboBox;
    @FXML
    private TextField descriptionTxt;
    @FXML
    private ComboBox<String> endTimeComboBox;
    @FXML
    private TextField id;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private TextField locationTxt;
    @FXML
    private ComboBox<String> startTimeComboBox;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private TextField titleTxt;
    @FXML
    private TextField typeTxt;
    @FXML
    private ComboBox<Integer> userIdComboBox;

    /**
     *
     * Event handler for the Cancel button.
     * When the Cancel button is clicked, the scene is changed to the appointments scene.
     * @param event the action event that triggered the handler
     * @throws IOException if there is an error loading the appointments scene
     * @FXML
     */
    @FXML
    void OnActionCancel(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "appointments.fxml", "Appointments");
    }

    /**
     *
     * This method is called when the Add button is clicked on the Add Appointment screen. It checks that all required
     * fields are not empty, that the start time is before the end time, that the start and end times are within business
     * hours, and that the appointment does not overlap with any existing appointments for the selected customer. If all
     * of these conditions are met, it adds the appointment to the database and returns to the main calendar screen. If
     * any of these conditions are not met, it displays an error message.
     * <br>
     * LAMBDA EXPRESSIONS: <br>
     * 1. The `getNewId` variable is a `Supplier` that, when called using the `get()` method, increments the idTotal
     * variable and returns the new value. This can be more concise and readable than creating a separate method to
     * perform the same task.
     * <br>
     * 2. The lambda expression make the code more concise because it allows the logic for checking for overlapping
     * appointments to be written in a single line of code, rather than having to write a separate method or block of
     * code to do the same thing. It can also make the code easier to read because the logic is expressed in a more
     * declarative way, rather than having to follow a sequence of control flow statements.
     *
     * @param event the event that triggered this method
     * @throws SQLException if there is an error executing the SQL statements
     * @throws IOException if there is an error loading the main calendar screen
     * @FXML
     */
    @FXML
    void OnActionAdd(ActionEvent event) throws SQLException, IOException {

        // Declare variables used in checking errors
        boolean inputError = false;
        String errorMessage = "";

        // Check that all required fields are not empty
        if (titleTxt.getText().trim().isEmpty()) {
            inputError = true;
            errorMessage += "Title field must be completed.\n";
        }
        if (descriptionTxt.getText().trim().isEmpty()) {
            inputError = true;
            errorMessage += "Description field must be completed.\n";
        }
        if (locationTxt.getText().trim().isEmpty()) {
            inputError = true;
            errorMessage += "Location field must be completed.\n";
        }
        if (typeTxt.getText().trim().isEmpty()) {
            inputError = true;
            errorMessage += "Type field must be completed.\n";
        }
        if (startTimeComboBox.getSelectionModel().isEmpty()) {
            inputError = true;
            errorMessage += "Start time must be selected.\n";
        }
        if (endTimeComboBox.getSelectionModel().isEmpty()) {
            inputError = true;
            errorMessage += "End time must be selected.\n";
        }
        if (startDatePicker.getValue() == null) {
            inputError = true;
            errorMessage += "Start date must be selected.\n";
        }
        if (endDatePicker.getValue() == null) {
            inputError = true;
            errorMessage += "End date must be selected.\n";
        }
        if (customerIdComboBox.getSelectionModel().isEmpty()) {
            inputError = true;
            errorMessage += "Customer ID must be selected.\n";
        }
        if (contactIdComboBox.getSelectionModel().isEmpty()) {
            inputError = true;
            errorMessage += "Contact ID must be selected.\n";
        }
        if (userIdComboBox.getSelectionModel().isEmpty()) {
            inputError = true;
            errorMessage += "User ID must be selected.\n";
        }

        // Display error(s)
        if (inputError) {
            DialogBox.displayAlert("Error", errorMessage);
            return;
        }

        // Check that start time is before end time
        int startHour = Integer.parseInt(startTimeComboBox.getSelectionModel().getSelectedItem());
        int endHour = Integer.parseInt(endTimeComboBox.getSelectionModel().getSelectedItem());
        if (startHour >= endHour) {
            DialogBox.displayAlert("Error", "Start time must be before end time");
            return;
        }

        // Lambda expression to generate unique id
        Supplier<Integer> getNewId = () -> idTotal++;
        int uniqueId = getNewId.get();
        id.setText(String.valueOf(uniqueId));

        // Taking in all string text fields
        String title = titleTxt.getText();
        String description = descriptionTxt.getText();
        String location = locationTxt.getText();
        String type = typeTxt.getText();

        // Get hours
        startHour = Integer.parseInt(startTimeComboBox.getSelectionModel().getSelectedItem());
        endHour = Integer.parseInt(endTimeComboBox.getSelectionModel().getSelectedItem());

        // Get local dates
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Combine dates & hours
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(startHour, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(endHour, 0));

        // Convert start and end times to UTC and keep them as LocalDateTime objects
        LocalDateTime startUTC = start.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
        LocalDateTime endUTC = end.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();

        // Define the business hours at 8:00 a.m. to 10:00 p.m. EST
        int startHourCheck = 8;
        int endHourCheck = 22;

        // Check if the start time is outside the business hours
        if (startUTC.getHour() < startHourCheck || startUTC.getHour() >= endHourCheck) {
            // Display an error message
            DialogBox.displayAlert("Error",
                    "Start time must be within business hours (8:00 a.m. to 10:00 p.m. EST)");
            return;
        }

        // Check if the end time is outside the business hours
        if (endUTC.getHour() < startHourCheck || endUTC.getHour() > endHourCheck) {
            // Display an error message
            DialogBox.displayAlert("Error",
                    "End time must be within business hours (8:00 a.m. to 10:00 p.m. EST)");
            return;
        }

        // Select customer id
        int customer = customerIdComboBox.getSelectionModel().getSelectedItem();

        // Select contact id
        int contact = contactIdComboBox.getSelectionModel().getSelectedItem();

        // Select user id
        int user = userIdComboBox.getSelectionModel().getSelectedItem();

        // Set create date
        LocalDateTime createDate = LocalDateTime.now().atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();

        // Set user created by
        String createdBy = LoginController.currentUser;

        // Set last update
        LocalDateTime lastUpdate = LocalDateTime.now().atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();

        // Set last updated by
        String lastUpdatedBy = LoginController.currentUser;

        // Check for overlapping appointments for the customer
        ObservableList<Appointment> customerAppointments = FXCollections
                .observableArrayList(AppointmentAccess.getAppointmentsByCustomerId(customer));

        // Lambda expression for checking for overlapping appointments
        boolean hasOverlap = customerAppointments.stream()
                .anyMatch(appointment ->
                        (start.isAfter(appointment.getStart()) && start.isBefore(appointment.getEnd())) ||
                        (end.isAfter(appointment.getStart()) && end.isBefore(appointment.getEnd())) ||
                        (start.isEqual(appointment.getStart()) && end.isEqual(appointment.getEnd())));
        if (hasOverlap) {
            DialogBox.displayAlert("Error",
                    "An appointment already exists at this time for this customer.");
            return;
        }

        // Create newAppointment object
        Appointment newAppointment = new Appointment(
                uniqueId,
                title,
                description,
                location,
                type,
                startUTC,
                endUTC,
                createDate,
                createdBy,
                lastUpdate,
                lastUpdatedBy,
                customer,
                user,
                contact
        );

        // Add appointment to database
        if (!AppointmentAccess.addAppointment(newAppointment)) {
            DialogBox.displayAlert("Error", "Failed to add appointment to database");
            return;
        }

        // Load appointment screen
        LoadSceneHelper.loadScene(event, "appointments.fxml", "Appointments");
    }

    /**
     *
     * Initializes the form with default values and populates the combo boxes with values from the database.
     * @param url the URL of the form
     * @param resourceBundle the resource bundle associated with the form
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Populate the observable lists with the desired values
        ObservableList<Integer> customerIdList;
        ObservableList<Integer> contactIdList;
        ObservableList<Integer> userIdList;
        try {
            customerIdList = FXCollections.observableArrayList(CustomerAccess.getAllCustomerIds());
            contactIdList = FXCollections.observableArrayList(ContactAccess.getAllContactIds());
            userIdList = FXCollections.observableArrayList(UserAccess.getAllUserIds());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Set the combo boxes' items after the combo boxes have been added to the scene
        contactIdComboBox.setItems(contactIdList);
        customerIdComboBox.setItems(customerIdList);
        userIdComboBox.setItems(userIdList);

        // Create a list of hours
        ObservableList<String> hours = FXCollections.observableArrayList();
        for (int i = 0; i <= 23; i++) {
            String hour = String.format("%02d", i);
            hours.add(hour);
        }

        // Populate both combo boxes with the list of hours
        startTimeComboBox.setItems(hours);
        endTimeComboBox.setItems(hours);
    }
}
