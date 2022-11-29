package com.example.ovohits;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Register extends Application {
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AddSong.class.getResource("Register.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            stage.setTitle("OVO Hits");
            stage.setScene(scene);
            stage.show();
            stage.setOnCloseRequest(event -> SocketConnection.terminateThread());
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void main(String[] args) { launch(); }
}
