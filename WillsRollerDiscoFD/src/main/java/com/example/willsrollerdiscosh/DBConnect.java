package com.example.willsrollerdiscosh;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.*;

public class DBConnect {
    String url = "jdbc:mysql://localhost:3306/wrdDatabase";
    String username = "root";
    String password = "root";

    static Connection connection = null;

    static ResultSet rs;

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
                System.out.println("Run Time Exception (Connection)");;
            }
            try {
                Statement statement = connection.createStatement();
            } catch (SQLException e) {
                System.out.println("Run Time Exception (Create)");
            }
            System.out.println("Database Connected");
        }
    }

    public void sessionStartChecker() throws SQLException {
        Timer reloadSessionChecker = new Timer();
        reloadSessionChecker.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM current_session");
                    resultSet.next();
                    int count = resultSet.getInt(1);
                    boolean recordExists;
                   sceneSelector.checkForRecord(count);

                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                });
            }
        }, 0, 2000);
    }

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

    public static ObservableList<Skate> loadSkates() {
        ObservableList<Skate> data = FXCollections.observableArrayList();

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

        return data;
    }

    public static void updateSkateListMinus(int newSkateAmount, String skateSize) throws SQLException {
        //System.out.println(newSkateAmount + "Test");
        //System.out.println(skateSize + " Test");
        //update value by inserting into database
        Statement stmt = connection.createStatement();
        String sql = "UPDATE current_skates SET skateAmount = " + newSkateAmount + " WHERE skateSize = '" + skateSize + "'";

        stmt.executeUpdate(sql);
        System.out.println("Updated Skate Amount");
    }

    public static List<String> loadTickets() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM tickets");
        List<String> ticketsList = new ArrayList<>();

        while (resultSet.next()) {
            String ticket_details = resultSet.getString("ticket_details");
            String dateCreated = resultSet.getString("ticket_date");
            String timeCreated = resultSet.getString("ticket_time");
            String postedBy = resultSet.getString("staff_id");
            ticketsList.add("Ticket: " + ticket_details + " \nPosted By: " + postedBy + "\n"
                    + "Date: " + dateCreated + " Time: " + timeCreated );
        }
        return ticketsList;
    }

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

    public static void insertTicket(String text) {
        //insert into query
        String ticketDate = dateTime.justDate();
        String ticketTime = dateTime.justTime();
        String postedBy = "Front Door App";
        try {
            Statement stmt = connection.createStatement();
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO tickets(ticket_date, ticket_time, ticket_details, staff_id) VALUES(?, ?, ?, ?)");

            pstmt.setString(1, ticketDate);
            pstmt.setString(2, ticketTime);
            pstmt.setString(3, text);
            pstmt.setString(4, postedBy);

            pstmt.executeUpdate();

            System.out.println("Inserted Into Database");

        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not inserted");
        }
    }

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
            System.out.println(e);
            System.out.println("Ticket not Deleted");
            success = false;
        }
        return success;
    }

    public static List<String> loadMaintenance() throws SQLException {
        Statement stmt = connection.createStatement();
        ResultSet resultSet = stmt.executeQuery("SELECT * FROM maintenance");
        List<String> maintenanceList = new ArrayList<>();

        while (resultSet.next()) {
            String record = resultSet.getString("maintenance_details");
            String type = resultSet.getString("maintenance_type");
            String skateSize = resultSet.getString("skateSize");

            if((skateSize == null) || (skateSize.equals("null"))){
                skateSize = "N/A";
            }
            maintenanceList.add(
                    "Details: " + record + "\nType: " + type + "\nSkate Size: " +skateSize);
        }
        return maintenanceList;
    }

    public static void insertMaintenance(String typeIn, String details, String skateSizeIn) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "INSERT INTO maintenance(maintenance_type, maintenance_details, skateSize)" +
                    " VALUES('" + typeIn + "', '" + details + "', '" + skateSizeIn + "')";
            stmt.executeUpdate(sql);
            System.out.println("Inserted Into Database");

            if (skateSizeIn != null) {
                int currentAmount = fetchSkateSizeCurrent(skateSizeIn);
                int newAmount = currentAmount - 1;

                int inventoryAmount = fetchSkateSizeAmount(skateSizeIn);
                int newInventoryAmount = inventoryAmount - 1;
                DBConnect.updateSkateSizeAmount(skateSizeIn, newAmount);
                DBConnect.updateSkateInventory(skateSizeIn, newInventoryAmount);
            }
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Announcement not inserted");
        }
    }

    public static void updateSkateSizeAmount(String skateSize, int newAmount) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE current_skates SET skateAmount = '" + newAmount + "' WHERE skateSize = '" + skateSize + "'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Skate inventory not updated");
        }
    }

    public static void updateSkateInventory(String skateSize, int newAmount) {
        try {
            Statement stmt = connection.createStatement();
            String sql = "UPDATE skate_inventory SET skateAmount = " + newAmount + " WHERE skateSize = '" + skateSize + "'";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Skate inventory not updated");
        }
    }

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



}