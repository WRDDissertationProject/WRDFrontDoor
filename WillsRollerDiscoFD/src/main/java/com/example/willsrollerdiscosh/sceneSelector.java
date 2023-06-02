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
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class sceneSelector {
    @FXML
    Button homeButton, lockButton, unlockButton;
    private Stage stage;
    public static Scene scene;
    private Parent root;

    @FXML
    static
    Label sessionStatus, admittedStatus, salesStatus;

    boolean skateHireLocked = false;

    static String resourceName = "skateHire";
    static String lockedBy = "FrontDoorApp";


    @FXML
    TextArea ticketsTextBox;

    @FXML
    public void switchToHome(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("home.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        SesssionStartReloader();

    }
    public static boolean sessionChecker() throws SQLException {
        boolean session = DBConnect.checkForSessionStart();
        if(session){
            return true;
        }
        else {
            return false;
        }
    }


    public void sessionStatus(){
        Platform.runLater(() -> {

            sessionStatus = (Label) root.lookup("#sessionStatus");

            // Check if session has started
            try {
                String sessionTime = DBConnect.getSessionStartTime();
                if (sessionChecker() == true) {
                    sessionStatus.setText("Session: " + sessionTime);
                } else {
                    sessionStatus.setText(sessionTime);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void switchToSkateHire(ActionEvent event) throws IOException, SQLException {
        root = FXMLLoader.load(getClass().getResource("skateHire.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        SkateHireReloader();

        SesssionStartReloader();
    }

    public void switchToTickets(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("tickets.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        TicketsReloader();

        SesssionStartReloader();
    }

    public void switchToCreateTicket(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("createTicket.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        SesssionStartReloader();
    }

    public void switchToEditOrDeleteTicket(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("editOrDeleteTicket.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        EditOrDeleteTicketsReloader();

        SesssionStartReloader();
    }

    public void switchToMaintenance(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("maintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        maintenanceReloader();

        SesssionStartReloader();
    }

    @FXML
    public void switchToAddMaintenance(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("createMaintenance.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        setMaintenanceTypeChoiceBox();
        setSkateSizeChoiceBox();

        SesssionStartReloader();

    }

    @FXML
    public void switchToExtraSales(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("extraSales.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        SesssionStartReloader();
    }

    @FXML
    public void switchToAdmissions(ActionEvent event) throws IOException{
        root = FXMLLoader.load(getClass().getResource("admissions.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        SesssionStartReloader();
    }

    public static void checkForRecord(int count) {
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

    public static void recordExists() {
        //Label session = (Label) scene.lookup("#sessionStatus");
        //session.setText("Session Currently Running");
        System.out.println("Session Currently Running");
    }

    public static void recordDoesNotExist(Scene scene) {
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

    public static ObservableList<Skate> loadSkateHire(ListView lv) throws SQLException {
        resourceName = "skateHire";
        ObservableList<Skate> data = DBConnect.loadSkates();
        if (lv.getItems().isEmpty()) {
            lv.setCellFactory(tv -> new ListCell<Skate>() {
                @Override
                protected void updateItem(Skate item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                        setGraphic(null);
                    } else {
                        Button button = new Button("Pair Hired");
                        button.getStyleClass().add("SH-Button");
                        Label label = new Label("Skate Size: " + item.getSkateSize() + "\nSkates Available: " + item.getSkateAmount());
                        HBox hbox = new HBox();
                        hbox.getChildren().addAll(label, button);
                        hbox.setSpacing(15);
                        setGraphic(hbox);
                        if (item.getSkateAmount() > 10) {
                            setStyle("-fx-background-color:#48992B;");
                            label.setTextFill(Color.web("BEBEBE"));
                        } else if (item.getSkateAmount() <= 10 && item.getSkateAmount() > 5) {
                            setStyle("-fx-background-color: #F7AE2C;");
                            label.setTextFill(Color.web("2D2D2D"));
                        } else if (item.getSkateAmount() <= 5) {
                            setStyle("-fx-background-color: #FA3837;");
                            label.setTextFill(Color.web("BEBEBE"));
                        } else {
                            setStyle("");
                        }
                        button.setOnAction(event -> {
                            // increment skate amount for this row
                            try {
                                locks.lock(resourceName, lockedBy);
                            } catch (SQLException e) {
                                throw new RuntimeException(e);
                            }
                            int currentValue = item.getSkateAmount();
                            if (currentValue > 0) {

                                int newValue = item.getSkateAmount() - 1;
                                item.setSkateAmount(newValue);
                                String skateSize = item.getSkateSize();
                                try {
                                    DBConnect.updateSkateListMinus(newValue, skateSize);
                                    DBConnect.updateSkatesAnalytics(skateSize);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }

                                if (item.getSkateAmount() > 0) {
                                    label.setText(item.getSkateSize() + " " + item.getSkateAmount());
                                    label.setText(item.getSkateSize() + " " + item.getSkateAmount());
                                } else if (item.getSkateAmount() > 10) {
                                    setStyle("-fx-background-color:#48992B;");
                                    label.setTextFill(Color.web("BEBEBE"));

                                } else if (item.getSkateAmount() <= 10 && item.getSkateAmount() > 5) {
                                    setStyle("-fx-background-color: #F7AE2C;");
                                    label.setTextFill(Color.web("2D2D2D"));

                                } else if (item.getSkateAmount() <= 5) {
                                    setStyle("-fx-background-color: #FA3837;");
                                    label.setTextFill(Color.web("BEBEBE"));
                                } else {
                                    errors.unknownError().show();
                                }
                                try {
                                    locks.unlock(resourceName, lockedBy);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            else {
                                errors.minusSkates().show();
                                //Add to 0 Skate List
                                DBConnect.neededSkates(item.getSkateSize());
                                try {
                                    locks.unlock(resourceName, lockedBy);
                                } catch (SQLException e) {
                                    throw new RuntimeException(e);
                                }
                            }

                        });
                    }
                }
            });
        }
        return data;
    }

    public void SkateHireReloader() {
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

    public void SesssionStartReloader() {
        Timer reloadSkateHire = new Timer();
        reloadSkateHire.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> {
                    System.out.println("Session Start Reload");
                    sessionStatus();
                });
            }
        }, 0, 800); // reload every second

    }

    public void TicketsReloader() {
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

    public void EditOrDeleteTicketsReloader() {
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

    public void maintenanceReloader() {
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

    public void createTicket(ActionEvent actionEvent) throws SQLException {
        resourceName = " tickets";
        if (ticketsTextBox.getText().isEmpty()) {
            errors.alertEmptyBox().show();
        } else {
            locks.lock(resourceName, lockedBy);
            String ticketText = ticketsTextBox.getText();
            System.out.println("Text Found");
            DBConnect insertTicket = new DBConnect();
            DBConnect.insertTicket(ticketText);
            ticketsTextBox.clear();
            locks.unlock(resourceName,lockedBy);
        }
    }

    public static void deleteFunction(String item){
        System.out.println("Delete Clicked");
        resourceName = "tickets";
        try {
            locks.lock(resourceName, lockedBy);
            String ticket = item.substring(8);
            boolean success = DBConnect.deleteTicket(ticket);

            if (success) {
                ListView<Skate> lv = (ListView<Skate>) scene.lookup("#CTListViewEditOrDelete");
                if (lv != null) {
                    lv.getItems().clear();
                    listViews.loadTicketEditOrDeleteListView(lv);;
                    System.out.println("Tickets Reload Delete");
                }
            }locks.unlock(resourceName,lockedBy);
        }
        catch(Exception e) {
            errors.deleteNotComplete().show();
        }
    }

    public void setMaintenanceTypeChoiceBox() {
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#maintenanceTypeChoiceBox");
        List<String> type = new ArrayList<>();
        type.add(new String("Skate Hire"));
        type.add(new String("Games Equipment"));
        type.add(new String("Lighting Rig"));
        type.add(new String("DJ Equipment"));
        type.add(new String("Front Door"));
        type.add(new String("Other"));

        choiceBox.getItems().addAll(type);
    }

    public void setSkateSizeChoiceBox() {
        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        List<String> skate = new ArrayList<>();

        skate.add(null);
        skate.add(new String("C11"));
        skate.add(new String("C12"));
        skate.add(new String("C13"));
        skate.add(new String("1"));
        skate.add(new String("2"));
        skate.add(new String("3"));
        skate.add(new String("4"));
        skate.add(new String("5"));
        skate.add(new String("6"));
        skate.add(new String("7"));
        skate.add(new String("8"));
        skate.add(new String("9"));
        skate.add(new String("10"));
        skate.add(new String("11"));
        skate.add(new String("12"));
        skate.add(new String("13"));
        choiceBox.getItems().addAll(skate);
    }

    public void createMaintenanceYesButton(){
        Button noButton = (Button) scene.lookup("#CMRNo");
        noButton.setVisible(true);

        Button yesButton = (Button) scene.lookup("#CMRYes");
        yesButton.setVisible(false);

        Label skateSizeLbl = (Label) scene.lookup("#skateSizeLbl");
        skateSizeLbl.setVisible(true);

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        choiceBox.setVisible(true);
    }

    public void createMaintenanceNoButton(){
        Button yesButton = (Button) scene.lookup("#CMRYes");
        yesButton.setVisible(true);

        Button noButton = (Button) scene.lookup("#CMRNo");
        noButton.setVisible(false);

        Label skateSizeLbl = (Label) scene.lookup("#skateSizeLbl");
        skateSizeLbl.setVisible(false);

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        choiceBox.setVisible(false);
    }

    public void createMaintenanceSubmit() {
        System.out.println("Clicked");

        ChoiceBox<String> choiceBox = (ChoiceBox<String>) scene.lookup("#maintenanceTypeChoiceBox");
        ChoiceBox<String> skateSize = (ChoiceBox<String>) scene.lookup("#skateSizeChoiceBox");
        TextArea maintenanceDetails = (TextArea) scene.lookup("#maintenanceDetails");

        //if null
        if (choiceBox == null || choiceBox.getValue() == null || choiceBox.getValue().isEmpty() || maintenanceDetails == null || maintenanceDetails.getText().isEmpty()) {
            errors.maintenanceEmpty().show();
        } else {
            try {
                resourceName = "maintenance";
                locks.lock(resourceName, lockedBy);
                String typeIn = choiceBox.getValue();
                String details = maintenanceDetails.getText();
                String skateSizeIn = skateSize.getValue();
                DBConnect.insertMaintenance(typeIn, details, skateSizeIn);

                maintenanceDetails.clear();
                locks.unlock(resourceName, lockedBy);
            } catch (SQLException e) {
                System.out.println("Couldn't Add Maintenance Record");
                throw new RuntimeException(e);

            }

        }
    }

    public void addSkaterOwnSkates(ActionEvent event) throws SQLException {
        boolean session = sessionChecker();

        if(session){
            double value = 6.00;
            String type = "Skater";

            String sessionDateTime = DBConnect.getSessionStartTime();
            System.out.println("Add Skater Own Skates");
            showAdmissionLabel(((Node) event.getSource()).getScene().getRoot());
            DBConnect.addSkaterAdmission("ownSkates", false, value);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value );
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }
    }

    public void addSkaterSkaterHire(ActionEvent event) throws SQLException {
        boolean session = sessionChecker();

        if(session){
            double value = 7.50 ;
            String type = "Skate Hire";
            String sessionDateTime = DBConnect.getSessionStartTime();

            System.out.println("Add Skater Skate Hire");
            showAdmissionLabel(((Node) event.getSource()).getScene().getRoot());
            DBConnect.addSkaterAdmission("skateHire", false, value);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }

    }

    public void addMemberOwnSkates(ActionEvent event) throws SQLException {
        boolean session = sessionChecker();
        showAdmissionLabel(((Node) event.getSource()).getScene().getRoot());

        if(session){
            double value = 6.00;
            String type = "Skater";
            String sessionDateTime = DBConnect.getSessionStartTime();

            System.out.println("Add Member Own Skates");
            showAdmissionLabel(((Node) event.getSource()).getScene().getRoot());
            DBConnect.addSkaterAdmission("ownSkates", true, value);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }

    }

    public void addMemberSkateHire(ActionEvent event) throws SQLException {
        boolean session = sessionChecker();

        if(session){
            double value = 7.50;
            String type = "Skate Hire";
            String sessionDateTime = DBConnect.getSessionStartTime();

            System.out.println("Add Member Skate Hire");
            showAdmissionLabel(((Node) event.getSource()).getScene().getRoot());
            DBConnect.addSkaterAdmission("skateHire", true, value);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value );
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }

    }

    public void addSpectator(ActionEvent event) throws SQLException {
        boolean session = sessionChecker();

        if(session){
            double value = 0.00;
            String type = "Spectator";
            String sessionDateTime = DBConnect.getSessionStartTime();

            System.out.println("Add Spectator");
            showAdmissionLabel(((Node) event.getSource()).getScene().getRoot());
            DBConnect.addSkaterAdmission("spectator", false, value);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value );
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }
    }

    public void showAdmissionLabel(Parent root) {
        Label admissionLabel= (Label) root.lookup("#admittedStatus");
        admissionLabel.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            Platform.runLater(() -> {
                admissionLabel.setVisible(false);
            });
        }, 2, TimeUnit.SECONDS);
    }

    public void showSalesLabel(Parent root) {
        Label salesLabel= (Label) root.lookup("#salesStatus");
        salesLabel.setVisible(true);

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.schedule(() -> {
            Platform.runLater(() -> {
                salesLabel.setVisible(false);
            });
        }, 2, TimeUnit.SECONDS);
    }

    public void addGlowstickSale(ActionEvent event) throws SQLException {
        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            double value = 0.20;
            String type = "Glow Stick";
            String sessionDateTime = DBConnect.getSessionStartTime();

            System.out.println("Add Glow Stick Sale");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());

            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);

        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }
    }

    public void addFoamGlowstickSale(ActionEvent event) throws SQLException {
        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            double value = 2.00;
            String type = "Foam Glow Stick";
            String sessionDateTime = DBConnect.getSessionStartTime();

            System.out.println("Add Foam Glow Stick Sale");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());

            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(2.00);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }
    }

    public void addSkateLacesSale(ActionEvent event) throws SQLException {
        double value = 3.00;
        String type = "Skate Laces";
        String sessionDateTime = DBConnect.getSessionStartTime();

        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            System.out.println("Add Skate Laces Sale");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());

            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }
    }

    public void addSeasonalSale(ActionEvent event) throws SQLException {
        double value = 1.00;
        String type = "Seasonal Sale";
        String sessionDateTime = DBConnect.getSessionStartTime();

        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            System.out.println("Add Seasonal Sale");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());

            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }
    }

    public void addItemReplacement(ActionEvent event) throws SQLException {
        double value = 0.00;
        String type = "Replacement";
        String sessionDateTime = DBConnect.getSessionStartTime();

        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            System.out.println("Add ItemReplacement");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());

            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }
    }

    public void addFreePromotion(ActionEvent event) throws SQLException {
        double value = 0.00;
        String type = "Free Promotion";
        String sessionDateTime = DBConnect.getSessionStartTime();

        resourceName ="extraSales";
        boolean session = sessionChecker();

        if(session){
            System.out.println("Add Free Promotion");
            showSalesLabel(((Node) event.getSource()).getScene().getRoot());
            locks.lock(resourceName,lockedBy);
            DBConnect.addExtraPurchase(value);
            locks.unlock(resourceName,lockedBy);

            resourceName= "transactions";
            locks.lock(resourceName,lockedBy);
            String time = dateTime.justTime();
            DBConnect.addTransaction(sessionDateTime, type, time, value);
            locks.unlock(resourceName,lockedBy);
        }
        else {
            System.out.println("Session not started");
            errors.sessionNotStarted().show();
        }
    }



}



//add / remove skates
//max number = max of skates

//if add skate = < max Number
//error can't be added