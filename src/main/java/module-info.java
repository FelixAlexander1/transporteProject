module com.example.transporte {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.transporte to javafx.fxml;
    exports com.example.transporte;
    exports com.example.transporte.controller;
    opens com.example.transporte.controller to javafx.fxml;
}