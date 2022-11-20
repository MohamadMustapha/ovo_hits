package com.example.ovohits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LandingController {
    @FXML
    private Button loginButton;
    @FXML
    private PasswordField passwordField;
    @FXML
    private TextField usernameField;

    public void login() throws Exception {
        if (usernameField.getText().isBlank()) return;
        if (passwordField.getText().isBlank()) return;

        byte[] dataBuffer;
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        InetAddress inetAddress = InetAddress.getLocalHost();

        dataBuffer = usernameField.getText().getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        dataBuffer = passwordField.getText().getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        dataBuffer = "@login".getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        DatagramPacket datagramPacket = new DatagramPacket(dataBuffer, dataBuffer.length);
        datagramSocket.receive(datagramPacket);
        if (dataBuffer[0] != 0) {
            FXMLLoader fxmlLoader = new FXMLLoader(LandingController.class.getResource("Main.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        }
    }
}
