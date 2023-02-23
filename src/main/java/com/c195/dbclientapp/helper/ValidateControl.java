package com.c195.dbclientapp.helper;

import com.c195.dbclientapp.DialogBox;
import com.c195.dbclientapp.database.AppointmentAccess;
import com.c195.dbclientapp.database.CustomerAccess;
import com.c195.dbclientapp.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.sql.SQLException;
import java.time.*;
import java.util.ArrayList;
import java.util.List;

public class ValidateControl {

    private boolean inputError = false;

    private List<String> errorMessages = new ArrayList<>();

    public ValidateControl() {}

    public void setInputError(boolean inputError) {
        this.inputError = inputError;
    }

    public void addErrorMessage(String errorMessage) {
        errorMessages.add(errorMessage);
    }

    public void clearErrorMessage() {
        errorMessages.clear();
    }

    public boolean isInputError() {
        return inputError;
    }

    public List<String> getErrorMessages() {
        return errorMessages;
    }

    public void validateNotEmpty(Control control, String message) {
        if (control instanceof TextField) {
            String text = ((TextField) control).getText().trim();
            if (text.isEmpty()) {
                setInputError(true);
                addErrorMessage(message);
            }
        } else if (control instanceof ComboBox) {
            if (((ComboBox<?>) control).getSelectionModel().isEmpty()) {
                setInputError(true);
                addErrorMessage(message);
            }
        } else if (control instanceof DatePicker) {
            if (((DatePicker) control).getValue() == null) {
                setInputError(true);
                addErrorMessage(message);
            }
        }
    }

    public void validateTimeSelection(LocalDateTime start, LocalDateTime end) {
        // Check if any of the arguments are null
        if (start == null || end == null) {
            setInputError(true);
            addErrorMessage("All fields are required.");
            return;
        }

        if (start.isAfter(end)) {
            setInputError(true);
            addErrorMessage("Start time must be before end time.");
            return;
        }

        if (start.toLocalDate().isAfter(end.toLocalDate())) {
            setInputError(true);
            addErrorMessage("Start date must be before end date.");
            return;
        }
    }


    public void validateBusinessHours(LocalDateTime startUTC, LocalDateTime endUTC) {
        // Check if any of the arguments are null
        if (startUTC == null || endUTC == null) {
            setInputError(true);
            addErrorMessage("All fields are required.");
            return;
        }

        // Define the business hours at 8:00 a.m. to 10:00 p.m. EST
        int startHourCheck = 8;
        int endHourCheck = 22;

        // Create a ZoneId object for Eastern Standard Time (EST)
        ZoneId estZoneId = ZoneId.of("America/New_York");

        // Convert the start and end times to EST
        ZonedDateTime startEST = startUTC.atZone(estZoneId);
        ZonedDateTime endEST = endUTC.atZone(estZoneId);

        // Check if the start time is outside the business hours
        if (startEST.getHour() < startHourCheck || startEST.getHour() >= endHourCheck) {
            // add an error message to the list
            setInputError(true);
            addErrorMessage("Start time must be within business hours (8:00 a.m. to 10:00 p.m. EST)");
        }

        // Check if the end time is outside the business hours
        if (endEST.getHour() < startHourCheck || endEST.getHour() > endHourCheck) {
            // add an error message to the list
            setInputError(true);
            addErrorMessage("End time must be within business hours (8:00 a.m. to 10:00 p.m. EST)");
        }
    }

    public boolean displayErrorAndReset(ValidateControl vc) {
        if (vc.isInputError()) {
            String errorMessage = String.join("\n", vc.getErrorMessages());
            DialogBox.displayAlert("Error", errorMessage);
            vc.setInputError(false);
            vc.clearErrorMessage();
            return true;
        } else {
            return false;
        }
    }

    public boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$");
    }


}

