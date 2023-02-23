package com.c195.dbclientapp;

import com.c195.dbclientapp.database.AppointmentAccess;
import com.c195.dbclientapp.database.ReportAccess;
import com.c195.dbclientapp.model.Appointment;
import com.c195.dbclientapp.model.CustomerReport;
import com.c195.dbclientapp.model.MonthReport;
import com.c195.dbclientapp.model.TypeReport;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * Controller class for the reports page of the application.
 */
public class ReportsController implements Initializable {

    // Declare all the FXML Controls
    @FXML
    private TableColumn<Appointment, Integer> appointmentIdCol;
    @FXML
    private TableView<Appointment> appointmentTableView;
    @FXML
    private TableColumn<Appointment, String> contactIdCol;
    @FXML
    private TableColumn<Appointment, Integer> customerIdCol;
    @FXML
    private TableColumn<Appointment, String> descriptionCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> endCol;
    @FXML
    private TableColumn<Appointment, LocalDateTime> startCol;
    @FXML
    private TableColumn<Appointment, String> titleCol;
    @FXML
    private TableColumn<Appointment, String> typeCol;
    @FXML
    private TableColumn<TypeReport, Integer> totalTypeCol;
    @FXML
    private TableColumn<TypeReport, String> typeReportCol;
    @FXML
    private TableView<TypeReport> typeTableView;
    @FXML
    private TableColumn<MonthReport, Integer> totalMonthCol;
    @FXML
    private TableColumn<MonthReport, String> monthCol;
    @FXML
    private TableView<MonthReport> monthTableView;
    @FXML
    private TableView<CustomerReport> CustomerReportTableView;
    @FXML
    private TableColumn<CustomerReport, Integer> customerReportCol;
    @FXML
    private TableColumn<CustomerReport, Integer> countryReportCol;

    /**
     * Returns to the main menu screen
     * @param event
     * @throws java.io.IOException
     */
    @FXML
    void OnActionReturn(ActionEvent event) throws java.io.IOException {
        LoadSceneHelper.loadScene(event, "mainMenu.fxml", "Main Menu");
    }

    /**
     *
     * Initializes the UI components and loads data into the tables.
     * @param url the URL of the FXML file
     * @param resourceBundle the resource bundle containing localized strings
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            // Populate appointment table view
            ObservableList<Appointment> appointments = getAllAppointments();
            setAppointmentTableColumns();
            appointmentTableView.setItems(appointments);

            // Populate type report table view
            ObservableList<TypeReport> typeReports = getAllTypeReports();
            setTypeReportTableColumns();
            typeTableView.setItems(typeReports);

            // Populate month report table view
            ObservableList<MonthReport> monthReports = getAllMonthReports();
            setMonthReportTableColumns();
            monthTableView.setItems(monthReports);

            // Populate customer report table view
            ObservableList<CustomerReport> customerReports = getAllCustomerReports();
            setCustomerReportTableColumns();
            CustomerReportTableView.setItems(customerReports);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ObservableList<Appointment> getAllAppointments() throws SQLException {
        return AppointmentAccess.getAllAppointments();
    }

    private void setAppointmentTableColumns() {
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        titleCol.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        startCol.setCellValueFactory(new PropertyValueFactory<>("start"));
        endCol.setCellValueFactory(new PropertyValueFactory<>("end"));
        customerIdCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        contactIdCol.setCellValueFactory(new PropertyValueFactory<>("contactId"));
    }

    private ObservableList<TypeReport> getAllTypeReports() throws SQLException {
        return ReportAccess.getAllTypeReports();
    }

    private void setTypeReportTableColumns() {
        typeReportCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        totalTypeCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private ObservableList<MonthReport> getAllMonthReports() throws SQLException {
        return ReportAccess.getAllMonthReports();
    }

    private void setMonthReportTableColumns() {
        monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
        totalMonthCol.setCellValueFactory(new PropertyValueFactory<>("total"));
    }

    private ObservableList<CustomerReport> getAllCustomerReports() throws SQLException {
        return ReportAccess.getAllCustomerReports();
    }

    private void setCustomerReportTableColumns() {
        customerReportCol.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        countryReportCol.setCellValueFactory(new PropertyValueFactory<>("countryId"));
    }

}
