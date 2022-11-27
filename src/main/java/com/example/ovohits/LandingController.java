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

    public void exit() {
        try { SocketConnection.sendByteFragments(SerializationUtils.serialize(new Request("@exit"))); }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    public void goRegister() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Register.class.getResource("Register.fxml"));
        Stage stage = (Stage) registerButton.getScene().getWindow();
        stage.setScene(new Scene(fxmlLoader.load()));
    }

    public void initialize() throws IOException {
        if (Client.isThreadAlive()) return;
        byte[] dataBuffer = SerializationUtils.serialize(new Request());
        SocketConnection.getDatagramSocket().send(new DatagramPacket(
                dataBuffer,
                dataBuffer.length,
                SocketConnection.getInetAddress(),
                SocketConnection.getPort()));

        dataBuffer = new byte[64000];
        DatagramPacket datagramPacket = new DatagramPacket(
                dataBuffer,
                dataBuffer.length);
        SocketConnection.getDatagramSocket().receive(datagramPacket);
        Response response = SerializationUtils.deserialize(dataBuffer);
        SocketConnection.setPort(response.getPort());
        Client.setThreadAlive(true);

        dataBuffer = SerializationUtils.serialize(new Request());
        SocketConnection.getDatagramSocket().send(new DatagramPacket(
                dataBuffer,
                dataBuffer.length,
                SocketConnection.getInetAddress(),
                SocketConnection.getPort()));
    }

    public void login() throws Exception {
        if (usernameField.getText().isBlank()) return;
        if (passwordField.getText().isBlank()) return;

        ArrayList<String> loginInfo = new ArrayList<>(Arrays.asList(
                usernameField.getText(),
                passwordField.getText()));
        SocketConnection.sendByteFragments(SerializationUtils.serialize(new Request(loginInfo)));

        Response response = SerializationUtils.deserialize(SocketConnection.getByteFragments());
        if (response.isFunctionCalled() && response.isExists()) {
            Client.setClientId(response.getUserId());
            FXMLLoader fxmlLoader = new FXMLLoader(LandingController.class.getResource("Main.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        }
    }
}
