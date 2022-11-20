package com.example.ovohits;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class LandingController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void login() throws Exception {
        String username = usernameField.getText();
        String password = passwordField.getText();

        byte[] dataBuffer;
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        InetAddress inetAddress = InetAddress.getLocalHost();

        if (username.isBlank()||password.isBlank()){
            return;
        }
        dataBuffer = username.getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        dataBuffer = password.getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));

        dataBuffer = "@login".getBytes();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));
    }
}
