# Customer Management and Scheduling System
This application is a database management system for a customer scheduling system.
It contains classes that access and manipulate data in a database, including retrieving
customer and appointment data, adding and deleting customers and appointments, and
generating reports. It also includes classes for handling user login and user management.
The application allows users to view, add, and delete customer and appointment
information, and to generate reports on various aspects of the scheduling data.

## Author information
<b>Author:</b> Christian Worthen <br>
<b>Email:</b> cwort40@wgu.edu <br>
<b>Application Version:</b> 1.0 <br>
<b>Date:</b> 01/02/2023

## Version Information
<b>IDE:</b> Intellij Community Edition 2022.2.3 <br>
<b>JDK:</b> OpenJDK 19 <br>
<b>JavaFX:</b> OpenJFX 19 <br>
<b>MySQL Connector Driver: </b> mysql-connector-java-8.0.30

## Instructions on How to Run the Program
1. Launch the application by running the main method in the App class. <br>
2. A login screen will appear. Enter your username and password to log in. <br>
3. After logging in, the main menu screen will appear. From here, you can access various
features of the application, such as adding or deleting customers, managing appointments,
and viewing reports. <br>
4. To add a customer, click the "Add Customer" button on the main menu and enter the
required information in the form that appears. Click the "Submit" button to save the new
customer to the database. <br>
5. To delete a customer, click the "Delete Customer" button on the main menu and select
the customer you wish to delete from the list. Click the "Delete" button to remove the
customer from the database. <br>
6. To manage appointments, click the "Appointments" button on the main menu. From here,
you can view, add, or cancel appointments. <br>
7. To view a report, click the "Reports" button on the main menu and view the various
types of reports you wish to see. The reports are separated by tabs. <br>
8. To exit, click the "Exit" button on the main menu. This will exit the application.

## Additional Report
The CustomerReport class has two fields, customerId and countryId, and the
getAllCustomerReports method in the ReportAccess class returns an ObservableList of
CustomerReport objects.
<br>
The purpose of this table is to be able to associate each customer with a particular
country, based on the countryId field in the first_level_divisions table, which is joined
with the customers table using the Division_ID field. This information could be used to
generate reports or analyze customer data based on their location.