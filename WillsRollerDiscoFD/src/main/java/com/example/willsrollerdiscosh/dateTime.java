/** WILLS ROLLER DISCO - DISSERTATION PROJECT
 *  AUTHOR : EMILY FLETCHER
 *  STUDENT NUMBER: 18410839
 *  APPLICATION: WillsRollerDiscoFD
 *  FILE TITLE: dateTime.java
 *  APPLICATION VERSION: 2.0
 *  DATE OF WRITING: 20/06/2023
 *
 *  PURPOSE:
 *   Used for creation of Date Time Objects. Used for database record creation as well as session checks.
 *   */

//PACKAGE
package com.example.willsrollerdiscosh;

//IMPORTS
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class dateTime {

    public static String justDate(){
        LocalDateTime justDate = LocalDateTime.now();
        DateTimeFormatter formattedJustDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return justDate.format(formattedJustDate);
    }

    public static String justTime(){
        LocalDateTime justTime = LocalDateTime.now();
        DateTimeFormatter formattedJustTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        return justTime.format(formattedJustTime);
    }
}
