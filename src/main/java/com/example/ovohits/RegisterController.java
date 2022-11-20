package com.example.ovohits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class RegisterController {
    private File songData = null;
    @FXML
    private Button returnButton;
    @FXML
    private ListView<String> listView;
    @FXML
    private PasswordField passwordInput;
    @FXML
    private TextField emailInput;
    @FXML
    private TextField firstNameInput;
    @FXML
    private TextField lastNameInput;
    @FXML
    private TextField songNameInput;
    @FXML
    private TextField usernameInput;

    public void addUser() throws Exception {
        if (firstNameInput.getText().length() == 0) return;
        if (lastNameInput.getText().length() == 0) return;
        if (usernameInput.getText().length() == 0) return;
        if (passwordInput.getText().length() == 0) return;
        if (emailInput.getText().length() == 0) return;

        if (songData == null) return;
        if (listView.getItems().size() == 0) return;

        byte[] dataBuffer;
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        InetAddress inetAddress = InetAddress.getLocalHost();

        dataBuffer = emailInput.getText().getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        dataBuffer = firstNameInput.getText().getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        dataBuffer = lastNameInput.getText().getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        dataBuffer = passwordInput.getText().getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        dataBuffer = usernameInput.getText().getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        dataBuffer = "@addUser".getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        FileInputStream fileInputStream = new FileInputStream(songData);
        dataBuffer = new byte[(int) songData.length()];
        if (fileInputStream.read(dataBuffer) == -1) {
            System.out.println("Reading song audio failed!");
            return;
        }
        fileInputStream.close();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        dataBuffer = songNameInput.getText().getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));
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
