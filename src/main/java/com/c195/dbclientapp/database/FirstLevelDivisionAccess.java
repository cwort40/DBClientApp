package com.c195.dbclientapp.database;

import com.c195.dbclientapp.helper.JDBC;
import com.c195.dbclientapp.model.FirstLevelDivision;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class for accessing the `first_level_divisions` table in the database.
 */
public abstract class FirstLevelDivisionAccess {

    /**
     * Retrieves a list of all first level divisions from the database.
     * @return an ObservableList of FirstLevelDivision objects
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static ObservableList<FirstLevelDivision> getAllDivisions() throws SQLException {

        // Create SQL select statement
        String sql = "SELECT * FROM client_schedule.first_level_divisions;";

        // Create PS with SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Initialize divisions list and add divisions
        ObservableList<FirstLevelDivision> divisions = FXCollections.observableArrayList();
        while (rs.next()) {

            // Create formatter for LocalDateTime objects
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Add results to respective variables
            int divisionId = rs.getInt("Division_ID");
            String divisionName = rs.getString("Division");
            LocalDateTime createDate = LocalDateTime.parse(rs.getString("Create_Date"), formatter);
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = LocalDateTime.parse(rs.getString("Last_Update"), formatter);
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int countryId = rs.getInt("Country_ID");

            // Create new division, add to list, and return list
            FirstLevelDivision division = new FirstLevelDivision(
                    divisionId,
                    divisionName,
                    createDate,
                    createdBy,
                    lastUpdate,
                    lastUpdatedBy,
                    countryId
            );
            divisions.add(division);
        }
        return divisions;
    }

    /**
     * Retrieves the division ID for a given division name.
     * @param selectedDivisionName the name of the division to get the ID for
     * @return the division ID
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static int getDivisionId(String selectedDivisionName) throws SQLException {

        // Query the first_level_division table to get the divisionId for the selected division name
        String sql = "SELECT Division_ID FROM client_schedule.first_level_divisions WHERE division = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setString(1, selectedDivisionName);
        ResultSet rs = ps.executeQuery();

        // Get the divisionId from the result set
        int divisionId = 0;
        if (rs.next()) {
            divisionId = rs.getInt("Division_ID");
        }
        return divisionId;
    }


    /**
     * Retrieves a first level division by its ID.
     * @param divisionId the ID of the division to retrieve
     * @return a FirstLevelDivision object
     * @throws SQLException if there is an error executing the SQL statement
     */
    public static FirstLevelDivision getDivisionById(int divisionId) throws SQLException {

        // Create SQL select statement
        String sql = "SELECT * FROM client_schedule.first_level_divisions WHERE Division_ID = ?";

        // Create PS using SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Add value to PS
        ps.setInt(1, divisionId);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Create a division object with result set
        if (rs.next()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            String divisionName = rs.getString("Division");
            LocalDateTime createDate = LocalDateTime.parse(rs.getString("Create_Date"), formatter);
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = LocalDateTime.parse(rs.getString("Last_Update"), formatter);
            String lastUpdatedBy = rs.getString("Last_Updated_By");
            int countryId = rs.getInt("Country_ID");

            return new FirstLevelDivision(
                    divisionId,
                    divisionName,
                    createDate,
                    createdBy,
                    lastUpdate,
                    lastUpdatedBy,
                    countryId
            );
        } else {
            return null;
        }
    }
}