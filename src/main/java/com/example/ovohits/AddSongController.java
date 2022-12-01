package com.example.ovohits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.SQLException;
import java.util.*;

public class AddSongController {
    private File songFile = null;
    @FXML
    private Button returnButton;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextField songNameInput;

    public void addSong() {
        if (!songFile.exists()) return;
        if (listView.getItems().isEmpty()) return;

        try {
            FileInputStream fileInputStream = new FileInputStream(songFile);
            byte[] songData = new byte[(int) songFile.length()];
            if (fileInputStream.read(songData) == -1) throw new RuntimeException();
            fileInputStream.close();

            ArrayList<String> songInfo = new ArrayList<>(Arrays.asList(
                    songNameInput.getText(),
                    Integer.toString(Client.getClientId())));
            Client.sendRequest(new Request(
                    songInfo,
                    new SerialBlob(songData)));
        } catch (IOException | SQLException e) { throw new RuntimeException(e); }

        goMain();
    }

    public void goMain() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Songs.class.getResource("Songs.fxml"));
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void uploadSong() { songFile = Utilities.uploadSong(listView); }
}
