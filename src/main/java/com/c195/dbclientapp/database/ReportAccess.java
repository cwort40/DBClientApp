package com.c195.dbclientapp.database;

import com.c195.dbclientapp.helper.JDBC;
import com.c195.dbclientapp.model.CustomerReport;
import com.c195.dbclientapp.model.MonthReport;
import com.c195.dbclientapp.model.TypeReport;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Provides methods for generating reports from the database.
 */
public abstract class ReportAccess {

    /**
     * Retrieves a list of type reports from the database.
     * @return an ObservableList of TypeReport objects
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static ObservableList<TypeReport> getAllTypeReports() throws SQLException {

        // Create SQL select statement
        String sql = "SELECT Type, COUNT(Type) as Total FROM client_schedule.appointments GROUP BY Type";

        // Create PS with select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Initialize TypeReport list
        ObservableList<TypeReport> typeReports = FXCollections.observableArrayList();

        // Add results to list and return
        while (rs.next()) {
            String type = rs.getString("Type");
            int total = rs.getInt("Total");
            TypeReport typeReport = new TypeReport(total, type);
            typeReports.add(typeReport);
        }
        return typeReports;
    }

    /**
     * Retrieves a list of month reports from the database.
     * @return an ObservableList of MonthReport objects
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static ObservableList<MonthReport> getAllMonthReports() throws SQLException {

        // Create SQL select statement
        String sql = "SELECT MONTHNAME(Start) as Month," +
                " COUNT(*) as Total FROM client_schedule.appointments GROUP BY Month";

        // Create PS with select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Initialize monthReports list
        ObservableList<MonthReport> monthReports = FXCollections.observableArrayList();

        // Add results to list and return
        while (rs.next()) {
            String month = rs.getString("Month");
            int total = rs.getInt("Total");
            MonthReport monthReport = new MonthReport(month, total);
            monthReports.add(monthReport);
        }
        return monthReports;
    }

    /**
     * Retrieves a list of customer reports from the database, containing the customer ID and country ID for each customer.
     * @return an ObservableList of CustomerReport objects
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static ObservableList<CustomerReport> getAllCustomerReports() throws SQLException {

        // Create SQL select statement
        String sql = """
                SELECT c.Customer_ID, fld.Country_ID
                FROM customers c
                INNER JOIN first_level_divisions fld
                ON c.Division_ID = fld.Division_ID""";

        // Create PS with select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Initialize customerReports list
        ObservableList<CustomerReport> customerReports = FXCollections.observableArrayList();

        // Add results to list and return
        while (rs.next()) {
            int customerId = rs.getInt("Customer_ID");
            int countryId = rs.getInt("Country_ID");
            CustomerReport customerReport = new CustomerReport(customerId, countryId);
            customerReports.add(customerReport);
        }
        return customerReports;
    }

}
