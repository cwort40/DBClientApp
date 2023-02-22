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
import javafx.scene.control.TextField;
import com.c195.dbclientapp.helper.ValidateControl;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

/**
 *
 * Controller for the Add Customer scene.
 * Allows the user to add a new customer to the database by providing all required information.
 * Also checks for input errors and displays error messages if necessary.
 */
public class AddCustomerController implements Initializable {

    // Initialize idTotal to be used for generating uniqueId
    private static int idTotal = 1;

    // Declare observable list with all the division objects (used in two methods)
    ObservableList<FirstLevelDivision> divisionList =
            FXCollections.observableArrayList(FirstLevelDivisionAccess.getAllDivisions());

    // Declare observable list with all the country objects (used in two methods)
    ObservableList<Country> countryList = FXCollections.observableArrayList(CountryAccess.getAllCountries());

    ValidateControl vc = new ValidateControl();

    // Declare all FXML controls
    @FXML
    private TextField idTxt;
    @FXML
    private TextField addressTxt;
    @FXML
    private ComboBox<String> countryComboBox;
    @FXML
    private ComboBox<String> divisionComboBox;
    @FXML
    private TextField nameTxt;
    @FXML
    private TextField phoneTxt;
    @FXML
    private TextField postalCodeTxt;

    /**
     *
     * Constructor for the AddCustomerController class.
     * Initializes the countryList and divisionList fields by querying the database.
     * @throws SQLException if there is an error executing the SQL statements
     */
    public AddCustomerController() throws SQLException {
    }

    /**
     *
     * Event handler for the Cancel button.
     * When the Cancel button is clicked, the scene is changed to the customers scene.
     * @param event the action event that triggered the handler
     * @throws IOException if there is an error loading the customers scene
     */
    @FXML
    void OnActionCancel(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "customers.fxml", "Customers");
    }

    /**
     *
     * Event handler for the Add button. When the Add button is clicked, this method checks that all required fields are
     * not empty, that the phone number is in the correct format, and then adds the new customer to the database. If any
     * of these conditions are not met, it displays an error message. <br>
     * LAMBDA EXPRESSIONS:
     * <br>
     * 1. The `getNewId` variable is a `Supplier` that, when called using the `get()` method, increments the idTotal
     * variable and returns the new value. This can be more concise and readable than creating a separate method to
     * perform the same task.
     * <br>
     * 2. The lambda expression is used to define a function that takes in a string and returns a boolean value.
     * This function checks if the input string is in the correct phone number format by using the matches method of the
     * String class and a regular expression to check if the string matches the pattern "^\\d{3}-\\d{3}-\\d{4}$". Using
     * a lambda expression in this case can be more concise and easier to read than defining a traditional method with
     * the same functionality. It also allows for the function to be defined and used in a single line of code, which
     * can be useful when the function is only used in a few places and does not need to be reused elsewhere in the
     * codebase.
     * @param event the event that triggered this method
     * @throws IOException if there is an error loading the customers scene
     * @throws SQLException if there is an error executing the SQL statements
     */
    @FXML
    void OnActionAdd(ActionEvent event) throws IOException, SQLException {
        vc.validateControl(nameTxt, "Name field is required.");
        vc.validateControl(addressTxt, "Address field is required.");
        vc.validateControl(postalCodeTxt, "Postal field is required.");
        vc.validateControl(phoneTxt, "Phone field is required.");
        vc.validateControl(countryComboBox, "Country field is required.");
        vc.validateControl(divisionComboBox, "Division field is required.");

        if (vc.isInputError()) {
            String errorMessage = String.join("\n", vc.getErrorMessages());
            DialogBox.displayAlert("Error", errorMessage);
            return;
        }

        // Validate phone number format
        if (!isPhoneNumberValid(phoneTxt.getText())) {
            DialogBox.displayAlert("Error", "Phone must be in the format xxx-xxx-xxxx.");
            return;
        }

        // Create a new customer object
        Customer newCustomer = createNewCustomer();

        // Add the customer to the database
        if (!CustomerAccess.addCustomer(newCustomer)) {
            DialogBox.displayAlert("Error", "Failed to save customer information.");
            return;
        }

        // Load the customers scene
        LoadSceneHelper.loadScene(event, "customers.fxml", "Customers");
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.matches("^\\d{3}-\\d{3}-\\d{4}$");
    }

    private Customer createNewCustomer() {
        try {
            int uniqueId = generateUniqueId();
            String name = nameTxt.getText();
            String address = addressTxt.getText();
            String postalCode = postalCodeTxt.getText();
            String phone = phoneTxt.getText();
            LocalDateTime createDate = LocalDateTime.now();
            String createdBy = LoginController.currentUser;
            LocalDateTime lastUpdate = LocalDate.of(1970, Month.JANUARY, 1).atStartOfDay();
            String lastUpdatedBy = "-1";
            String selectedDivisionName = divisionComboBox.getSelectionModel().getSelectedItem();
            int divisionId = FirstLevelDivisionAccess.getDivisionId(selectedDivisionName);
            return new Customer(uniqueId, name, address, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdatedBy, divisionId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    private int generateUniqueId() {
        return idTotal++;
    }

    /**
     *
     * Event handler for the country selection. When a country is selected from the country combo box, this method will
     * update the options in the division combo box to display only divisions within the selected country.
     * @param event the action event that triggered the handler
     */
    @FXML
    void OnActionCountrySelected(ActionEvent event) {
        String selectedCountry = countryComboBox.getSelectionModel().getSelectedItem();
        if (selectedCountry != null) {
            divisionComboBox.getItems().clear(); // Clear previous items

            int countryId;
            switch (selectedCountry) {
                case "U.S" -> countryId = 1;
                case "UK" -> countryId = 2;
                case "Canada" -> countryId = 3;
                default -> {
                    return; // Do nothing for other countries
                }
            }

            divisionList.stream()
                    .filter(division -> division.getCountryId() == countryId)
                    .map(FirstLevelDivision::getDivision)
                    .forEach(divisionComboBox.getItems()::add);
        }
    }

    /**
     *
     * This method is called when the Add Customer screen is first displayed. It sets the items in the division and
     * country combo boxes by looping through the list of divisions and countries, adding their names to an observable
     * list, and setting the combo boxes' items to the observable lists.
     * @param url the URL of the FXML file that was used to create the scene
     * @param resourceBundle the resource bundle that was used to localize the scene
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDivisionComboBoxItems();
        setCountryComboBoxItems();
    }

    private void setDivisionComboBoxItems() {
        ObservableList<String> divisionNameList = FXCollections.observableArrayList();
        for (FirstLevelDivision division : divisionList) {
            divisionNameList.add(division.getDivision());
        }
        divisionComboBox.setItems(divisionNameList);
    }

    private void setCountryComboBoxItems() {
        ObservableList<String> countryNameList = FXCollections.observableArrayList();
        for (Country country : countryList) {
            countryNameList.add(country.getCountry());
        }
        countryComboBox.setItems(countryNameList);
    }
}
