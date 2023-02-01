package com.c195.dbclientapp;

import com.c195.dbclientapp.database.AppointmentAccess;
import com.c195.dbclientapp.database.LoginAccess;
import com.c195.dbclientapp.i18n.Resources_en;
import com.c195.dbclientapp.i18n.Resources_fr;
import com.c195.dbclientapp.model.Appointment;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A controller class for the login scene in the DBClientApp application.
 * This class handles user input and interactions with the login scene,
 * including validating user credentials and displaying alerts for upcoming
 * appointments within 15 minutes of login.
 */
public class LoginController implements Initializable {

    // Declare static String for currentUser
    public static String currentUser = "";

    // Declare all the FXML controls
    @FXML
    private Button exitButton;
    @FXML
    private Label location;
    @FXML
    private Label locationLabel;
    @FXML
    private Label loginLabel;
    @FXML
    private Button submitButton;
    @FXML
    private Label userNameLabel;
    @FXML
    private TextField userNameTxt;
    @FXML
    private Label userPasswordLabel;
    @FXML
    private PasswordField userPasswordTxt;

    /**
     * Exits the application
     */
    @FXML
    void OnActionExit() {
        System.exit(0);
    }

    /**
     * Event handler for the submit button. Validates the user's login credentials and displays a notification if there
     * is an upcoming appointment within 15 minutes of login.
     * @param event the ActionEvent that triggers this method
     * @throws java.io.IOException if there is an error loading a new scene
     */
    @FXML
    void OnActionSubmit(ActionEvent event) throws java.io.IOException {

        try {
            // Take in credentials and locale
            String userName = userNameTxt.getText();
            String password = userPasswordTxt.getText();
            Locale locale = Locale.getDefault();

            // Validate the user
            int userId = LoginAccess.validateUser(userName, password);
            if (userId != -1) {

                // Log successful login
                LogActivity.logSuccessfulLogin(userName);

                // Check for appointments within 15 minutes of login
                try {
                    ObservableList<Appointment> appointments = AppointmentAccess.getAppointmentsByUserId(userId);
                    boolean foundAppointment = false;
                    for (Appointment appointment : appointments) {
                        LocalDateTime appointmentStart = appointment.getStart();
                        if (appointmentStart.isBefore(LocalDateTime.now().plusMinutes(15)) &&
                                appointmentStart.isAfter(LocalDateTime.now())) {

                            // There is an appointment within 15 minutes of login, so display an alert
                            foundAppointment = true;
                            if (locale.getLanguage().equals("fr")) {
                                Resources_fr rb = new Resources_fr();
                                DialogBox.displayAlert(rb.getString("Upcoming Appointment"),
                                        "Vous avez un rendez-vous prochain avec l'ID " + appointment
                                                .getAppointmentId() +
                                                " à " + appointmentStart.toString() + ".");
                            } else {
                                Resources_en rb = new Resources_en();
                                DialogBox.displayAlert(rb.getString("Upcoming Appointment"),
                                        "You have an upcoming appointment with ID " + appointment
                                                .getAppointmentId() +
                                                " at " + appointmentStart.toString() + ".");
                            }
                        }
                    }
                    if (!foundAppointment) {

                        // No appointments within 15 minutes of login, so display an alert
                        if (locale.getLanguage().equals("fr")) {
                            Resources_fr rb = new Resources_fr();
                            DialogBox.displayAlert(rb.getString("No Upcoming Appointments"),
                                    "Vous n'avez pas de rendez-vous prochain.");
                        } else {
                            Resources_en rb = new Resources_en();
                            DialogBox.displayAlert(rb.getString("No Upcoming Appointments"),
                                    "You have no upcoming appointments.");
                        }
                    }
                } catch (SQLException e) {

                    // An error occurred while trying to get the user's appointments, so display an alert
                    if (locale.getLanguage().equals("fr")) {
                        Resources_fr rb = new Resources_fr();
                        DialogBox.displayAlert(rb.getString("Error"),
                                "Une erreur s'est produite lors de la récupération de vos rendez-vous à venir: "
                                        + e.getMessage());
                    } else {
                        Resources_en rb = new Resources_en();
                        DialogBox.displayAlert(rb.getString("Error"),
                                "An error occurred while trying to get your upcoming appointments: "
                                        + e.getMessage());
                    }
                }

                // Load the main menu scene if the user is valid
                LoadSceneHelper.loadScene(event, "mainMenu.fxml", "Main Menu");

                //record current user
                currentUser = userName;
            } else {

                // Log failed login
                LogActivity.logFailedLogin(userName);

                // Get the appropriate Resources subclass based on the default locale
                if (locale.getLanguage().equals("fr")) {
                    Resources_fr rb = new Resources_fr();
                    DialogBox.displayAlert(rb.getString("Error"),
                            rb.getString("Invalid username or password"));
                } else {
                    Resources_en rb = new Resources_en();
                    DialogBox.displayAlert(rb.getString("Error"),
                            rb.getString("Invalid username or password"));
                }
            }
        } catch (SQLException e) {

            // Get the appropriate Resources subclass based on the default locale
            Locale locale = Locale.getDefault();
            if (locale.getLanguage().equals("fr")) {
                Resources_fr rb = new Resources_fr();
                DialogBox.displayAlert(rb.getString("Error"),
                        "An error occurred while trying to validate the user: " + e.getMessage());
            } else {
                Resources_en rb = new Resources_en();
                DialogBox.displayAlert(rb.getString("Error"),
                        "An error occurred while trying to validate the user: " + e.getMessage());
            }
        }
    }

    /**
     * Initializes the LoginController and sets the location label to the current location.
     * @param url the location of the current user
     * @param resourceBundle the resources to be used for localization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {

            // Set the default locale and time zone
            Locale locale = Locale.getDefault();
            ZoneId zone = ZoneId.systemDefault();

            // Set the location field to the system default time zone
            location.setText(String.valueOf(zone));

            // Create a new instance of the appropriate Resources subclass
            if (locale.getLanguage().equals("fr")) {
                Resources_fr rb = new Resources_fr();
                loginLabel.setText(rb.getString("Login"));
                userNameLabel.setText(rb.getString("Username"));
                userPasswordLabel.setText(rb.getString("Password"));
                submitButton.setText(rb.getString("Submit"));
                exitButton.setText(rb.getString("Exit"));
                locationLabel.setText(rb.getString("Location"));
            } else {
                Resources_en rb = new Resources_en();
                loginLabel.setText(rb.getString("Login"));
                userNameLabel.setText(rb.getString("Username"));
                userPasswordLabel.setText(rb.getString("Password"));
                submitButton.setText(rb.getString("Submit"));
                exitButton.setText(rb.getString("Exit"));
                locationLabel.setText(rb.getString("Location"));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}





