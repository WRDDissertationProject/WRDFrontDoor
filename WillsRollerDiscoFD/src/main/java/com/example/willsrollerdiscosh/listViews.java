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

/*Resources Used:
 * List Views:  */

//All classes work the same in this method, if the listView is empty then related values are fetched from the database
//Then those values are added to the list view in layout defined in their database method.
public class listViews {
    //Announcements
    public static void loadAnnouncementsListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadAnnouncement();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }
    //Tickets
    public static void loadTicketsListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadTickets();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }
    //Skate Hire
    public static void loadSkateHireListView(ListView lv) throws SQLException {
        ObservableList<Skate> data = sceneSelector.loadSkateHire(lv);
        lv.setItems(data);
    }
    //Tickets for Editing or Deleting Scene
    public static void loadTicketEditOrDeleteListView(ListView lv) throws SQLException {
        //If the list view is empty, load the tickets
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadTicketsEditOrDelete();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);

            //Custom List View creation
            //Adds a coloured edit and delete button to each ticket
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

                        //Gets style from a custom style sheet
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

    //Placeholder class
    public static void editFunction(){
        System.out.println("Edit Clicked");
    }

    //Maintenance
    public static void loadMaintenanceListView(ListView lv) throws SQLException {
        if (lv.getItems().isEmpty()) {
            List<String> data = DBConnect.loadMaintenance();
            ObservableList<String> items = FXCollections.observableArrayList(data);
            lv.setItems(items);
        }
    }
}

