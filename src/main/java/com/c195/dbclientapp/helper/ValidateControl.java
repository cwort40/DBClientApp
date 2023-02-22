package com.c195.dbclientapp.helper;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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

    public void validateControl(Control control, String message) {
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

    public void validateTimeSelection(String startTime, String endTime) {
        try {
            int startHour = Integer.parseInt(startTime);
            int endHour = Integer.parseInt(endTime);
            if (startHour >= endHour) {
                setInputError(true);
                addErrorMessage("Start time must be before end time");
            }
        } catch (NumberFormatException e) {
            setInputError(true);
            addErrorMessage("Invalid time selected");
        }
    }

    public void validateBusinessHours(String startTime, String endTime, LocalDate startDate, LocalDate endDate) {
        // check if any of the arguments are null
        if (startTime == null || endTime == null || startDate == null || endDate == null) {
            setInputError(true);
            addErrorMessage("All fields are required");
            return;
        }

        // get the selected start and end hours as integers
        int startHour = Integer.parseInt(startTime);
        int endHour = Integer.parseInt(endTime);

        // combine dates and hours to create local date times
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(startHour, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(endHour, 0));

        // convert start and end times to UTC and keep them as LocalDateTime objects
        LocalDateTime startUTC = start.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
        LocalDateTime endUTC = end.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();

        // Define the business hours at 8:00 a.m. to 10:00 p.m. EST
        int startHourCheck = 8;
        int endHourCheck = 22;

        // Check if the start time is outside the business hours
        if (startUTC.getHour() < startHourCheck || startUTC.getHour() >= endHourCheck) {
            // add an error message to the list
            setInputError(true);
            addErrorMessage("Start time must be within business hours (8:00 a.m. to 10:00 p.m. EST)");
        }

        // Check if the end time is outside the business hours
        if (endUTC.getHour() < startHourCheck || endUTC.getHour() > endHourCheck) {
            // add an error message to the list
            setInputError(true);
            addErrorMessage("End time must be within business hours (8:00 a.m. to 10:00 p.m. EST)");
        }
    }


}

