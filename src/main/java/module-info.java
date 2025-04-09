module com.example.currencyconverter2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;


    opens com.example.currencyconverter2 to javafx.fxml;
    exports com.example.currencyconverter2;
}