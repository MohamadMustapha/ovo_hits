package com.example.ovohits;

import com.example.ovohits.backend.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MainController {
    @FXML
    private Button returnButton;
    @FXML
    private Button uploadSongButton;
    @FXML
    private ListView<String> myPlaylistView;
    @FXML
    private ListView<String> songsView;

    private Response sendRequest(Request request) throws IOException {
        byte[] dataBuffer = SerializationUtils.serialize(request);
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length,
                SocketConnection.getInetAddress(), SocketConnection.getPort()));

        DatagramPacket datagramPacket = new DatagramPacket(dataBuffer, dataBuffer.length);
        datagramSocket.receive(datagramPacket);
        return SerializationUtils.deserialize(dataBuffer);
    }

    public void addToMyPlaylist() { myPlaylistView.getItems().add(songsView.getSelectionModel().getSelectedItem()); }

    public void goBack() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddSong.class.getResource("Landing.fxml"));
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load()));
        Client.setSessionId(null);
    }


//    public void initialize() throws IOException {
//        Request request = new Request("@getSongs");
//        System.out.println(Client.getSessionId());
//        request.setModelId(Client.getSessionId());
//        Response response = sendRequest(request);
//        ArrayList<Song> songArrayList = new ArrayList<>(response.getSongDataArrayList().stream()
//                .map(bytes -> (Song) SerializationUtils.deserialize(bytes)).toList());
//        System.out.println(songArrayList);
//        for (Song song : songArrayList) {
//            response = sendRequest(new Request("@getUser"));
//            User user = SerializationUtils.deserialize(response.getUserData());
//            songsView.getItems().add(song.getName() + " from: " + user.getUsername() + " id: " +
//                    song.getId());
//        }
//    }

    public void playSong() {
        // TODO: Send partial audio packets to backend and download/stream the file (ex: YouTube)
    }

    public void uploadSong() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddSong.class.getResource("AddSong.fxml"));
        Stage stage = (Stage) uploadSongButton.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load()));
    }
}
