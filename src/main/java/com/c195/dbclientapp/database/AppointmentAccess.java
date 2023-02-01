package com.c195.dbclientapp.database;


import com.c195.dbclientapp.helper.JDBC;
import com.c195.dbclientapp.model.Appointment;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * This abstract class provides access to the appointments table in the database. It contains methods for retrieving,
 * deleting, adding, and updating appointments.
 */
public abstract class AppointmentAccess {

    /**
     * Retrieves all appointments from the database, ordered by start time.
     *
     * @return an ObservableList of Appointment objects representing all appointments in the database
     * @throws SQLException if a database access error occurs or this method is called on a closed connection
     */
    public static ObservableList<Appointment> getAllAppointments() throws SQLException {
        // SQL statement to select all appointments, ordered by start time
        String sql = "SELECT * FROM client_schedule.appointments ORDER BY start";

        // Prepare the statement and execute the query
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();

        // Create an ObservableList to store the appointments
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        // Iterate through the result set, extracting each appointment and adding it to the list
        while (rs.next()) {
            Appointment appointment = extractAppointmentFromResultSet(rs);
            appointments.add(appointment);
        }

        // Return the list of appointments
        return appointments;
    }

    /**
     * Deletes an appointment from the appointments table in the database.
     *
     * @param appointmentId the ID of the appointment to delete
     * @throws SQLException if a database access error occurs or the generated SQL statement does not return a result set
     */
    public static void deleteAppointment(int appointmentId) throws SQLException {
        // Create a prepared statement with a delete SQL statement
        String deleteSql = "DELETE FROM appointments WHERE Appointment_ID = ?";
        PreparedStatement ps = JDBC.connection.prepareStatement(deleteSql);

        // Set the appointment ID parameter and execute the delete statement
        ps.setInt(1, appointmentId);
        ps.executeUpdate();
    }

    /**
     *
     * Adds an appointment to the database.
     * @param appointment the appointment to add
     * @return true if the appointment was added successfully, false otherwise
     * @throws SQLException if a database error occurs
     */
    public static boolean addAppointment(Appointment appointment) throws SQLException {

        // Define the SQL insert statement
        String insertSql = "INSERT INTO appointments (Title, Description, Location, Type, Start, End, Create_Date," +
                " Created_By, Last_Update, Last_Updated_By, Customer_ID, User_ID, Contact_ID)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Create a prepared statement using the insert SQL statement
        PreparedStatement ps = JDBC.connection.prepareStatement(insertSql);

        // Set the values for the prepared statement
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setObject(5, appointment.getStart());
        ps.setObject(6, appointment.getEnd());
        ps.setObject(7, appointment.getCreateDate());
        ps.setString(8, appointment.getCreatedBy());
        ps.setObject(9, appointment.getLastUpdate());
        ps.setString(10, appointment.getLastUpdatedBy());
        ps.setInt(11, appointment.getCustomerId());
        ps.setInt(12, appointment.getUserId());
        ps.setInt(13, appointment.getContactId());

        // Execute statement and return true
        ps.executeUpdate();
        return true;
    }

    /**
     * Updates an appointment in the database.
     * @param appointment the appointment to update
     * @return true if the update was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public static boolean updateAppointment(Appointment appointment) throws SQLException {

        // Define the SQL update statement
        String updateSql = "UPDATE appointments SET Title = ?, Description = ?, Location = ?, Type = ?, Start = ?," +
                " End = ?, Last_Update = ?, Last_Updated_By = ?, Customer_ID = ?, User_ID = ?," +
                " Contact_ID = ? WHERE Appointment_ID = ?";

        // Create a prepared statement using the update SQL statement
        PreparedStatement ps = JDBC.connection.prepareStatement(updateSql);

        // Set the values for the prepared statement
        ps.setString(1, appointment.getTitle());
        ps.setString(2, appointment.getDescription());
        ps.setString(3, appointment.getLocation());
        ps.setString(4, appointment.getType());
        ps.setObject(5, appointment.getStart());
        ps.setObject(6, appointment.getEnd());
        ps.setObject(7, appointment.getLastUpdate());
        ps.setString(8, appointment.getLastUpdatedBy());
        ps.setInt(9, appointment.getCustomerId());
        ps.setInt(10, appointment.getUserId());
        ps.setInt(11, appointment.getContactId());
        ps.setInt(12, appointment.getAppointmentId());

        // Execute statement and return true
        ps.executeUpdate();
        return true;
    }

    /**
     * Retrieves a list of all appointments for the current month.
     * @return an {@code ObservableList} of all appointments for the current month
     * @throws SQLException if a database access error occurs or this method is called on a closed PreparedStatement
     */
    public static ObservableList<Appointment> getAllAppointmentsByMonth() throws SQLException {

        // Get the current month and year
        LocalDateTime now = LocalDateTime.now();
        int currentMonth = now.getMonthValue();
        int currentYear = now.getYear();

        // Define SQL select statement
        String sql = "SELECT * FROM client_schedule.appointments WHERE MONTH(start) AND YEAR(start) ORDER BY start;";

        // Create a prepared statement using SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Set PS values
        ps.setInt(1, currentMonth);
        ps.setInt(2, currentYear);

        // Execute statement
        ResultSet rs = ps.executeQuery();

        // Make a list of appointments and return
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();
        while (rs.next()) {
            Appointment appointment = extractAppointmentFromResultSet(rs);
            appointments.add(appointment);
        }
        return appointments;
    }

