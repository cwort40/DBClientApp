module com.c195.dbclientapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.c195.dbclientapp to javafx.fxml;
    opens com.c195.dbclientapp.model to javafx.base;
    exports com.c195.dbclientapp;
}