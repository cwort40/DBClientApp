package com.c195.dbclientapp.database;

import com.c195.dbclientapp.helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Abstract class to provide access to contact information stored in a database.
 */
public abstract class ContactAccess {

    /**
     * Returns a list of all contact IDs stored in the database.
     *
     * @return an ObservableList of contact IDs
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public static ObservableList<Integer> getAllContactIds() throws SQLException {

        // Create SQL select statement
        String sql = "SELECT * FROM contacts";

        // Create PS statement with SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Create list of contact ID's
        ObservableList<Integer> contactIdList = FXCollections.observableArrayList();

        // Add them to list and return
        while (rs.next()) {
            int customerId = rs.getInt("Contact_ID");
            contactIdList.add(customerId);
        }
        return contactIdList;
    }
}
