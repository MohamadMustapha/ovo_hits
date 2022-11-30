module com.example.ovohits {
    requires com.dlsc.formsfx;
    requires com.google.common;
    requires java.desktop;
    requires java.sql;
    requires java.sql.rowset;
    requires javafx.controls;
    requires javafx.fxml;
    requires mysql.connector.j;
    requires org.apache.commons.lang3;
    requires org.controlsfx.controls;
    requires org.kordamp.bootstrapfx.core;
    requires javafx.media;


    exports com.example.ovohits;
    opens com.example.ovohits to javafx.fxml;
    exports com.example.ovohits.backend.database.models;
    opens com.example.ovohits.backend.database.models to javafx.fxml;
    exports com.example.ovohits.backend;
    opens com.example.ovohits.backend to javafx.fxml;
}