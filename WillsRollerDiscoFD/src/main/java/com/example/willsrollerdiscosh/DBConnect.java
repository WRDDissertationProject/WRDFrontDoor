package com.example.willsrollerdiscosh;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
        //System.out.println("test");
        Timer reloadSessionChecker = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                Statement statement = null;
                try {
                    statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT COUNT(*) FROM current_session");
                    resultSet.next();
                    int count = resultSet.getInt(1);
                    boolean recordExists;
                    if (count > 0) {
                        sceneSelector.recordExists();
                    } else {
                        sceneSelector.recordDoesNotExist();
                    }
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        reloadSessionChecker.schedule(task, 2000, 2000);

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
}