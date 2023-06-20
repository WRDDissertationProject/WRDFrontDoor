/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoFD
 *  FILE TITLE: listViews.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   class that contains all the listviews used by the application, seperated for code clarity,
 *   connected to sceneSelector.
 *   */

//PACKAGE
package com.example.willsrollerdiscosh;

//IMPORTS
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import java.sql.SQLException;
import java.util.List;

public class listViews {
    public static void loadAnnouncementsListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadAnnouncement();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }

    public static void loadTicketsListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadTickets();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }

    public static void loadSkateHireListView(ListView lv) throws SQLException {
        ObservableList<Skate> data = sceneSelector.loadSkateHire(lv);
        lv.setItems(data);
    }

    public static void loadTicketEditOrDeleteListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadTicketsEditOrDelete();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);

            lv.setCellFactory(param -> new ListCell<String>() {

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Button editButton = new Button("Edit");
                        Button deleteButton = new Button("Delete");
                        Label ticket = new Label(item);
                        HBox hbox = new HBox();
                        hbox.getChildren().addAll(ticket, editButton, deleteButton);
                        hbox.setSpacing(15);
                        setGraphic(hbox);


                        ticket.setStyle("-fx-text-fill: #bebebe;");

                        editButton.getStyleClass().add("edit-button-style");
                        deleteButton.getStyleClass().add("delete-button-style");

                        // Set the event handlers for the Edit and Delete buttons
                        editButton.setOnAction(event -> {
                            // Call the edit function with the selected item
                            editFunction();
                        });
                        deleteButton.setOnAction(event -> {
                            // Call the delete function with the selected item
                            sceneSelector.deleteFunction(item);
                        });
                    }
                }
            });
        }
    }

    public static void editFunction(){
        System.out.println("Edit Clicked");
    }

    public static void loadMaintenanceListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadMaintenance();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }
}

