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
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.*;
import java.util.Objects;
import java.util.ResourceBundle;


/**
 * The UpdateAppointmentController class is the controller class for the Update Appointment window. It provides
 * functionality for updating an existing appointment in the database.
 */
public class UpdateAppointmentController implements Initializable {

    // Initialize an appointment object for use between methods
    Appointment selectedAppointment = null;

    // Declare all the FXML controls
    @FXML
    private Label createDateLabel;
    @FXML
    private Label createdByLabel;
    @FXML
    private Label lastUpdateDateLabel;
    @FXML
    private Label lastUpdatedByLabel;
    @FXML
    private ComboBox<Integer> contactIdComboBox;
    @FXML
    private ComboBox<Integer> customerIdComboBox;
    @FXML
    private TextField descriptionTxt;
    @FXML
    private DatePicker endDatePicker;
    @FXML
    private ComboBox<String> endTimeComboBox;
    @FXML
    private TextField id;
    @FXML
    private TextField locationTxt;
    @FXML
    private DatePicker startDatePicker;
    @FXML
    private ComboBox<String> startTimeComboBox;
    @FXML
    private TextField titleTxt;
    @FXML
    private TextField typeTxt;
    @FXML
    private ComboBox<Integer> userIdComboBox;

    /**
     * Event handler for the cancel button. Closes the current window and opens the appointments window.
     * @param event the action event triggered by the cancel button
     * @throws IOException if the appointments window cannot be opened
     */
    @FXML
    void OnActionCancel(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "appointments.fxml", "Appointments");
    }

    /**
     * This method is called when the "Update" button is pressed on the Update Appointment form.
     * It updates the selected appointment with the new data entered in the form. If any required fields are empty
     * or if the start time is later than the end time, an error message is displayed. If the update is successful,
     * the user is taken back to the Appointments screen.
     * <br>
     * LAMBDA EXPRESSION:
     * <br>
     * The lambda expression make the code more concise because it allows the logic for checking for overlapping
     * appointments to be written in a single line of code, rather than having to write a separate method or block of
     * code to do the same thing. It can also make the code easier to read because the logic is expressed in a more
     * declarative way, rather than having to follow a sequence of control flow statements.
     * @param event The event that triggers this method.
     * @throws SQLException If there is an issue with the database connection.
     * @throws IOException If there is an issue loading the Appointments screen.
     */
    @FXML
    void OnActionUpdate(ActionEvent event) throws SQLException, IOException {

        // Declare variables used for checking errors
        boolean inputError = false;
        String errorMessage = "";

        // Assign the selectedAppointment object
        Appointment selectedAppointment = this.selectedAppointment;

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

        // Print error message(s) if inputError is true
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

        // Take in uniqueId
        int uniqueId = Integer.parseInt(id.getText());

        //taking in all string text fields
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

        //select customer id
        int customer = customerIdComboBox.getSelectionModel().getSelectedItem();

        //select contact id
        int contact = contactIdComboBox.getSelectionModel().getSelectedItem();

        //select user id
        int user = userIdComboBox.getSelectionModel().getSelectedItem();

        // Get create date
        LocalDateTime createDate = selectedAppointment.getCreateDate();

        // Get user created by
        String createdBy = selectedAppointment.getCreatedBy();

        // Convert create date to UTC and keep it as a LocalDateTime object
        LocalDateTime createDateUTC = createDate.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();

        // Set last update
        LocalDateTime lastUpdate = LocalDateTime.now();

        // Convert last update to UTC and keep it as a LocalDateTime object
        LocalDateTime lastUpdateUTC = lastUpdate.atZone(ZoneId.systemDefault())
                .withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();

        // Set last updated by
        String lastUpdatedBy = LoginController.currentUser;

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

        // Create new appointment object
        Appointment updatedAppointment = new Appointment(
                uniqueId,
                title,
                description,
                location,
                type,
                startUTC,
                endUTC,
                createDateUTC,
                createdBy,
                lastUpdateUTC,
                lastUpdatedBy,
                customer,
                user,
                contact
        );

        //update appointment to database
        if (!AppointmentAccess.updateAppointment(updatedAppointment)) {
            DialogBox.displayAlert("Error", "Failed to update appointment in database");
            return;
        }

        // Load appointments screen
        LoadSceneHelper.loadScene(event, "appointments.fxml", "Appointments");

        // Show success message
        DialogBox.displayAlert("Success", "Appointment updated successfully");
    }

    /**
     * This method is called when the UpdateAppointmentController is initialized and a specific appointment object is
     * passed to it. It sets the value of all the controls in the Update Appointment screen to the values of the passed
     * appointment object. It also converts the UTC create date and last update to the user's local time zone and
     * displays them in the appropriate labels.
     * @param selectedAppointment the appointment object whose values will be used to populate the controls in the
     * Update Appointment screen
     */
    public void sendAppointment(Appointment selectedAppointment) {

        // Assign the selectedAppointment object
        this.selectedAppointment = selectedAppointment;

        // Populate fields
        if (selectedAppointment != null) {
            id.setText(String.valueOf(selectedAppointment.getAppointmentId()));
            titleTxt.setText(selectedAppointment.getTitle());
            descriptionTxt.setText(selectedAppointment.getDescription());
            locationTxt.setText(selectedAppointment.getLocation());
            typeTxt.setText(selectedAppointment.getType());
            customerIdComboBox.setValue(selectedAppointment.getCustomerId());
            startDatePicker.setValue(selectedAppointment.getStart().toLocalDate());
            startTimeComboBox.setValue(String.valueOf(selectedAppointment.getStart().getHour()));
            endDatePicker.setValue(selectedAppointment.getStart().toLocalDate());
            endTimeComboBox.setValue(String.valueOf(selectedAppointment.getEnd().getHour()));
            contactIdComboBox.setValue(selectedAppointment.getContactId());
            userIdComboBox.setValue(selectedAppointment.getUserId());

            // Get the user's local time zone
            ZoneId localTimeZone = ZoneId.systemDefault();

            // Convert the UTC create date and last update to the user's local time zone
            LocalDateTime localCreateDate = selectedAppointment.getCreateDate().atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(localTimeZone)
                    .toLocalDateTime();
            LocalDateTime localLastUpdate = selectedAppointment.getLastUpdate().atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(localTimeZone)
                    .toLocalDateTime();

            // Set labels
            createdByLabel.setText(selectedAppointment.getCreatedBy());
            createDateLabel.setText(String.valueOf(localCreateDate));

            // If appointment has not yet been edited, label is marked with N/A
            if (Objects.equals(selectedAppointment.getLastUpdatedBy(), "-1")) {
                lastUpdatedByLabel.setText("N/A");
            } else {
                lastUpdatedByLabel.setText(selectedAppointment.getLastUpdatedBy());
            }
            boolean b = Objects.equals(selectedAppointment.getLastUpdate(), LocalDate.of(1970, Month.JANUARY,
                    1).atStartOfDay());
            if (b) {
                lastUpdateDateLabel.setText("N/A");
            } else {
                lastUpdateDateLabel.setText(String.valueOf(localLastUpdate));
            }
        }
    }

    /**
     * Initializes the UpdateAppointmentController by setting the appointment object and populating the
     * fields with the selected appointment's information.
     * @param url the URL of the FXML file
     * @param resourceBundle the resource bundle for localization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Populate the observable lists with the desired values
        ObservableList<Integer> customerIdList = null;
        ObservableList<Integer> contactIdList = null;
        ObservableList<Integer> userIdList = null;
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
