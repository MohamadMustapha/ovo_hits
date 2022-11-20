package com.example.ovohits;

import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.services.SongService;
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
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class AddSongController {
    private File songData = null;
    @FXML
    private Button returnButton;
    @FXML
    private ListView<String> listView;
    @FXML
    private TextField songNameInput;

    public void addSong() throws Exception {
        if (songData == null) return;
        if (listView.getItems().size() == 0) return;

        byte[] dataBuffer;
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();

        FileInputStream fileInputStream = new FileInputStream(songData);
        dataBuffer = new byte[(int) songData.length()];
        if (fileInputStream.read(dataBuffer) == -1) {
            System.out.println("Read file failed!");
            return;
        }
        fileInputStream.close();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, InetAddress.getLocalHost(), 6969));

        dataBuffer = songNameInput.getText().getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, InetAddress.getLocalHost(), 6969));



//        new SongService().add(new Song(
//                new SerialBlob(songDataBytes),
//                songNameInput.getText(),
//                1
//        ));
    }

    public void goBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddSong.class.getResource("Landing.fxml"));
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load()));
    }

    public void uploadSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

        songData = fileChooser.showOpenDialog(null);
        if (songData != null) {
            if (listView.getItems().size() == 0)
                listView.getItems().add(songData.getName());
            else
                listView.getItems().set(0, songData.getName());
        }
    }
}
