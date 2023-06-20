/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoFD
 *  FILE TITLE: HelloApplication.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Application starting point, launches application and also connects to database
 *   */

//PACKAGE
package com.example.willsrollerdiscosh;

//IMPORTS
import javafx.application.Application;
import javafx.application.Platform;
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
        reloadAnnouncements.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        List<String> announcementsData = DBConnect.loadAnnouncement();
                        ObservableList<String> items = FXCollections.observableArrayList(announcementsData);
                        ListView lv = (ListView) scene.lookup("#announcementsList");
                        listViews.loadAnnouncementsListView(lv);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000);
        stage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }

    public static void main(String[] args) {
        DBConnect connect = new DBConnect();
        connect.connect();
        connect.sessionStartChecker();

        locks locks = new locks();
        locks.connect();

        launch();
    }
}