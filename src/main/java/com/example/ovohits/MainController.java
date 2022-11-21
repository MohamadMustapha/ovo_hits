package com.example.ovohits;

import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class MainController {
    private ArrayList<Song> songList;
    @FXML
    private ListView<String> songsView;

    public void initialize() throws IOException {
        Request request = new Request("@getSongs");
        byte[] dataBuffer = SerializationUtils.serialize(request);
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, InetAddress.getLocalHost(), 6969));

        DatagramPacket datagramPacket = new DatagramPacket(dataBuffer, dataBuffer.length);
        datagramSocket.receive(datagramPacket);
        request = SerializationUtils.deserialize(dataBuffer);
        songList = request.getGetSongArray();

        for (Song song : songList) {
            request = new Request(song.getUser_id(), "@getUser");
            dataBuffer = SerializationUtils.serialize(request);
            datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, InetAddress.getLocalHost(), 6969));

            datagramPacket = new DatagramPacket(dataBuffer, dataBuffer.length);
            datagramSocket.receive(datagramPacket);
            request = SerializationUtils.deserialize(dataBuffer);

            songsView.getItems().add(song.getName() + " from: " + request.getUser().getUsername());
        }

    }
}