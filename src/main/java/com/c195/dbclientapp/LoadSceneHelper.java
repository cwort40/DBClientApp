package com.c195.dbclientapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * The LoadSceneHelper class provides a helper method for loading a new scene in the application.
 * It is intended to be used as a utility class for switching between different screens in the application.
 */
public class LoadSceneHelper {

    /**
     *
     * Loads the specified scene in the application.
     * @param event the action event that triggered this method
     * @param fxmlFile the name of the FXML file representing the scene to be loaded
     * @param title the title to be displayed on the window of the scene being loaded
     * @throws IOException if there is an error loading the specified scene
     */
    public static void loadScene(ActionEvent event, String fxmlFile, String title) throws IOException {
        Stage stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        Parent scene = FXMLLoader.load(Objects.requireNonNull(LoadSceneHelper.class.getResource(fxmlFile)));
        stage.setTitle(title);
        stage.setScene(new Scene(scene));
        stage.show();
    }

}
