package com.example.willsrollerdiscosh;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("announcements.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 420, 600);
        stage.getIcons().add(new Image(getClass().getResourceAsStream("icon.png")));
        stage.setTitle("Wills Roller Disco - Front Door");
        stage.setScene(scene);
        stage.show();


        DBConnect connect = new DBConnect();
        connect.connect();

        Timer reloadAnnouncements = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                try {
                    List<String> announcementsData = DBConnect.loadAnnouncement();
                    ObservableList<String> items = FXCollections.observableArrayList(announcementsData);
                    ListView lv = (ListView) scene.lookup("#announcementsList");
                    lv.setItems(items);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        };

        // Schedule the task to run every 2 seconds
        reloadAnnouncements.schedule(task, 0, 2000);
    }

    public static void main(String[] args) throws SQLException {
        DBConnect connect = new DBConnect();
        connect.connect();
        connect.sessionStartChecker();

        launch();
    }
}