    /**
     * Retrieves a list of all appointments that fall within the current week (Sunday through Saturday).
     * @return an {@link ObservableList} of {@link Appointment} objects representing the appointments in the current
     * week
     * @throws SQLException if there is an issue executing the SQL query
     */
    public static ObservableList<Appointment> getAllAppointmentsByWeek() throws SQLException {

        // Create a SQL select statement
        String sql = "SELECT *\n" +
                "FROM client_schedule.appointments\n" +
                "WHERE WEEK(start) = WEEK(CURDATE())\n" +
                "AND YEAR(start) = YEAR(CURDATE())\n";

        // Create a PS with SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Initialize list of appointments
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        // Add appointments to list and return
        while (rs.next()) {
            Appointment appointment = extractAppointmentFromResultSet(rs);
            appointments.add(appointment);
        }
        return appointments;
    }

    /**
     * Retrieves a list of appointments associated with a given customer.
     * @param customer the ID of the customer whose appointments should be retrieved
     * @return an {@link ObservableList} of appointments associated with the given customer
     * @throws SQLException if a database error occurs while retrieving the appointments
     */
    public static ObservableList<Appointment> getAppointmentsByCustomerId(int customer) throws SQLException {

        // Create SQL select statement
        String sql = "SELECT * FROM client_schedule.appointments WHERE Customer_ID = ? ORDER BY start;";

        // Create PS with SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Set PS value
        ps.setInt(1, customer);

        // Execute statement
        ResultSet rs = ps.executeQuery();

        // Create list of appointments
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        // Add appointments to list and return
        while (rs.next()) {
            Appointment appointment = extractAppointmentFromResultSet(rs);
            appointments.add(appointment);
        }
        return appointments;
    }

    /**
     * Returns a list of all appointments associated with the given user.
     *
     * @param user the ID of the user
     * @return a list of appointments
     * @throws SQLException if a database error occurs
     */
    public static ObservableList<Appointment> getAppointmentsByUserId(int user) throws SQLException {

        // Create SQL select statement
        String sql = "SELECT * FROM client_schedule.appointments WHERE User_ID = ? ORDER BY start;";

        // Create PS with SQL select statement
        PreparedStatement ps = JDBC.connection.prepareStatement(sql);

        // Set PS value
        ps.setInt(1, user);

        // Execute query
        ResultSet rs = ps.executeQuery();

        // Create list of appointments
        ObservableList<Appointment> appointments = FXCollections.observableArrayList();

        // Add appointments to list and return
        while (rs.next()) {
            Appointment appointment = extractAppointmentFromResultSet(rs);
            appointments.add(appointment);
        }
        return appointments;
    }

    /**
     * Extracts an appointment object from the provided ResultSet.
     * @param rs the ResultSet to extract the appointment from
     * @return the extracted appointment
     * @throws SQLException if a database access error occurs
     */
    private static Appointment extractAppointmentFromResultSet(ResultSet rs) throws SQLException {

        // Create DateTimeFormatter object for converting string from DB into LocalDateTime object
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Get strings from SQL query and store them in respective variables
        int appointmentId = rs.getInt("Appointment_ID");
        String title = rs.getString("Title");
        String description = rs.getString("Description");
        String location = rs.getString("Location");
        String type = rs.getString("Type");

        // Convert start and end times to local time zone
        LocalDateTime start = LocalDateTime.parse(rs.getString("Start"), formatter)
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime end = LocalDateTime.parse(rs.getString("End"), formatter)
                .atZone(ZoneId.of("UTC"))
                .withZoneSameInstant(ZoneId.systemDefault())
                .toLocalDateTime();
        LocalDateTime createDate = LocalDateTime.parse(rs.getString("Create_Date"), formatter);
        String createdBy = rs.getString("Created_By");
        LocalDateTime lastUpdate = LocalDateTime.parse(rs.getString("Last_Update"), formatter);
        String lastUpdatedBy = rs.getString("Last_Updated_By");
        int customerId = rs.getInt("Customer_ID");
        int userId = rs.getInt("User_ID");
        int contactId = rs.getInt("Contact_ID");

        // Create and return new appointment object
        return new Appointment(
                appointmentId,
                title,
                description,
                location,
                type,
                start,
                end,
                createDate,
                createdBy,
                lastUpdate,
                lastUpdatedBy,
                customerId,
                userId,
                contactId
        );
    }
}
