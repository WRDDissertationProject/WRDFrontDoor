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

public class reloader {
    public static void SkateHireReloader(Scene scene) {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#SHListView");
                        if (lv != null) {
                            listViews.loadSkateHireListView(lv);
                            System.out.println("Skate Hire Reload");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 800); // reload every second

    }

    public static void SesssionStartReloader() {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    System.out.println("Session Start Reload");
                    sceneSelector.sessionStatus();
                });
            }
        }, 0, 800); // reload every second

    }

    public static void TicketsReloader(Scene scene) {
        Timer reloadTickets = new Timer();
        reloadTickets.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListView");
                    if (lv != null) {
                        try {
                            listViews.loadTicketsListView(lv);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Ticket Reload");
                    }
                });
            }
        }, 0, 800);

    }

    public static void EditOrDeleteTicketsReloader(Scene scene) {
        Timer reloadTickets = new Timer();
        reloadTickets.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    ListView<String> lv = (ListView<String>) scene.lookup("#CTListViewEditOrDelete");
                    if (lv != null) {
                        try {
                            listViews.loadTicketEditOrDeleteListView(lv);
                        } catch (SQLException e) {
                            throw new RuntimeException(e);
                        }
                        System.out.println("Ticket Reload");
                    }
                });
            }
        }, 0, 800);

    }

    public static void maintenanceReloader(Scene scene) {
        Timer maintenanceTimer = new Timer();
        maintenanceTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        ListView<Skate> lv = (ListView<Skate>) scene.lookup("#MListView");
                        if (lv != null) {
                            listViews.loadMaintenanceListView(lv);
                            System.out.println("Maintenance Reload");
                        }
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 1000); // reload every second

    }
}
