package com.c195.dbclientapp.helper;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * The JDBC class is responsible for managing the connection to the MySQL database. It contains methods for opening
 * and closing the connection, as well as the necessary constants for constructing the JDBC URL, driver name, and
 * login credentials.
 */
public class JDBC {

    private static final String protocol = "jdbc";
    private static final String vendor = ":mysql:";
    private static final String location = "//localhost/";
    private static final String databaseName = "client_schedule";
    private static final String jdbcUrl = protocol + vendor + location + databaseName +
            "?connectionTimeZone = SERVER"; // LOCAL
    private static final String driver = "com.mysql.cj.jdbc.Driver"; // Driver reference
    private static final String userName = "sqlUser"; // Username
    private static String password = "Passw0rd!"; // Password
    public static Connection connection;  // Connection Interface

    /**
     * Opens a connection to the specified database using the predefined JDBC URL, driver, and login credentials.
     * Prints a message to the console if the connection is successful.
     */
    public static void openConnection()
    {
        try {
            Class.forName(driver); // Locate Driver
            connection = DriverManager.getConnection(jdbcUrl, userName, password); // Reference Connection object
            System.out.println("Connection successful!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }

    /**
     * Opens a connection to the MySQL database specified in the JDBC class. If the connection is successful, a message
     * indicating so will be printed. If the connection fails, an error message with the exception message will be
     * printed.
     */
    public static void closeConnection() {
        try {
            connection.close();
            System.out.println("Connection closed!");
        }
        catch(Exception e)
        {
            System.out.println("Error:" + e.getMessage());
        }
    }
}
