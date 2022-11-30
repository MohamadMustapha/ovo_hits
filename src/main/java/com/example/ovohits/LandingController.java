package com.example.ovohits;

import com.example.ovohits.backend.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
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

    public void goRegister() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Register.class.getResource("Register.fxml"));
            Stage stage = (Stage) registerButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void initialize() {
        if (Client.isThreadAlive()) return;
        SocketConnection.setSocket();

        try { SocketConnection.sendRequest(new Request()); }
        catch (SQLException e) { throw new RuntimeException(e); }

        SocketConnection.setPort(SocketConnection.getResponse().getPort());
        SocketConnection.setSocket();
        Client.setThreadAlive(true);
    }

    public void login() {
        if (usernameField.getText().isBlank()) return;
        if (passwordField.getText().isBlank()) return;

        try {
            ArrayList<String> loginInfo = new ArrayList<>(Arrays.asList(
                    usernameField.getText(),
                    passwordField.getText()));
            SocketConnection.sendRequest(new Request(loginInfo));

            Response response = SocketConnection.getResponse();
            if (response.isFunctionCalled() && response.isExists()) {
                Client.setClientId(response.getUserId());
                FXMLLoader fxmlLoader = new FXMLLoader(LandingController.class.getResource("Main.fxml"));
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(fxmlLoader.load()));
            }
        } catch (IOException | SQLException e) { throw new RuntimeException(e); }
    }
}
