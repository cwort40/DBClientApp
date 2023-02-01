package com.c195.dbclientapp.database;

import com.c195.dbclientapp.helper.JDBC;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class provides methods for accessing the users table in the database.
 * It contains a method to validate the user login credentials and return the user's ID.
 */
public abstract class LoginAccess {

    /**
     * Validates a user by checking if the provided username and password match a record in the database.
     * @param username the username to be checked
     * @param password the password to be checked
     * @return the User_ID of the matching user, or -1 if no matching user is found
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static int validateUser(String username, String password) throws SQLException {

        // Select only the columns that are needed
        String sql = "SELECT User_ID FROM client_schedule.users WHERE User_Name = ? AND Password = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, username);
        ps.setString(2, password);
        ResultSet rs = ps.executeQuery();

        // Check if there is at least one matching user
        if (rs.next()) {
            int userId = rs.getInt("User_ID");
            return userId;
        } else {
        // Return -1 to indicate that no matching user was found
        return -1;
        }
    }

}
