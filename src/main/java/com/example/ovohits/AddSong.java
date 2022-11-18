package com.example.ovohits;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddSong extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddSong.class.getResource("AddSong.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Add New Song");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) { launch(); }
}
