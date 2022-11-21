package com.example.ovohits;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

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

        ArrayList<String> loginArray = new ArrayList<>(Arrays.asList(usernameField.getText(),
                passwordField.getText()));

        Request request = new Request(loginArray);

        byte[] dataBuffer = SerializationUtils.serialize(request);
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, InetAddress.getByName("10.169.30.40"), 6969));

        DatagramPacket datagramPacket = new DatagramPacket(dataBuffer, dataBuffer.length);
        datagramSocket.receive(datagramPacket);
        Response response = SerializationUtils.deserialize(dataBuffer);
        if (response.getExists()) {
            UserSession.setSessionId(response.getUser().getId());
            FXMLLoader fxmlLoader = new FXMLLoader(LandingController.class.getResource("Main.fxml"));
            Stage stage = (Stage) loginButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        }
    }
}
