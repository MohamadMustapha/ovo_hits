module com.example.ovohits {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.j;
    requires java.sql.rowset;

    exports com.example.ovohits;
    opens com.example.ovohits to javafx.fxml;
}