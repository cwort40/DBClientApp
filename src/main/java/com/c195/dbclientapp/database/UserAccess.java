package com.c195.dbclientapp.database;

import com.c195.dbclientapp.helper.JDBC;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This abstract class provides access to user data in the database.
 * It includes methods for retrieving user IDs and performing operations on user records.
 */
public abstract class UserAccess {

    /**
     * Retrieves a list of all user IDs from the database.
     * @return an ObservableList of Integer objects representing the user IDs
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static ObservableList<Integer> getAllUserIds() throws SQLException {

        // Create SQL select statement
        String sql = "SELECT * FROM users";

        // Create a PS with the SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Execute the query
        ResultSet rs = ps.executeQuery();

        // Initialize userIdList
        ObservableList<Integer> userIdList = FXCollections.observableArrayList();

        // Add results to list and return
        while (rs.next()) {
            int userId = rs.getInt("User_ID");
            userIdList.add(userId);
        }
        return userIdList;
    }
}
