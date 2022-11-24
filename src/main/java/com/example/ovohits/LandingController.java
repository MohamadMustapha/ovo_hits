package com.example.ovohits;

import com.example.ovohits.backend.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
import java.util.Arrays;

public class LandingController {
    @FXML
    private Button loginButton;
    @FXML
    private Button registerButton;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;

    public void login() throws Exception {
        if (usernameField.getText().isBlank()) return;
        if (passwordField.getText().isBlank()) return;

        ArrayList<String> loginArray = new ArrayList<>(Arrays.asList(
                usernameField.getText(),
                passwordField.getText()));
        Request request = new Request(loginArray);

        byte[] dataBuffer = SerializationUtils.serialize(request);
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        datagramSocket.send(new DatagramPacket(
                dataBuffer,
                dataBuffer.length,
                SocketConnection.getInetAddress(),
                SocketConnection.getPort()));

        DatagramPacket datagramPacket = new DatagramPacket(
                dataBuffer,
                dataBuffer.length);
        datagramSocket.receive(datagramPacket);
        Response response = SerializationUtils.deserialize(dataBuffer);
        if (response.getExists()) {
            Client.setSessionId(response.getUserId());
            FXMLLoader fxmlLoader = new FXMLLoader(LandingController.class.getResource("Main.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        }
    }

    public void goRegister() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(AddSong.class.getResource("Register.fxml"));
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load()));
        Client.setSessionId(null);
    }
}
