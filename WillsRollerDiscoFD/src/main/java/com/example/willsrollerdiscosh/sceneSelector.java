package com.example.willsrollerdiscosh;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Timer;
import java.util.TimerTask;

public class sceneSelector {
    @FXML
    Button homeButton, lockButton, unlockButton;
    private Stage stage;
    public static Scene scene;
    private Parent root;

    @FXML
    static
    Label sessionStatus;

    boolean skateHireLocked = false;

    @FXML
    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToSkateHire(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("skateHire.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        SkateHireReloader();
    }

    public static void checkForRecord(int count){
        Button skateHire = (Button) scene.lookup("#skateHireButton");
        Button tickets = (Button) scene.lookup("#ticketsButton");
        Button maintenance = (Button) scene.lookup("#maintenanceButton");

        if (count > 0) {
            System.out.println("Session Currently Running");

            //skateHire.setDisable(false);
            // tickets.setDisable(false);
            // maintenance.setDisable(false);
        } else {
            System.out.println("Session Stopped");
            //Label session = (Label) scene.lookup("#sessionStatus");
            //session.setText("Session Stopped");

            //skateHire.setDisable(true);
            //tickets.setDisable(true);
            //maintenance.setDisable(true);
        }
    }
    public static void recordExists(){
        //Label session = (Label) scene.lookup("#sessionStatus");
        //session.setText("Session Currently Running");
        System.out.println("Session Currently Running");
    }

    public static void recordDoesNotExist(Scene scene){
        //Label session = (Label) scene.lookup("#sessionStatus");

        Button skateHire = (Button) scene.lookup("#skateHireButton");
        skateHire.setDisable(true);
    }



    public void lockSkateHire(ActionEvent event) throws IOException {
        System.out.println("Locked");
        this.lockButton.setVisible(false);
        skateHireLocked = true;
        this.unlockButton.setVisible(true);
    }

    public void unlockSkateHire(ActionEvent event) throws IOException {
        System.out.println("unlocked");
        this.unlockButton.setVisible(false);
        skateHireLocked = false;
        this.lockButton.setVisible(true);
    }

    public void loadSkateHire() throws SQLException {
        //Testing Data
        //Eventually replace with fetch from database

        ObservableList<Skate> data = DBConnect.loadSkates();
        // create table columns
        TableColumn<Skate, String> skateSizeCol = new TableColumn<>("Skate Size");
        skateSizeCol.setCellValueFactory(new PropertyValueFactory<>("skateSize"));

        TableColumn<Skate, String> skateAmountCol = new TableColumn<>("Skate Amount");
        skateAmountCol.setCellValueFactory(new PropertyValueFactory<>("skateAmount"));

        // create table view and set data and columns
        TableView<Skate> tableView = new TableView<>();
        tableView.setItems(data);
        tableView.getColumns().addAll(skateSizeCol, skateAmountCol);

        // get the existing list view and replace it with the table view
        ListView lv = (ListView) scene.lookup("#SHListView");
        ListView<Skate> listView = lv; // replace with your own method to get the existing list view
        listView.getItems().clear();
        listView.setCellFactory(tv -> new ListCell<Skate>() {
            @Override
            protected void updateItem(Skate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    Button button = new Button("Pair Hired");
                    Label label = new Label(item.getSkateSize() + " " + item.getSkateAmount());
                    HBox hbox = new HBox();
                    hbox.getChildren().addAll(label, button);
                    hbox.setSpacing(15);
                    setGraphic(hbox);
                    if (item.getSkateAmount() > 10) {
                        setStyle("-fx-background-color:#48992B;");
                        label.setTextFill(Color.web("BEBEBE"));
                    } else if(item.getSkateAmount() <= 10 && item.getSkateAmount() > 5){
                        setStyle("-fx-background-color: #F7AE2C;");
                        label.setTextFill(Color.web("2D2D2D"));
                    } else if(item.getSkateAmount() <= 5){
                        setStyle("-fx-background-color: #FA3837;");
                        label.setTextFill(Color.web("BEBEBE"));
                    }
                    else {
                        setStyle("");
                    }
                    button.setOnAction(event -> {
                        // increment skate amount for this row
                        int currentValue = item.getSkateAmount();
                        if (currentValue > 0) {

                            int newValue = item.getSkateAmount() - 1;
                            item.setSkateAmount(newValue);
                            String skateSize = item.getSkateSize();
                                try {
                                    DBConnect.updateSkateListMinus(newValue, skateSize);
                                } catch (SQLException e) {
                                throw new RuntimeException(e);
                                }

                            if(item.getSkateAmount() > 0) {
                                label.setText(item.getSkateSize() + " " + item.getSkateAmount());
                                label.setText(item.getSkateSize() + " " + item.getSkateAmount());
                            }
                            else if (item.getSkateAmount() > 10) {
                                setStyle("-fx-background-color:#48992B;");
                                label.setTextFill(Color.web("BEBEBE"));

                            } else if(item.getSkateAmount() <= 10 && item.getSkateAmount() > 5){
                                setStyle("-fx-background-color: #F7AE2C;");
                                label.setTextFill(Color.web("2D2D2D"));

                            } else if(item.getSkateAmount() <= 5){
                                setStyle("-fx-background-color: #FA3837;");
                                label.setTextFill(Color.web("BEBEBE"));
                            }
                        }
                        else {
                            //errors.minusSkates();
                        }

                    });

                }

            }
        });
        listView.setItems(data);
    }

    public void SkateHireReloader() {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    try {
                        loadSkateHire();
                        System.out.println("Skate Hire Reload");
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }, 0, 800); // reload every second

}}



//add / remove skates
//max number = max of skates

//if add skate = < max Number
//error can't be added