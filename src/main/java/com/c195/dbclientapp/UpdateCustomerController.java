package com.c195.dbclientapp;

import com.c195.dbclientapp.database.CountryAccess;
import com.c195.dbclientapp.database.CustomerAccess;
import com.c195.dbclientapp.database.FirstLevelDivisionAccess;
import com.c195.dbclientapp.model.Country;
import com.c195.dbclientapp.model.Customer;
import com.c195.dbclientapp.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.function.Function;

/**
 * The UpdateCustomerController class is responsible for handling the logic for updating customer
 * information in the database. It provides a GUI for the user to input the updated information and
 * sends the data to the CustomerAccess class to update the database.
 */
public class UpdateCustomerController implements Initializable {

    // Initializing class members
    ObservableList<FirstLevelDivision> divisionList = null;
    ObservableList<Country> countryList = null;
    Customer selectedCustomer = null;

    // Declaring FXML Controls
    @FXML
    private Label createDateLabel;

    @FXML
    private Label createdByLabel;

    @FXML
    private Label lastUpdateDateLabel;

    @FXML
    private Label lastUpdatedByLabel;

    @FXML
    private TextField addressTxt;

    @FXML
    private ComboBox<String> countryComboBox;

    @FXML
    private ComboBox<String> divisionComboBox;

    @FXML
    private TextField idTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField phoneTxt;

    @FXML
    private TextField postalCodeTxt;

