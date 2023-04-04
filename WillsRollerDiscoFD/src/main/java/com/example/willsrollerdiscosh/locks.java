package com.example.willsrollerdiscosh;

import java.sql.*;

    public class locks {
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
                    System.out.println("Run Time Exception (Connection)");
                    ;
                }
                try {
                    Statement statement = connection.createStatement();
                } catch (SQLException e) {
                    System.out.println("Run Time Exception (Create)");
                }
                System.out.println("Lock Table Connected");
            }
        }

        public static void lock(String resourceName, String lockedBy) throws SQLException {
            String query = "UPDATE locks SET lockedBy = ?, lockTime = NOW() WHERE resourceName = ? AND lockedBy IS NULL";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, lockedBy);
            statement.setString(2, resourceName);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException("Failed to acquire lock for resource: " + resourceName);
            }
        }

        public static void unlock(String resourceName, String lockedBy) throws SQLException {
            String query = "UPDATE locks SET lockedBy = NULL, lockTime = NULL WHERE resourceName = ? AND lockedBy = ?";

            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, resourceName);
            statement.setString(2, lockedBy);

            int rowsUpdated = statement.executeUpdate();

            if (rowsUpdated == 0) {
                throw new SQLException("Failed to release lock for resource: " + resourceName);
            }
        }

    }
