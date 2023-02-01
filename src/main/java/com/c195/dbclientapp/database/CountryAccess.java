package com.c195.dbclientapp.database;

import com.c195.dbclientapp.helper.JDBC;
import com.c195.dbclientapp.model.Country;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The CountryAccess class provides methods for interacting with the countries table in the database.
 */
public abstract class CountryAccess {

    /**
     * Returns a list of all countries in the countries table.
     * @return an ObservableList of Country objects representing all countries in the table
     * @throws SQLException if a database error occurs
     */
    public static ObservableList<Country> getAllCountries() throws SQLException {

        // Create SQL select statement
        String sql = "SELECT * FROM client_schedule.countries;";

        // Create PS with SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Create list of countries
        ObservableList<Country> countries = FXCollections.observableArrayList();

        // Add countries to list
        while (rs.next()) {

            // Create formatter for LocalDateTime objects
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // Store query results in variables
            int countryId = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            LocalDateTime createDate = LocalDateTime.parse(rs.getString("Create_Date"), formatter);
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = LocalDateTime.parse(rs.getString("Last_Update"), formatter);
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            // Create new country object with those variables and return
            Country country = new Country (
                    countryId,
                    countryName,
                    createDate,
                    createdBy,
                    lastUpdate,
                    lastUpdatedBy
            );
            countries.add(country);
        }
        return countries;
    }

    /**
     * Returns the country with the specified ID from the countries table.
     * @param id the ID of the country to retrieve
     * @return a Country object representing the retrieved country
     * @throws SQLException if a database error occurs
     */
    public static Country getCountryById(int id) throws SQLException {
        String sql = "SELECT * FROM client_schedule.countries WHERE Country_ID = ?;";
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ps.setInt(1, id);
        ResultSet rs = ps.executeQuery();
        Country country = null;
        while (rs.next()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            int countryId = rs.getInt("Country_ID");
            String countryName = rs.getString("Country");
            LocalDateTime createDate = LocalDateTime.parse(rs.getString("Create_Date"), formatter);
            String createdBy = rs.getString("Created_By");
            LocalDateTime lastUpdate = LocalDateTime.parse(rs.getString("Last_Update"), formatter);
            String lastUpdatedBy = rs.getString("Last_Updated_By");

            country = new Country (
                    countryId,
                    countryName,
                    createDate,
                    createdBy,
                    lastUpdate,
                    lastUpdatedBy
            );
            
        }
        return country;
    }
}
