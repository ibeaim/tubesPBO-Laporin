module com.laporin.tubesjava_laporin {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;


    opens com.laporin.tubesjava_laporin to javafx.fxml;
    exports com.laporin.tubesjava_laporin;
}