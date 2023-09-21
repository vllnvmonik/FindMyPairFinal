module com.example.ewankona {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;


    opens com.example.findmypair to javafx.fxml;
    exports com.example.findmypair;
}