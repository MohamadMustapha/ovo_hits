package com.example.ovohits;

import com.example.ovohits.backend.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.sql.rowset.serial.SerialBlob;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

public class RegisterController {
    private File songFile = null;
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

    private boolean verifyEmail() {
        Pattern pattern = Pattern.compile(
                "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
        return pattern.matcher(emailInput.getText()).matches();
    }

    public void addUser() {
        if (firstNameInput.getText().isBlank()) return;
        if (lastNameInput.getText().isBlank()) return;
        if (usernameInput.getText().isBlank()) return;
        if (passwordInput.getText().isBlank()) return;
        if (emailInput.getText().isBlank() || !verifyEmail()) return;

        if (!songFile.exists()) return;
        if (listView.getItems().isEmpty()) return;

        try {
            FileInputStream fileInputStream = new FileInputStream(songFile);
            byte[] songData = new byte[(int) songFile.length()];
            if (fileInputStream.read(songData) == -1) throw new RuntimeException();
            fileInputStream.close();

            ArrayList<String> songInfo = new ArrayList<>(Arrays.asList(
                    songNameInput.getText(),
                    null));
            ArrayList<String> userInfo = new ArrayList<>(Arrays.asList(
                    emailInput.getText(),
                    firstNameInput.getText(),
                    lastNameInput.getText(),
                    passwordInput.getText(),
                    usernameInput.getText()));
            SocketConnection.sendRequest(new Request(
                    songInfo,
                    userInfo,
                    new SerialBlob(songData)));
        } catch (IOException | SQLException e) { throw new RuntimeException(e); }

        Response response = SocketConnection.getResponse();
        if (response.isFunctionCalled()) {
            Client.setClientId(response.getUserId());
            goMain();
        }
    }

    public void goLanding() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Landing.class.getResource("Landing.fxml"));
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void goMain() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("Main.fxml"));
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void uploadSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));
        songFile = fileChooser.showOpenDialog(null);
        if (songFile != null) {
            if (listView.getItems().isEmpty()) listView.getItems().add(songFile.getName());
            else listView.getItems().set(0, songFile.getName());
        }
    }
}
