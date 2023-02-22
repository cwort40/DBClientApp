package com.c195.dbclientapp;

import com.c195.dbclientapp.database.CountryAccess;
import com.c195.dbclientapp.database.CustomerAccess;
import com.c195.dbclientapp.database.FirstLevelDivisionAccess;
import com.c195.dbclientapp.model.Country;
import com.c195.dbclientapp.model.Customer;
import com.c195.dbclientapp.model.CustomerWithDivision;
import com.c195.dbclientapp.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import static com.c195.dbclientapp.DialogBox.displayAlert;
import static com.c195.dbclientapp.DialogBox.displayConfirmation;

/**
 *
 * The controller class for the Customers screen. This screen displays a table of all customers in the database, with
 * columns for the customer's name, address, postal code, phone number, country, and division. The user can add, update,
 * or delete customers from the table, or return to the main menu.
 */
public class CustomersController implements Initializable {

    // Declare all FXML controls
    @FXML
    private TableColumn<Customer, String> addressCol;
    @FXML
    private TableColumn<Customer, String> countryCol;
    @FXML
    private TableView<CustomerWithDivision> customerTableView;
    @FXML
    private TableColumn<Customer, Integer> divisionCol;
    @FXML
    private TableColumn<Customer, Integer> idCol;
    @FXML
    private TableColumn<Customer, String> nameCol;
    @FXML
    private TableColumn<Customer, String> phoneCol;
    @FXML
    private TableColumn<Customer, Integer> postalCodeCol;

    /**
     * Event handler for the Add button. When the Add button is clicked, the scene is changed to the Add Customer
     * scene.
     * @param event the action event that triggered the handler
     * @throws IOException if there is an error loading the main menu scene
     */
    @FXML
    void OnActionAdd(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "addCustomer.fxml", "Add Customer");
    }

    /**
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
     * This method is called when the Update button is clicked on the Customers screen. It sends the selected customer
     * and their associated division and country to the Update Customer screen. If a customer is not selected, it does
     * nothing.
     * @param event the event that triggered this method
     * @throws IOException if there is an error loading the Update Customer screen
     * @throws SQLException if there is an error executing the SQL statements
     */
    @FXML
    void OnActionUpdate(ActionEvent event) throws java.io.IOException, SQLException {
        if (customerTableView.getSelectionModel().getSelectedItem() == null) {
            displayAlert("Error", "No customer selected.");
            return;
        }
        // Load the update customer scene
        FXMLLoader loader = new FXMLLoader(getClass().getResource("updateCustomer.fxml"));
        Parent root = loader.load();
        UpdateCustomerController controller = loader.getController();
        controller.sendCustomer(
                customerTableView.getSelectionModel().getSelectedItem().getCustomer(),
                customerTableView.getSelectionModel().getSelectedItem().getDivision(),
                customerTableView.getSelectionModel().getSelectedItem().getCountry()
        );
        // Set up the stage and scene for the update customer scene
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();;
        stage.setTitle("Update Customer");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     *
     * Event handler for the Delete button. When the Delete button is clicked, the selected customer is deleted
     * along with all of their associated appointments. The user is prompted to confirm the deletion before it is
     * carried out. If a customer is not selected, an error message is displayed.
     * @param event the action event that triggered the handler
     * @throws IOException if there is an error loading the main menu scene
     */
    @FXML
    void OnActionDelete(ActionEvent event) throws java.io.IOException {
        // Get the selected customer from the table view
        CustomerWithDivision selectedCustomerWithDivision = customerTableView.getSelectionModel().getSelectedItem();
        if (selectedCustomerWithDivision == null) {
            // Show an error message if no customer is selected
            displayAlert("Error", "No customer selected.");
            return;
        }
        Customer selectedCustomer = selectedCustomerWithDivision.getCustomer();

        // Confirm the deletion with the user
        boolean confirm = displayConfirmation("Delete Customer", "Do you want to delete the selected customer and all associated data?");
        if (!confirm) {
            return;
        }

        try {
            // Delete the customer and all of their appointments
            CustomerAccess.deleteCustomer(selectedCustomer.getCustomerId());
            // Display alert
            displayAlert("Success!", "Customer deleted");
            // Refresh the customer table view
            refreshCustomerTableView();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void refreshCustomerTableView() throws SQLException {
        // Get the updated list of customers
        ObservableList<Customer> customers = CustomerAccess.getAllCustomers();
        // Get the updated list of divisions
        ObservableList<FirstLevelDivision> divisions = FirstLevelDivisionAccess.getAllDivisions();
        // Get the updated list of countries
        ObservableList<Country> countries = CountryAccess.getAllCountries();
        // Create a CustomerWithDivision object for each customer and add it to the list
        ObservableList<CustomerWithDivision> customerWithDivisions = FXCollections.observableArrayList();

        for (Customer customer : customers) {
            FirstLevelDivision division = FirstLevelDivisionAccess.getDivisionById(customer.getDivisionId());
            assert division != null;
            Country country = CountryAccess.getCountryById(division.getCountryId());
            customerWithDivisions.add(new CustomerWithDivision(customer, division, country));
        }
        // Update the table with the updated list of CustomerWithDivision objects
        customerTableView.setItems(customerWithDivisions);
    }


    /**
     *
     * The initialize method is called when the Customers screen is loaded. It populates the table view with a list of
     * CustomerWithDivision objects, which contain a customer object, the division object associated with that customer,
     * and the country object associated with that division. It also sets the values of each column in the table view
     * to the corresponding values in the CustomerWithDivision objects.
     * @param url the location of the FXML file that defines the layout of the Customers screen
     * @param resourceBundle resources to be used for localization
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Initialize lists of items needed to populate table view
            ObservableList<Customer> customers = CustomerAccess.getAllCustomers();
            ObservableList<FirstLevelDivision> divisions = FirstLevelDivisionAccess.getAllDivisions();
            ObservableList<Country> countries = CountryAccess.getAllCountries();

            // Create a CustomerWithDivision object for each customer and add it to the list
            ObservableList<CustomerWithDivision> customerWithDivisions = customers.stream()
                    .map(customer -> {
                        FirstLevelDivision division;
                        Country country;
                        try {
                            division = FirstLevelDivisionAccess.getDivisionById(customer.getDivisionId());
                            assert division != null;
                            country = CountryAccess.getCountryById(division.getCountryId());
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        return new CustomerWithDivision(customer, division, country);
                    })
                    .collect(Collectors.toCollection(FXCollections::observableArrayList));

            // Populate table with customerWithDivisions list
            customerTableView.setItems(customerWithDivisions);
            idCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
            nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
            addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
            postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
            phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
            divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
            countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}