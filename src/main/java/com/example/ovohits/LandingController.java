package com.example.ovohits;

import com.example.ovohits.backend.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
        Utilities.goPage(
                registerButton,
                new FXMLLoader(Landing.class.getResource("Register.fxml")));
    }

    public void login() {
        if (usernameField.getText().isBlank()) return;
        if (passwordField.getText().isBlank()) return;

        try {
            ArrayList<String> loginInfo = new ArrayList<>(Arrays.asList(
                    usernameField.getText(),
                    passwordField.getText()));
            Client.sendRequest(new Request(loginInfo));
        } catch (SQLException e) { throw new RuntimeException(e); }

        Response response = Client.getResponse();
        if (response.isFunctionCalled() && response.isExists()) {
            Client.setClientId(response.getUserId());
            Utilities.goPage(
                    loginButton,
                    new FXMLLoader(Songs.class.getResource("Songs.fxml")));
        }
    }
}
