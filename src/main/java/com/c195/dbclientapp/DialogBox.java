package com.c195.dbclientapp;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import static javafx.scene.control.Alert.AlertType.CONFIRMATION;

/**
 * The DialogBox class is a utility class that contains static methods for displaying alert and confirmation dialogs
 * to the user.
 */
public class DialogBox {

    /**
     *
     * Displays an alert dialog with the given title and message.
     * @param title the title of the alert
     * @param message the message to display in the alert
     */
    public static void displayAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     *
     * Displays a confirmation dialog with the given title and message. Returns true if the user clicks the "Yes"
     * button, false otherwise.
     * @param title the title of the confirmation dialog
     * @param message the message to display in the confirmation dialog
     * @return true if the user clicks the "Yes" button, false otherwise
     */
    public static boolean displayConfirmation(String title, String message) {
        Alert alert = new Alert(CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        ButtonType yesButton = new ButtonType("Yes");
        ButtonType noButton = new ButtonType("No");
        alert.getButtonTypes().setAll(yesButton, noButton);

        return alert.showAndWait().filter(buttonType -> buttonType == yesButton).isPresent();
    }
}
