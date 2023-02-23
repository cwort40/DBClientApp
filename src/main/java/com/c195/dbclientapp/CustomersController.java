package com.c195.dbclientapp;

import com.c195.dbclientapp.database.AppointmentAccess;
import com.c195.dbclientapp.database.CountryAccess;
import com.c195.dbclientapp.database.CustomerAccess;
import com.c195.dbclientapp.database.FirstLevelDivisionAccess;
import com.c195.dbclientapp.model.*;
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

        // Send selected part to updateAppointmentController
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("updateCustomer.fxml"));
        loader.load();
        FirstLevelDivision divisionName = customerTableView.getSelectionModel().getSelectedItem().getDivision();
        Country countryName = customerTableView.getSelectionModel().getSelectedItem().getCountry();
        UpdateCustomerController UCustomerController = loader.getController();
        UCustomerController.sendCustomer(customerTableView.getSelectionModel().getSelectedItem().getCustomer(),
                divisionName, countryName);

        // If customer is selected, takes user to update customer scene
        Stage stage;
        if (!(customerTableView.getSelectionModel().getSelectedItem() == null)) {
            stage = (Stage)((Button)event.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setTitle("Update Customer");
            stage.setScene(new Scene(scene));
            stage.show();
        }
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
        ObservableList<CustomerWithDivision> customerWithDivisions = FXCollections.observableArrayList();
        Customer selectedCustomer = customerTableView.getSelectionModel().getSelectedItem().getCustomer();

        // If a customer is selected
        if (selectedCustomer != null) {
            // Confirm the deletion with the user
            boolean confirm = displayConfirmation("Delete Customer",
                    "Are you sure you want to delete the selected customer and all associated appointments?");
            if (confirm) {
                try {

                    ObservableList<Appointment> customerAppointments =
                            AppointmentAccess.getAppointmentsByCustomerId(selectedCustomer.getCustomerId());
                    if (customerAppointments.isEmpty()) {
                        // Delete the customer and all of their appointments
                        CustomerAccess.deleteCustomer(selectedCustomer.getCustomerId());

                        // Display alert
                        displayAlert("Success!", "Customer deleted");

                        // Get the updated list of customers
                        ObservableList<Customer> customers = CustomerAccess.getAllCustomers();

                        // Get the updated list of divisions
                        ObservableList<FirstLevelDivision> divisions = FirstLevelDivisionAccess.getAllDivisions();

                        // Get the updated list of countries
                        ObservableList<Country> countries = CountryAccess.getAllCountries();

                        // Create a CustomerWithDivision object for each customer and add it to the list
                        for (Customer customer : customers) {
                            FirstLevelDivision division = null;
                            Country country = null;
                            try {
                                division = FirstLevelDivisionAccess.getDivisionById(customer.getDivisionId());
                                assert division != null;
                                country = CountryAccess.getCountryById(division.getCountryId());
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            customerWithDivisions.add(new CustomerWithDivision(customer, division, country));
                        }

                        // Update the table with the updated list of CustomerWithDivision objects
                        customerTableView.setItems(customerWithDivisions);
                    } else {
                        displayAlert("Error", "Unable to delete customer that has scheduled appointments");
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        } else {
            // Show an error message if no customer is selected
            displayAlert("Error", "No customer selected.");
        }
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

        // Initialize lists of items needed to populate table view
        ObservableList<Customer> customers;
        ObservableList<FirstLevelDivision> divisions;
        ObservableList<Country> countries;
        ObservableList<CustomerWithDivision> customerWithDivisions = FXCollections.observableArrayList();

        // Populate lists with desired values
        try {
            customers = CustomerAccess.getAllCustomers();
            divisions = FirstLevelDivisionAccess.getAllDivisions();
            countries = CountryAccess.getAllCountries();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Create a CustomerWithDivision object for each customer and add it to the list
        for (Customer customer : customers) {
            FirstLevelDivision division = null;
            Country country = null;
            try {
                division = FirstLevelDivisionAccess.getDivisionById(customer.getDivisionId());
                assert division != null;
                country = CountryAccess.getCountryById(division.getCountryId());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            customerWithDivisions.add(new CustomerWithDivision(customer, division, country));
        }

        // Populate table with customerWithDivisions list
        customerTableView.setItems(customerWithDivisions);
        idCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        nameCol.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        addressCol.setCellValueFactory(new PropertyValueFactory<>("address"));
        postalCodeCol.setCellValueFactory(new PropertyValueFactory<>("postalCode"));
        phoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        divisionCol.setCellValueFactory(new PropertyValueFactory<>("divisionName"));
        countryCol.setCellValueFactory(new PropertyValueFactory<>("countryName"));
    }
}

