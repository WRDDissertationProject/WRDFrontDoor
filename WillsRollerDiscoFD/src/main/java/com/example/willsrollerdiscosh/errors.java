/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoFD
 *  FILE TITLE: errors.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Used to show errors to the user, improves users interaction with the application..
 *   */

//PACKAGE
package com.example.willsrollerdiscosh;

//IMPORTS
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;


    public class errors {

        public static Alert minusSkates() {
            return new Alert(Alert.AlertType.INFORMATION, "Skates Cannot be below 0, added to Needed Skates",
                    ButtonType.OK);
        }

        public static Alert unknownError() {
            return new Alert(Alert.AlertType.ERROR, "An Unknown Error has occurred with Skate Hire", ButtonType.OK);
        }

        public static Alert alertEmptyBox() {
            return new Alert(Alert.AlertType.ERROR, "Text Field Cannot Be Empty", ButtonType.OK);
        }

        public static Alert deleteNotComplete() {
            return new Alert(Alert.AlertType.ERROR, "Error: Record Could Not be Completed", ButtonType.OK);
        }

        public static Alert maintenanceEmpty() {
            return new Alert(Alert.AlertType.INFORMATION, "Error: You have empty mandatory fields, " +
                    "please check and try again", ButtonType.OK);
        }

        public static Alert sessionNotStarted(){
            return new Alert(Alert.AlertType.WARNING, "Cannot Admit Customers Before a Session Has Started");
        }

        public static Alert noSkaterType(){
           return new Alert(Alert.AlertType.ERROR, "No Skater Type Found, record not entered");
        }
    }
