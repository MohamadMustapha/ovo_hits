package com.example.ovohits;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class Utilities {
    public static void addToLibrary(ListView<String> listView) {
        String song = listView.getSelectionModel().getSelectedItem();
        if (song == null) return;

        int index = Integer.parseInt(song.substring(song.lastIndexOf(" ") + 1));
        try {
            Request request = new Request("@addSavedSong");
            request.setSavedSongInfo(new ArrayList<>(Arrays.asList(
                    index,
                    Client.getClientId())));
            Client.sendRequest(request);
        } catch (SQLException e) { throw new RuntimeException(e); }

        listView.getItems().remove(index);
    }

    public static void goPage(Button button, FXMLLoader fxmlLoader) {
        try {
            Stage stage = (Stage) button.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void initializeStage(Stage stage, Scene scene) {
        stage.setTitle("OVO Hits");
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(event -> Client.terminateThread());
    }

    public static File uploadSong(ListView<String> listView) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        File songFile = fileChooser.showOpenDialog(null);
        if (songFile != null) {
            if (listView.getItems().isEmpty()) listView.getItems().add(songFile.getName());
            else listView.getItems().set(0, songFile.getName());
        }
        return songFile;
    }
}
