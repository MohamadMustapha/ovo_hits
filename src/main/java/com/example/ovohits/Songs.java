package com.example.ovohits;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Songs extends Application {
    @Override
    public void start(Stage stage) {
        try {
            Utilities.initializeStage(
                    stage,
                    new Scene(new FXMLLoader(Songs.class.getResource("Songs.fxml")).load()));
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void main(String[] args) {
        launch();
    }
}
