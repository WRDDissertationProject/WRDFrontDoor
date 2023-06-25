/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoFD
 *  FILE TITLE: reloader.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Methods that automatically reloads other methods, used for live updates within the application.
 *   */

//PACKAGE
package com.example.willsrollerdiscosh;

//IMPORTS
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

/*Resources Used:
 * Reloaders using Timers:
 * pcalcao (2012) How to use Timer class to call a method, do something, reset time, repeat?. StackOverflow. [Online]
 * Available From: https://stackoverflow.com/questions/9413656/how-to-use-timer-class-to-call-a-method-do-something-reset-timer-repeat
 * [Accessed From 09/03/2023].
 *
 * GeeksForGeeks (2023) Java.util.Time Class in Java. GeeksForGeeks [Online]. Available From:
 * https://www.geeksforgeeks.org/java-util-timer-class-java/ [Accessed From 09/03/2023].
 *
 * Run Laters:
 * invariant (2012) Platform.runLater and Task in JavaFX. StackOverflow [Online]. Available From:
 * https://stackoverflow.com/questions/13784333/platform-runlater-and-task-in-javafx [Accessed From 31/03/2023]
 *
 * jewelsea (2013) How do I safely modify JavaFX GUI nodes from my own Thread?. StackOverflow [Online]. Available From:
 * https://stackoverflow.com/questions/19945422/how-do-i-safely-modify-javafx-gui-nodes-from-my-own-thread
 * [Accessed From 31/03/2023]
 * */

//Used to Reload all the list views, most methods follow the same layout
//Run laters are used due to JavaFX issues when trying to run all at the same time
public class reloader {
    //Skate Hire
    public static void SkateHireReloader(Scene scene) {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Linking the ListView to the Scene
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHListView");
                        if (lv != null) {
                            //If the list view is on the scene, then load the ListView contents
                            listViews.loadSkateHireListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 800); // reload every 0.8 Seconds

    }

    //Session start reloader
    public static void SesssionStartReloader() {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    //Continuous checks of whether a session is ongoing
                    sceneSelector.sessionStatus();
                });
            }
        }, 0, 800); // reload every 0.8 Seconds

    }
    //Tickets
    public static void TicketsReloader(Scene scene) {
        Timer reloadTickets = new Timer();
        reloadTickets.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    //Linking the ListView to the Scene
                    ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListView");
                    if (lv != null) {
                        try {
                            //If the list view is on the scene, then load the ListView contents
                            listViews.loadTicketsListView(lv);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }, 0, 800); // reload every 0.8 Seconds
    }

    //Edit or Delete Tickets Reloader
    public static void EditOrDeleteTicketsReloader(Scene scene) {
        Timer reloadTickets = new Timer();
        reloadTickets.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    //Linking the ListView to the Scene
                    ListView<String> lv = (ListView<String>) scene.lookup("#CTListViewEditOrDelete");
                    if (lv != null) {
                        try {
                            //If the list view is on the scene, then load the ListView contents
                            listViews.loadTicketEditOrDeleteListView(lv);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
            }
        }, 0, 800); // reload every 0.8 Seconds
    }

    //Maintenance
    public static void maintenanceReloader(Scene scene) {
        Timer maintenanceTimer = new Timer();
        maintenanceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        //Linking the ListView to the Scene
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#MListView");
                        if (lv != null) {
                            //If the list view is on the scene, then load the ListView contents
                            listViews.loadMaintenanceListView(lv);
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second

    }
}
