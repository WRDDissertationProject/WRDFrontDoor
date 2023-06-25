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

/*Resources Used:
 * Java DateTime Layouts:
 * w3Schools (2023) Java Date and Time. W3Schools [Online]. Available From: https://www.w3schools.com/java/java_date.asp
 * [Accessed 15/02/2023].
 *
 * Evans, B. Warburton, R. (2014) Java SE 8 Date and Time. Oracle [Online]. Available From:
 * https://www.oracle.com/technical-resources/articles/java/jf14-date-time.html [Accessed 15/02/2023]. */
public class dateTime {
    //Creates a date object, passes into a format so just the date is used, returned to class that called it.
    public static String justDate(){
        //Fetches current machine time
        LocalDateTime justDate = LocalDateTime.now();
        //Passes into a layout
        DateTimeFormatter formattedJustDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return justDate.format(formattedJustDate);
    }
    //Creates a date object, passes into a format so just the date is used, returned to class that called it.
    public static String justTime(){
        //Fetches current machine time
        LocalDateTime justTime = LocalDateTime.now();
        //Passes into a layout
        DateTimeFormatter formattedJustTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        return justTime.format(formattedJustTime);
    }
}
