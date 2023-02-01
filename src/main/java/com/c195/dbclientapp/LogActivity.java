package com.c195.dbclientapp;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * This class represents the log activity of successful and failed logins in the system. It writes the log message to
 * a file with the current date and time in UTC.
 */
public class LogActivity {

    /**
     * Logs a successful login attempt for the given username.
     * The log message includes the current date and time in UTC and the username of the user who logged in.
     * The log message is written to the "login_activity.txt" file.
     * @param username the username of the user who logged in
     */
    public static void logSuccessfulLogin(String username) {
        // Get the current date and time
        LocalDateTime currentTime = LocalDateTime.now();
        // Convert to UTC
        currentTime = currentTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
        String dateTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));


        // Write the log message to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("login_activity.txt", true))) {
            writer.write(dateTime + " - SUCCESSFUL login for user: " + username);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs a failed login attempt for the given username.
     * @param username the username of the user who attempted to login
     */
    public static void logFailedLogin(String username) {
        // Get the current date and time
        LocalDateTime currentTime = LocalDateTime.now();
        // Convert to UTC
        currentTime = currentTime.atZone(ZoneId.systemDefault()).withZoneSameInstant(ZoneId.of("UTC"))
                .toLocalDateTime();
        String dateTime = currentTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        // Write the log message to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("login_activity.txt", true))) {
            writer.write(dateTime + " - FAILED login for user: " + username);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
