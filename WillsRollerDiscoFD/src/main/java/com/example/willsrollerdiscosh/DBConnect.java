/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoFD
 *  FILE TITLE: DBConnect.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *     All the code that relates to database manipulation, including the database connection as well as creates, inserts
 *     ,updates and deletes.
 *   */

//PACKAGE
package com.example.willsrollerdiscosh;

//IMPORTS
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/*Resources Used:
 *Java Loggers:
 *Kappagantula,S (2022) What is logger in Java and why do you use it?. Edureka [Online] Available From:
 *https://www.edureka.co/blog/logger-in-java#:~:text=The%20process%20of%20creating%20a,takes%20string%20as%20a%20parameter.
 *[Accessed 22/06/2023].
 *
 * Bhattacharjee, D. (2023) System.out.println vs Loffers. Baeldung [Online] Available From:
 * https://www.baeldung.com/java-system-out-println-vs-loggers [Accessed 22/06/2023].
 *
 *Connecting to Docker Hosted Database
 * Database Star (2022) How to Set Up MySQL Database With Docker. YouTube [Online]. Available From:
 * https://www.youtube.com/watch?v=kphq2TsVRIs&ab_channel=DatabaseStar [Accessed 23/06/2023].
 *
 * Olson, P. Lischke, M. (2014) Importing Data and Schema to MySQL Workbench. stackOverFlow [Online] Available From:
 * https://stackoverflow.com/questions/26260235/importing-data-and-schema-to-mysql-workbench [Accessed 23/06/2023].
 *
 * Observable Lists
 * Leonardo (2023) JavaFX - List View with an Image Button. StackOverflow [Online] Available From:
 * https://stackoverflow.com/questions/15661500/javafx-listview-item-with-an-image-button [Accessed 25/02/2023].
 *
 * Tom G (2021) Linking the TableView and Observable List. YouTube [Online]. Available From:
 * https://www.youtube.com/watch?v=BYj2NBjiLDY&ab_channel=TomG [Accessed 25/02/2023].
 *
 * Sarah Szabo (2014) TableView with an Embedded ListView. StackOverflow [Online] Available From:
 * https://stackoverflow.com/questions/25150915/tableview-with-an-embedded-listview [Accessed 25/02/2023].
 *
 * Locks and Database Concurrency
 * Denny Sam (2022) Locking in Databases and Isolation Mechanisms. Medium [Online] Available From:
 * https://medium.com/inspiredbrilliance/what-are-database-locks-1aff9117c290 [Accessed From: 04/04/2023].
 *
 * Ambler, S. (2023) Introduction to Database Concurrency Control. Agile Date [Online] Available From:
 * http://agiledata.org/essays/concurrencyControl.html [Accessed From: 04/04/2023].
 * */

public class DBConnect {
    //Defining a Logger, used for error recording
    private static final Logger log = Logger.getLogger(String.valueOf(DBConnect.class));

    /******************************************************
     Local Testing Database Connections

     String url = "jdbc:mysql://localhost:3306/wrdDatabase";
     String username = "root";
     String password = "root";
     ********************************************************/

    /*Docker Database Connections
     * Using Local host currently due to firewall issues with University Connections*/
    String url = "jdbc:mysql://localhost:3307/wrddatabase";
    //String url = "jdbc:mysql://172.17.0.2:3307/wrddatabase";
    String username = "root";
    String password = "my-secret-pw";
    static Connection connection = null;
    static ResultSet rs;

