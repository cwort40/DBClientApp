# Customer Management and Scheduling System
This software is designed to manage a customer scheduling system through a database management system. 
It provides classes to access and manipulate data stored in a database, including functions to retrieve, 
add, and delete customer and appointment information, as well as generate reports. Additionally, 
it includes features for user login and management. Users can use the application to view, add, 
and delete customer and appointment data, as well as generate reports on various aspects of the scheduling data.

## Version Information
<b>IDE:</b> Intellij Community Edition 2022.2.3 <br>
<b>JDK:</b> OpenJDK 19 <br>
<b>JavaFX:</b> OpenJFX 19 <br>
<b>MySQL Connector Driver: </b> mysql-connector-java-8.0.30

## Instructions on How to Run the Program
1. Start the application by executing the main method located in the App class. <br>

2. Upon launching, a login screen will appear. Enter your username and password to gain access. <br>

3. Once you have successfully logged in, you will be taken to the main menu screen where you can 
explore the different features of the application such as managing customers, appointments, and generating reports. <br>

4. To add a customer, navigate to the main menu and click on the "Add Customer" button. Fill in the required information 
in the form that appears, then click "Submit" to save the new customer to the database. <br>

5. To delete a customer, go to the main menu and click on the "Delete Customer" button. Choose the customer you want to 
delete from the list and click "Delete" to remove the selected customer from the database. <br>

6. To manage appointments, click on the "Appointments" button from the main menu. This will give you access to 
view, add, or cancel appointments. <br>

7. To view a report, go to the main menu and click on the "Reports" button. You will be able to view different 
types of reports separated by tabs. <br>

8. To exit the application, click on the "Exit" button on the main menu. This will close the application.

## Additional Report
he CustomerReport class comprises two fields, customerId and countryId, while the ReportAccess class's 
getAllCustomerReports method returns an ObservableList containing CustomerReport objects.
<br>
The table's aim is to link each customer to a specific country using the countryId field in the 
first_level_divisions table, which is combined with the customers table using the Division_ID field. 
FThis data can be utilized to produce reports or analyze customer information based on their location.
