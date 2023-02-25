package com.example.willsrollerdiscosh;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


    public class errors {

        public static Alert minusSkates() {
            Alert negativeSkates = new Alert(Alert.AlertType.ERROR, "Skates Cannot be Negative Numbers", ButtonType.OK);
            return negativeSkates;
        }

    }
