module com.example.willsrollerdiscosh {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.willsrollerdiscosh to javafx.fxml;
    exports com.example.willsrollerdiscosh;
}