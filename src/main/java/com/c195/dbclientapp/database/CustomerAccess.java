package com.c195.dbclientapp.database;

import com.c195.dbclientapp.helper.JDBC;
import com.c195.dbclientapp.model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract class containing methods for accessing and modifying customer data in the database.
 */
public abstract class CustomerAccess {

    /**
     * Retrieves a list of all customers from the database.
     * @return an ObservableList of Customer objects
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static ObservableList<Customer> getAllCustomers() throws SQLException {

        // Create SQL select statement
        String sql = "SELECT * FROM customers";

        // Create PS with SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Create list of customers
        ObservableList<Customer> customers = FXCollections.observableArrayList();

        // Add customers to list
        while (rs.next()) {

            // Create formatter to convert the appropriate query results into LocalDateTime objects
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Store query results in variables
            int customerId = rs.getInt("Customer_ID");
            String name = rs.getString("Customer_Name");
            String address = rs.getString("Address");
            String postalCode = rs.getString("Postal_Code");
            String phone = rs.getString("Phone");
            LocalDateTime createDate = LocalDateTime.parse(rs.getString("Create_Date"), formatter);
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = LocalDateTime.parse(rs.getString("Last_Update"), formatter);
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int divisionId = rs.getInt("Division_ID");

            // Create new customer object with SQL results, add to list, and return
            Customer customer = new Customer(
                    customerId,
                    name,
                    address,
                    postalCode,
                    phone,
                    createDate,
                    createdBy,
                    lastUpdate,
                    lastUpdatedBy,
                    divisionId
            );
            customers.add(customer);
        }
        return customers;
    }

    /**
     * Retrieves a list of all customer IDs from the database.
     * @return an ObservableList of integers representing customer IDs
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static ObservableList<Integer> getAllCustomerIds() throws SQLException {

        // Create SQL select statement
        String sql = "SELECT * FROM customers";

        // Create PS with SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Initialize list of customer ID's
        ObservableList<Integer> customerIdList = FXCollections.observableArrayList();

        // Add ID's to list and return
        while (rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            customerIdList.add(customerId);
        }
        return customerIdList;
    }

    /**
     * Deletes a customer from the database based on the provided customer ID.
     * @param customerId the ID of the customer to delete
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static void deleteCustomer(int customerId) throws SQLException {

        // Create the DELETE SQL statement
        String deleteSql = "DELETE FROM customers WHERE Customer_ID = ?";

        // Create a prepared statement
        PreparedStatement ps = JDBC.connection.prepareStatement(deleteSql);

        // Set the customerId parameter
        ps.setInt(1, customerId);

        // Execute the DELETE statement
        ps.executeUpdate();
    }

    /**
     * Adds a new customer to the database based on the provided Customer object.
     * @param customer the Customer object to add to the database
     * @return true if the customer was added successfully, false if the customer already exists in the database
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static boolean addCustomer(Customer customer) throws SQLException {

        // Create the INSERT SQL statement
        String insertSql = "INSERT INTO customers (Customer_ID, Customer_Name, Address, Postal_Code, Phone," +
                " Create_Date, Created_By, Last_Update, Last_Updated_By, Division_ID)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create a prepared statement
        PreparedStatement ps = JDBC.connection.prepareStatement(insertSql);

        // Set the parameters for the prepared statement using the getter methods of the Customer object
        ps.setInt(1, customer.getCustomerId());
        ps.setString(2, customer.getCustomerName());
        ps.setString(3, customer.getAddress());
        ps.setString(4, customer.getPostalCode());
        ps.setString(5, customer.getPhone());
        ps.setObject(6, customer.getCreateDate());
        ps.setString(7, customer.getCreatedBy());
        ps.setObject(8, customer.getLastUpdate());
        ps.setString(9, customer.getLastUpdatedBy());
        ps.setInt(10, customer.getDivisionId());

        // Execute the INSERT statement and return the result
        return ps.executeUpdate() > 0;
    }

    /**
     * Updates an existing customer in the database based on the provided Customer object.
     * @param customer the Customer object to update in the database
     * @return true if the customer was updated successfully, false if the customer does not exist in the database
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static boolean updateCustomer(Customer customer) throws SQLException {
        // Create the UPDATE SQL statement
        String updateSql = "UPDATE customers SET Customer_Name = ?, Address = ?, Postal_Code = ?, Phone = ?," +
                " Last_Update = ?, Last_Updated_By = ?, Division_ID = ? WHERE Customer_ID = ?";

        // Create a prepared statement
        PreparedStatement ps = JDBC.connection.prepareStatement(updateSql);

        // Set the parameters for the prepared statement using the getter methods of the Customer object
        ps.setString(1, customer.getCustomerName());
        ps.setString(2, customer.getAddress());
        ps.setString(3, customer.getPostalCode());
        ps.setString(4, customer.getPhone());
        ps.setObject(5, customer.getLastUpdate());
        ps.setString(6, customer.getLastUpdatedBy());
        ps.setInt(7, customer.getDivisionId());
        ps.setInt(8, customer.getCustomerId());

        // Execute the UPDATE statement and return
        ps.executeUpdate();
        return true;
    }
}
