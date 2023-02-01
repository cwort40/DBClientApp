package com.c195.dbclientapp;

import com.c195.dbclientapp.helper.JDBC;
import com.c195.dbclientapp.i18n.Resources_fr;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.ZoneId;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The main class that launches the application and manages the database connection.
 */
public class Main extends Application {

    /**
     * Sets up the login scene and displays it.
     *
     * @param stage the primary stage for the application
     * @throws IOException if an error occurs while loading the FXML file
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Locale locale = Locale.getDefault();

        if (locale.getLanguage().equals("fr")) {
            Resources_fr rb = new Resources_fr();
            stage.setTitle(rb.getString("Login"));
        } else {
            stage.setTitle("Login");
        }
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Opens a connection to the database and launches the application.
     * When the application is closed, the database connection is closed.
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {
        JDBC.openConnection();
        launch();
        JDBC.closeConnection();
    }
}