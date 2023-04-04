package com.example.willsrollerdiscosh;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class dateTime {
    public static String fullDateTime() {
        LocalDateTime fullDateTime = LocalDateTime.now();
        DateTimeFormatter formattedFullDate = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String stFullDateTime =  fullDateTime.format(formattedFullDate);
        return stFullDateTime;
    }

    public static String justDate(){
        LocalDateTime justDate = LocalDateTime.now();
        DateTimeFormatter formattedJustDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String stJustDate=  justDate.format(formattedJustDate);
        return stJustDate;
    }

    public static String justTime(){
        LocalDateTime justTime = LocalDateTime.now();
        DateTimeFormatter formattedJustTime = DateTimeFormatter.ofPattern("HH:mm:ss");
        String stJustTime=  justTime.format(formattedJustTime);
        return stJustTime;
    }
}