    //When a skate size is at 0 and tried to be hired, then the value is added to the needed skates table
    public static void neededSkates(String skateSize) {
        String date = dateTime.justDate();
        //Testing Prints
        System.out.println(skateSize);
        System.out.println(date);

        try {
            //Try to insert the recieved values into the database
            Statement stmt = connection.createStatement();
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO needed_skates(skateSize, dateNeeded) VALUES(?, ?)");

            pstmt.setString(1, skateSize);
            pstmt.setString(2, date);
            pstmt.executeUpdate();

            //Testing Print
            System.out.println("Inserted Into Database");

        } catch (SQLException e) {
            log.log(Level.SEVERE,"Error Within Skates Needed", e);
        }

    }
    //Used to check if there is a current session in place
    public static boolean checkForSessionStart() throws SQLException {
        String query = "SELECT * FROM current_session";
        Statement stmt = connection.createStatement();
        ResultSet rs = stmt.executeQuery(query);
        return rs.next();
    }
    //Gets the session start time, uses the one saved to the current session rather than creating new
    //Prevents app incompatibility issues by ensuring the start time is consistent.
    public static String getSessionStartTime() {
        String query = "SELECT current_dateTime FROM current_session";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            if (rs.next()) {
                return rs.getString("current_dateTime");
            } else {
                return "Session not started";
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    //code for starting a database connection
    //various tries and catches are used to account for common issues and debugging a broken connection
    public void connect() {
        {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                System.out.println("Class Not Found");
            }
            try {
                connection = DriverManager.getConnection(url, username, password);
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Connection)");
            }
            try {
                Statement statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Create)");
            }
            System.out.println("Database Connected");
        }
    }

    //Checks whether a session has started
    //Uses a reloader built in
    //Counts the rows, if the row has any data (above 0) a session has started
    public void sessionStartChecker() {
        Timer reloadSessionChecker = new Timer();
        reloadSessionChecker.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                Statement statement;
                try {
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM current_session");
                    resultSet.next();
                    int count = resultSet.getInt(1);
                   sceneSelector.checkForRecord(count);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                });
            }
        }, 0, 2000); //Loaded every two seconds
    }

    //Creates a list that defines the layouts for announcements
    //Used for announcements list view
    public static List<String> loadAnnouncement() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT announcement_details FROM announcements");
        List<String> announcementsList = new ArrayList<>();

        while (resultSet.next()) {
            String announcement = resultSet.getString("announcement_details");
            announcementsList.add(announcement);
        }
        return announcementsList;
    }

    //List that fetches all the skate sizes and amount and sets them in a layout for use in a list view
    public static ObservableList<Skate> loadSkates() {
        //Defining List
        ObservableList<Skate> data = FXCollections.observableArrayList();

        //Trying to connect to the database
        //If connected, query is executed
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT skateSize, skateAmount FROM current_skates");

            // iterate through the result set and add Skate objects to the ObservableList
            while (rs.next()) {
                String size = rs.getString("skateSize");
                int amount = rs.getInt("skateAmount");
                data.add(new Skate(size, amount));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        //returns results in layout needed for list view
        return data;
    }

    //Updates the skate amount with a new value, used when skates are hired out
    public static void updateSkateListMinus(int newSkateAmount, String skateSize) throws SQLException {
        //update value by inserting into database
        Statement stmt = connection.createStatement();
        String sql = "UPDATE current_skates SET skateAmount = " + newSkateAmount + " WHERE skateSize = '" + skateSize + "'";
        stmt.executeUpdate(sql);
        //Testing statement
        System.out.println("Updated Skate Amount");
    }

    //Adds skates to the analytics table, used by the business management system.
    //When skates are hired, 1 pair is incremented
    public static void updateSkatesAnalytics(String skateSize) throws SQLException {
        int newSkateAmount;
        // Fetch current skateAmount from current_skates_analytics
        String query = "SELECT skateAmount FROM current_skates_analytics WHERE skateSize = ?";
        PreparedStatement stmt = connection.prepareStatement(query);
        stmt.setString(1, skateSize);
        ResultSet fetchRs = stmt.executeQuery();

        //Gets the skate amount based on the size in the earlier query
        //Adds one to the skate count
        if (fetchRs.next()) {
            int currentSkateAmount = fetchRs.getInt("skateAmount");
            newSkateAmount = currentSkateAmount + 1;
        } else {
            // Handle case when skateSize is not found in the table
            System.out.println("SkateSize not found in current_skates_analytics");
            return;
        }
        // Update value by inserting into database
        String updateQuery = "UPDATE current_skates_analytics SET skateAmount = ? WHERE skateSize = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
        updateStmt.setInt(1, newSkateAmount);
        updateStmt.setString(2, skateSize);
        updateStmt.executeUpdate();
        System.out.println("Updated Skate Analytics");
    }

    //Used to fetch all tickets currently stored
    public static List<String> loadTickets() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM tickets");
        List<String> ticketsList = new ArrayList<>();

        //while there are values, get them and store them as strings
        while (resultSet.next()) {
            //Sets all these values in a layout for each row in a list view
            String ticket_details = resultSet.getString("ticket_details");
            String dateCreated = resultSet.getString("ticket_date");
            String timeCreated = resultSet.getString("ticket_time");
            String postedBy = resultSet.getString("staff_id");
            ticketsList.add("Ticket: " + ticket_details + " \nPosted By: " + postedBy + "\n"
                    + "Date: " + dateCreated + " Time: " + timeCreated );
        }
        return ticketsList;
    }

    //Used for the edit and delete of the tickets
    public static List<String> loadTicketsEditOrDelete() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM tickets");
        List<String> ticketsList = new ArrayList<>();

        while (resultSet.next()) {
            String ticket_details = resultSet.getString("ticket_details");
            ticketsList.add("Ticket: " + ticket_details);
        }
        return ticketsList;
    }

    //used to insert the users input as a new ticket
    public static void insertTicket(String text) {
        //Creates a date and time object for the new ticket
        String ticketDate = dateTime.justDate();
        String ticketTime = dateTime.justTime();
        //Sets the posted by
        String postedBy = "Front Door App";

        //tries to insert the above as a new tickets
        try {
            Statement stmt = connection.createStatement();
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO tickets(ticket_date, ticket_time, ticket_details, staff_id) VALUES(?, ?, ?, ?)");

            pstmt.setString(1, ticketDate);
            pstmt.setString(2, ticketTime);
            pstmt.setString(3, text);
            pstmt.setString(4, postedBy);
            pstmt.executeUpdate();
            //developer print statement to confirm ticket creation
            System.out.println("Inserted Into Database");

        } catch (SQLException e) {
            log.log(Level.SEVERE,"Ticket not inserted", e);
        }
    }

    //removes the ticket where the details match the value recieved from the parent class
    public static boolean deleteTicket(String value){
        boolean success;
        try {
            Statement stmt = connection.createStatement();
            String query = "DELETE FROM tickets WHERE ticket_details = '" + value + "'";
            System.out.println(query);
            stmt.executeUpdate(query);
            success = true;
        }
        catch (SQLException e) {
            log.log(Level.SEVERE,"Ticket could not be deleted", e);
            success = false;
        }
        return success;
    }

    //used to fetch all the maintenance records currently stored
    public static List<String> loadMaintenance() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM maintenance");
        List<String> maintenanceList = new ArrayList<>();

        //while there are values, get them and store them as strings
        while (resultSet.next()) {
            String record = resultSet.getString("maintenance_details");
            String type = resultSet.getString("maintenance_type");
            String skateSize = resultSet.getString("skateSize");

            //If there is no skate size variable, then set the display as N/A for the list view
            if((skateSize == null) || (skateSize.equals("null"))){
                skateSize = "N/A";
            }
            //Sets all these values in a layout for each row in a list view
            maintenanceList.add(
                    "Details: " + record + "\nType: " + type + "\nSkate Size: " +skateSize);
        }
        return maintenanceList;
    }

    //Inserts the user input into the maintenance database
    //Receives type, details of issues, and the skate size
    public static void insertMaintenance(String typeIn, String details, String skateSizeIn) {
        //Trying to connect to the database
        //If connected, query is executed and values are inserted
        //If successful statement is printed to the console
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO maintenance(maintenance_type, maintenance_details, skateSize)" +
                    " VALUES('" + typeIn + "', '" + details + "', '" + skateSizeIn + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

            //if a skate size is entered, skate sizes are fetched
            //Amount of inventory and current is reduced by 1 so data tables match
            //New value is passed and skate records are updated so pair cannot be hired out
            if (skateSizeIn != null) {
                int currentAmount = fetchSkateSizeCurrent(skateSizeIn);
                int newAmount = currentAmount - 1;

                int inventoryAmount = fetchSkateSizeAmount(skateSizeIn);
                int newInventoryAmount = inventoryAmount - 1;
                DBConnect.updateSkateSizeAmount(skateSizeIn, newAmount);
                DBConnect.updateSkateInventory(skateSizeIn, newInventoryAmount);
            }
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Announcement not inserted", e);
        }
    }

    //Used to set the skates to a new value, receives the value and size from parent call
    public static void updateSkateSizeAmount(String skateSize, int newAmount) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE current_skates SET skateAmount = '" + newAmount + "' WHERE skateSize = '" + skateSize + "'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Skate Inventory Not Updated", e);
        }
    }

    //Updates to the skate inventory value, very similar to previous function
    //on a code review could be merged together by adding a table name value and having one function.
    public static void updateSkateInventory(String skateSize, int newAmount) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE skate_inventory SET skateAmount = " + newAmount + " WHERE skateSize = '" + skateSize + "'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            log.log(Level.SEVERE,"Skate inventory not updated", e);
        }
    }

    //Used to fetch the amount of skates for a certain size from the current skates table
    public static int fetchSkateSizeCurrent(String skateSize) {
        int value = 0;
        String query = "SELECT skateAmount FROM skate_inventory WHERE skateSize = ?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, skateSize);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                value = resultSet.getInt("skateAmount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return value;
    }

    //Used to fetch the amount of skates for a size inputted by the parent function
    //retrieves only the amount for that specific skate size
    public static int fetchSkateSizeAmount(String skateSize) {
        int value = 0;
        String query = "SELECT skateAmount FROM current_skates WHERE skateSize = '" + skateSize + "'";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            if (resultSet.next()) {
                value = resultSet.getInt("skateAmount");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return value;
    }

    //Generic Class, adds a skater admission based on the button clicked
    //No locks used as no other application can add admissions
    public static void addSkaterAdmission(String skaterType, Boolean memberBoolean, Double admissionFee) throws SQLException {

        //fetch current session
        String currentSessionQuery = "SELECT * FROM current_session";
        Statement selectStmt = connection.createStatement();
        ResultSet rs = selectStmt.executeQuery(currentSessionQuery);

        //setting all values as 0
        //used if the value isn't passed due to an error
        int ownSkaters = 0;
        int hireSkaters = 0;
        int spectators = 0;
        int members = 0;

        double totalProfit = 0.00;
        double profitOwnSkaters = 0.00;
        double profitHireSkaters = 0.00;

        String currentSession = " ";
        while (rs.next()) {
            currentSession = rs.getString("current_dateTime");
            ownSkaters = rs.getInt("Current_Own_skaters");
            hireSkaters = rs.getInt("Current_Hire_skaters");
            spectators = rs.getInt("Current_Spectators");
            members = rs.getInt("Current_Membership_cards_used");
            profitOwnSkaters = rs.getDouble("Current_Admission_profit_own_skaters");
            profitHireSkaters = rs.getDouble("Current_Admission_profit_hire_skaters");
            totalProfit = rs.getDouble("Current_Admission_profit_total");
        }

        //Depending on the type of skater entered, the admissions counter is increased as well as the profit added.
        switch (skaterType) {
            case "ownSkates" -> {
                ownSkaters = ownSkaters + 1;
                profitOwnSkaters = profitOwnSkaters + admissionFee;
            }
            case "skateHire" -> {
                hireSkaters = hireSkaters + 1;
                profitHireSkaters = profitHireSkaters + admissionFee;
            }
            case "spectator" -> spectators = spectators + 1;
            default -> errors.noSkaterType().show();
        }

        //If there is a member variable passed then members are incremented in count
        if (memberBoolean) {
            members = members + 1;
        }

        //get total profit database
        totalProfit = totalProfit + admissionFee;

        //Updates the database after the changes are made
        String updateQuery = "UPDATE current_session SET Current_Own_skaters = ?, Current_Hire_skaters = ?, Current_Spectators = ?, Current_Membership_cards_used = ?, Current_Admission_profit_own_skaters = ?, Current_Admission_profit_hire_skaters = ?, Current_Admission_profit_total = ? WHERE current_dateTime = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
        updateStmt.setInt(1, ownSkaters);
        updateStmt.setInt(2, hireSkaters);
        updateStmt.setInt(3, spectators);
        updateStmt.setInt(4, members);
        updateStmt.setDouble(5, profitOwnSkaters);
        updateStmt.setDouble(6, profitHireSkaters);
        updateStmt.setDouble(7, totalProfit);
        updateStmt.setString(8, currentSession);
        updateStmt.executeUpdate();
    }

    //Generic function that adds any extra purchase to the relevant database for analytics
    public static void addExtraPurchase(Double purchaseCost) throws SQLException {
        //fetch current session
        String currentSessionQuery = "SELECT * FROM current_session";
        Statement selectStmt = connection.createStatement();
        ResultSet rs = selectStmt.executeQuery(currentSessionQuery);

        //default sets
        int extrasSoldAmountFetch = 0;
        double extrasSoldTotalFetch =0.00;
        String currentSession = " ";

        while (rs.next()) {
            currentSession = rs.getString("current_dateTime");
            extrasSoldAmountFetch = rs.getInt("Current_Extras_sold_amount");
            extrasSoldTotalFetch = rs.getDouble("Current_Extras_sold_total");
        }

        //adding an extra to the sales, based on the type passed by the user
        //Also adds the price to the total costs
        int extrasSoldAmount = extrasSoldAmountFetch + 1;
        double extrasSoldTotal = extrasSoldTotalFetch + purchaseCost;

        //updates the current database with the new values
        String updateQuery = "UPDATE current_session SET Current_Extras_sold_amount = ?, " +
                "Current_Extras_sold_total = ? WHERE current_dateTime = ?";
        PreparedStatement updateStmt = connection.prepareStatement(updateQuery);
        updateStmt.setInt(1, extrasSoldAmount);
        updateStmt.setDouble(2, extrasSoldTotal);
        updateStmt.setString(3, currentSession);
        updateStmt.executeUpdate();
    }
    //Adds all transactions into the transaction history table
    //Used by the Business Management System
    public static void addTransaction(String session, String type, String time, Double value) throws SQLException {
        System.out.println("Transaction Added Test");
        Statement stmt = connection.createStatement();
        String sql = "INSERT INTO transaction_history(session_dateTime, transaction_type, transaction_time, " +
                "transaction_value)" +
                    " VALUES('" + session + "', '" + type + "', '" + time + "', '" + value + "')";
        stmt.executeUpdate(sql);
        System.out.println("Inserted Into Database");
    }
}