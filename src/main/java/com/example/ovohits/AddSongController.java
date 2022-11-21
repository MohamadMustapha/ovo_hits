package com.example.ovohits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
//import org.apache.commons.lang3.SerializationUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

public class AddSongController {
    private File songFile = null;
    @FXML
    private Button returnButton;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextField songNameInput;

    public void addSong() throws Exception {
        if (songFile.exists()) return;
        if (listView.getItems().isEmpty()) return;

        FileInputStream fileInputStream = new FileInputStream(songFile);
        byte[] songData = new byte[(int) songFile.length()];
        if (fileInputStream.read(songData) == -1) throw new IOException();
        fileInputStream.close();
        ArrayList<String> addSongArray = new ArrayList<>(Arrays.asList(songNameInput.getText(), "1"));

        Request request = new Request(null, addSongArray, null, new SerialBlob(songData), "@addSong");

        byte[] dataBuffer = SerializationUtils.serialize(request);
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, InetAddress.getLocalHost(), 6969));
    }

    public void goBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddSong.class.getResource("Landing.fxml"));
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load()));
    }

    public void uploadSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

        songFile = fileChooser.showOpenDialog(null);
        if (songFile != null) {
            if (listView.getItems().isEmpty())
                listView.getItems().add(songFile.getName());
            else
                listView.getItems().set(0, songFile.getName());
        }
    }
}