    /**
     * Returns the user to the customers screen
     * @param event
     * @throws java.io.IOException
     */
    @FXML
    void OnActionCancel(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "customers.fxml", "Customers");
    }

    /**
     * The OnActionUpdate method is called when the user clicks the Update button in the Update Customer screen.
     * This method updates the selected customer in the database with the new data entered by the user. If any required
     * field is empty, or if the phone number is not in the correct format, an error message is displayed and the update
     * is not performed. If the update is successful, the user is returned to the Customers screen and a success message
     * is displayed. If the update fails, an error message is displayed. <br>
     * LAMBDA EXPRESSION:
     * <br>
     * The lambda expression is used to define a function that takes in a string and returns a boolean value.
     * This function checks if the input string is in the correct phone number format by using the matches method of the
     * String class and a regular expression to check if the string matches the pattern "^\\d{3}-\\d{3}-\\d{4}$". Using
     * a lambda expression in this case can be more concise and easier to read than defining a traditional method with
     * the same functionality. It also allows for the function to be defined and used in a single line of code, which
     * can be useful when the function is only used in a few places and does not need to be reused elsewhere in the
     * codebase.
     * @param event the ActionEvent that triggered the method call (clicking the Update button)
     * @throws SQLException if a database error occurs
     * @throws IOException if an I/O error occurs
     */
    @FXML
    void OnActionUpdate(ActionEvent event) throws SQLException, IOException {

        // Declare variables used for checking errors
        boolean inputError = false;
        String errorMessage = "";

        // Assign selectedCustomer object
        Customer selectedCustomer = this.selectedCustomer;

        // Check that all required fields are not empty
        if (nameTxt.getText().trim().isEmpty()) {
            inputError = true;
            errorMessage += "Name field must be completed.\n";
        }
        if (addressTxt.getText().trim().isEmpty()) {
            inputError = true;
            errorMessage += "Address field must be completed.\n";
        }
        if (postalCodeTxt.getText().trim().isEmpty()) {
            inputError = true;
            errorMessage += "Postal code field must be completed.\n";
        }
        if (phoneTxt.getText().trim().isEmpty()) {
            inputError = true;
            errorMessage += "Phone field must be completed.\n";
        }
        if (divisionComboBox.getSelectionModel().isEmpty()) {
            inputError = true;
            errorMessage += "Division must be selected.\n";
        }

        // If inputError is true, display error message
        if (inputError) {
            DialogBox.displayAlert("Error", errorMessage);
            return;
        }

        // Store text field values in variables
        int uniqueId = Integer.parseInt(idTxt.getText());
        String name = nameTxt.getText();
        String address = addressTxt.getText();
        String postalCode = postalCodeTxt.getText();
        String phone = phoneTxt.getText();

        // Lambda expression that ensures correct phone number format
        Function<String, Boolean> isPhoneNumberValid = phoneNumber ->
                phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$");
        if (!isPhoneNumberValid.apply(phone)) {
            errorMessage += "Phone must be proper format\n";
            DialogBox.displayAlert("Error", errorMessage);
            return;
        }

        // Get division ID
        int divisionId = -1;
        for (FirstLevelDivision division : divisionList) {
            if (division.getDivision().equals(divisionComboBox.getSelectionModel().getSelectedItem())) {
                divisionId = division.getDivisionId();
                break;
            }
        }

        // Set create date
        LocalDateTime createDate = selectedCustomer.getCreateDate();

        // Set user created by
        String createdBy = selectedCustomer.getCreatedBy();

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

        // Create a new Customer object
        Customer updatedCustomer = new Customer(uniqueId, name, address, postalCode, phone, createDateUTC, createdBy,
                lastUpdateUTC, lastUpdatedBy, divisionId);

        // Update the customer in the database
        boolean success = CustomerAccess.updateCustomer(updatedCustomer);

        if (success) {
            // Return to customers page
            LoadSceneHelper.loadScene(event, "customers.fxml", "Customers");

            // Show success message
            DialogBox.displayAlert("Success", "Customer updated successfully");

        } else {
            DialogBox.displayAlert("Error", "Failed to update customer");
        }
    }

    /**
     * Populates the fields of the update customer page with the details of the selected customer.
     * @param selectedCustomer the customer to be displayed
     * @param selectedDivision the division of the selected customer
     * @param selectedCountry the country of the selected customer
     * @throws SQLException if a database error occurs
     */
    public void sendCustomer(Customer selectedCustomer, FirstLevelDivision selectedDivision, Country selectedCountry)
            throws SQLException {

        // Assign selectedCustomer object
        this.selectedCustomer = selectedCustomer;

        // Set the text fields using the getter methods of the Customer object
        if (selectedCustomer != null) {
            idTxt.setText(Integer.toString(selectedCustomer.getCustomerId()));
            nameTxt.setText(selectedCustomer.getCustomerName());
            addressTxt.setText(selectedCustomer.getAddress());
            postalCodeTxt.setText(selectedCustomer.getPostalCode());
            phoneTxt.setText(selectedCustomer.getPhone());
            divisionComboBox.getSelectionModel().select(selectedDivision.getDivision());
            countryComboBox.getSelectionModel().select(selectedCountry.getCountry());

            // Get the user's local time zone
            ZoneId localTimeZone = ZoneId.systemDefault();

            // Convert the UTC create date and last update to the user's local time zone
            LocalDateTime localCreateDate = selectedCustomer.getCreateDate().atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(localTimeZone)
                    .toLocalDateTime();
            LocalDateTime localLastUpdate = selectedCustomer.getLastUpdate().atZone(ZoneId.of("UTC"))
                    .withZoneSameInstant(localTimeZone)
                    .toLocalDateTime();

            // Set labels
            createdByLabel.setText(selectedCustomer.getCreatedBy());
            createDateLabel.setText(String.valueOf(selectedCustomer.getCreateDate()));

            // Set N/A labels for customers who have not yet been updated
            if (Objects.equals(selectedCustomer.getLastUpdatedBy(), "-1")) {
                lastUpdatedByLabel.setText("N/A");
            } else {
                lastUpdatedByLabel.setText(selectedCustomer.getLastUpdatedBy());
            }
            boolean b = Objects.equals(selectedCustomer.getLastUpdate(), LocalDate.of(1970, Month.JANUARY,
                    1).atStartOfDay());
            if (b) {
                lastUpdateDateLabel.setText("N/A");
            } else {
                lastUpdateDateLabel.setText(String.valueOf(localLastUpdate));
            }

            // Set divisions list based off of the country
            // for US
            if (!(countryComboBox.getSelectionModel().isEmpty())) {
                ObservableList<String> divisionNameList = FXCollections.observableArrayList();
                if (countryComboBox.getSelectionModel().getSelectedItem().equals("U.S")) {
                    for (FirstLevelDivision division : divisionList) {
                        if (division.getCountryId() == 1)
                            divisionNameList.add(division.getDivision());
                    }
                    divisionComboBox.setItems(divisionNameList);
                }

                // For UK
                if (countryComboBox.getSelectionModel().getSelectedItem().equals("UK")) {
                    for (FirstLevelDivision division : divisionList) {
                        if (division.getCountryId() == 2)
                            divisionNameList.add(division.getDivision());
                    }
                    divisionComboBox.setItems(divisionNameList);
                }

                // For Canada
                if (countryComboBox.getSelectionModel().getSelectedItem().equals("Canada")) {
                    for (FirstLevelDivision division : divisionList) {
                        if (division.getCountryId() == 3)
                            divisionNameList.add(division.getDivision());
                    }
                    divisionComboBox.setItems(divisionNameList);
                }
            }
        }
    }

    /**
     *
     * This method is called when the countryComboBox object is selected. It populates the divisionComboBox object with the
     * appropriate divisions based on the country selected in the countryComboBox object.
     * @param event the ActionEvent object that triggered the method call
     */
    @FXML
    void OnActionCountrySelected(ActionEvent event) {
        // Set divisions list based off of the country
        // for US
        if (!(countryComboBox.getSelectionModel().isEmpty())) {
            ObservableList<String> divisionNameList = FXCollections.observableArrayList();
            if (countryComboBox.getSelectionModel().getSelectedItem().equals("U.S")) {
                for (FirstLevelDivision division : divisionList) {
                    if (division.getCountryId() == 1)
                        divisionNameList.add(division.getDivision());
                }
                divisionComboBox.setItems(divisionNameList);
            }
            // For UK
            if (countryComboBox.getSelectionModel().getSelectedItem().equals("UK")) {
                for (FirstLevelDivision division : divisionList) {
                    if (division.getCountryId() == 2)
                        divisionNameList.add(division.getDivision());
                }
                divisionComboBox.setItems(divisionNameList);
            }
            // For Canada
            if (countryComboBox.getSelectionModel().getSelectedItem().equals("Canada")) {
                for (FirstLevelDivision division : divisionList) {
                    if (division.getCountryId() == 3)
                        divisionNameList.add(division.getDivision());
                }
                divisionComboBox.setItems(divisionNameList);
            }
        }
    }

    /**
     *
     * Initializes the UI elements in the edit customer scene.
     * @param url the URL to the location of the FXML file associated with this controller
     * @param resourceBundle the bundle containing any localized resources for this controller
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        // Get desired list values
        try {
            divisionList = FXCollections.observableArrayList(FirstLevelDivisionAccess.getAllDivisions());
            countryList = FXCollections.observableArrayList(CountryAccess.getAllCountries());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Populate division names in combo box
        ObservableList<String> divisionNameList = FXCollections.observableArrayList();
        for (FirstLevelDivision division : divisionList) {
            divisionNameList.add(division.getDivision());
        }

        // Set list
        divisionComboBox.setItems(divisionNameList);

        // Populate country names in combo box
        ObservableList<String> countryNameList = FXCollections.observableArrayList();
        for (Country country : countryList) {
            countryNameList.add(country.getCountry());

        }

        // Set list
        countryComboBox.setItems(countryNameList);

        // Set the combo boxes' items after the combo boxes have been added to the scene
        divisionComboBox.setItems(divisionNameList);
        countryComboBox.setItems(countryNameList);
    }
}
