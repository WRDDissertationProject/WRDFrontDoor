package com.example.willsrollerdiscosh;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


    public class errors {

        public static Alert minusSkates() {
            Alert negativeSkates = new Alert(Alert.AlertType.INFORMATION, "Skates Cannot be below 0, added to Needed Skates", ButtonType.OK);
            return negativeSkates;
        }

        public static Alert unknownError() {
            Alert negativeSkates = new Alert(Alert.AlertType.ERROR, "An Unknown Error has occurred with Skate Hire", ButtonType.OK);
            return negativeSkates;
        }

        public static Alert alertEmptyBox() {
            Alert emptyField = new Alert(Alert.AlertType.ERROR, "Text Field Cannot Be Empty", ButtonType.OK);
            return emptyField;
        }

        public static Alert deleteNotComplete() {
            Alert deleteNotComplete = new Alert(Alert.AlertType.ERROR, "Error: Record Could Not be Completed", ButtonType.OK);
            return deleteNotComplete;
        }

        public static Alert maintenanceEmpty() {
            Alert maintenanceEmpty = new Alert(Alert.AlertType.INFORMATION, "Error: You have empty mandatory fields, please check and try again", ButtonType.OK);
            return maintenanceEmpty;
        }

        public static Alert sessionNotStarted(){
            Alert sessionNotStarted = new Alert(Alert.AlertType.WARNING, "Cannot Admit Customers Before a Session Has Started");
            return sessionNotStarted;
        }

        public static Alert noSkaterType(){
            Alert noSkaterType = new Alert(Alert.AlertType.ERROR, "No Skater Type Found, record not entered");
            return noSkaterType;
        }

    }
