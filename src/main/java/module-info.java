module com.example.demo2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.demo2 to javafx.fxml;
    exports com.example.demo2;
    exports com.example.demo2.algorithms;
    opens com.example.demo2.algorithms to javafx.fxml;
}