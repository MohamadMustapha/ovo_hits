package com.example.ovohits;

import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
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

    public void addSong() throws IOException, SQLException {
        if (!songFile.exists()) return;
        if (listView.getItems().isEmpty()) return;

        byte[] songData = new byte[(int) songFile.length()];
        FileInputStream fileInputStream = new FileInputStream(songFile);
        if (fileInputStream.read(songData) == -1) throw new RuntimeException();
        fileInputStream.close();
        ArrayList<String> addSongArray = new ArrayList<>(Arrays.asList(songNameInput.getText(),
                Integer.toString(1
//                        Client.getSessionId()
                )));
        Request request = new Request(addSongArray, new SerialBlob(songData));

        byte[] dataBuffer = SerializationUtils.serialize(request);
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length,
                SocketConnection.getInetAddress(), SocketConnection.getPort()));
        List<List<Byte>> dataBuffers = Lists.partition(
                new ArrayList<>(Bytes.asList(dataBuffer)), 64000);
        for (List<Byte> dataBufferList : dataBuffers) {
            dataBuffer = ArrayUtils.toPrimitive(dataBufferList.toArray(new Byte[0]));

        }
    }

    public void goBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddSong.class.getResource("Main.fxml"));
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
