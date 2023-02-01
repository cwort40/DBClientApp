package com.c195.dbclientapp.i18n;

import java.util.ListResourceBundle;

/**
 * This class is a concrete subclass of the abstract {@link Resources} class. It contains a list of
 * key-value pairs for various strings that are used throughout the application and are specific to
 * the English language.
 */
public class Resources_en extends ListResourceBundle {
    /**
     * Returns the contents of this resource bundle.
     * @return an array of key-value pairs containing the resources in this bundle
     */
    protected Object[][] getContents() {
        return new Object[][]{
                {"Login", "Login"},
                {"Username", "Username"},
                {"Password", "Password"},
                {"Location", "Location"},
                {"Submit", "Submit"},
                {"Exit", "Exit"},
                {"Error", "Exit"},
                {"Invalid username or password", "Invalid username or password"},
                {"An error occurred while trying to validate the user: ",
                        "An error occurred while trying to validate the user: "},
                {"No Upcoming Appointments", "No Upcoming Appointments"},
                {"Upcoming Appointment", "Upcoming Appointment"}
        };
    }
}
