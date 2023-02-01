package com.c195.dbclientapp.i18n;

import java.util.ListResourceBundle;

/**
 * This abstract class extends the ListResourceBundle class and is used to store string values for use in the
 * application. It provides a protected method, getContents, which returns an array of key-value pairs containing the
 * string values.
 */
public abstract class Resources extends ListResourceBundle {

    /*
    Returns an array of key-value pairs containing the string values.
    @return an array of key-value pairs containing the string values
    */
    protected Object[][] getContents() {
        return new Object[][] {
                {"Login", "Login"},
                {"Username", "Username"},
                {"Password", "Password"},
                {"Location", "Location"},
                {"Submit", "Submit"},
                {"Exit", "Exit"},
                {"Error", "Error"},
                {"Invalid username or password", "Invalid username or password"},
                {"An error occurred while trying to validate the user: ",
                        "An error occurred while trying to validate the user: "},
                {"No Upcoming Appointments", "No Upcoming Appointments"},
                {"Upcoming Appointment", "Upcoming Appointment"}
        };
    }
}

