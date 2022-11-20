package com.example.ovohits;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.DatagramPacket;
import java.util.ArrayList;

import static com.example.ovohits.SocketConnection.datagramSocket;

public class LandingController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    public void login(){
        String username = usernameField.getText();
        String password = passwordField.getText();
        byte[] dataBuffer;
        if(username.isBlank()||password.isBlank()){
            return;
        }
//        dataBuffer = username.getBytes();
//        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, inetAddress, 6969));
    }
}
