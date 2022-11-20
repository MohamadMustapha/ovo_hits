package com.example.ovohits;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.*;



public class Client {
    @FXML
    private static TextField usernameField;
    @FXML
    private static PasswordField passwordField;

    public static void main(String[] args) throws Exception {
        // connection
        DatagramSocket datagramSocket = new DatagramSocket();
        InetAddress ip = InetAddress.getLocalHost();
        byte[] dataBuffer = null;

        while(true){
            // read data and pass it into buffer
            String username = usernameField.getText();
            String password = passwordField.getText();
            dataBuffer = username.getBytes();
            // pass data to the packet
            DatagramPacket dataToSend = new DatagramPacket(dataBuffer,dataBuffer.length,ip,6969);
            datagramSocket.send(dataToSend);

            //login the user
                //press the login button

            // register the user
                //press the register button

            // close the server

        }

    }


}
