module com.example.ovohits {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires mysql.connector.j;
    requires java.sql.rowset;
    requires org.apache.commons.lang3;

    exports com.example.ovohits;
    opens com.example.ovohits to javafx.fxml;

    exports com.example.ovohits.database.models;
    opens com.example.ovohits.database.models to javafx.fxml;
}