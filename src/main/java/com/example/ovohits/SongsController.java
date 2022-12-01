package com.example.ovohits;

import com.example.ovohits.backend.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.util.Pair;
import java.sql.SQLException;
import java.util.ArrayList;

public class SongsController {
    @FXML
    private Button libraryButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button uploadButton;
    @FXML
    private Button usersButton;
    @FXML
    private ListView<String> songsView;

    public void initialize() {
        try {
            Client.sendRequest(new Request("@getSongs"));
            Client.callRequestFunction(Client.getServerRequest());
            Response response = Client.getResponse();
            ArrayList<Pair<String, Integer>> songList = response.getSongList();
            for (Pair<String, Integer> song : songList)
                songsView.getItems().add(song.getKey() + ".mp3     |     id: " + song.getValue());
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void addToLibrary() { Utilities.addToLibrary(songsView); }

    public void goAddSong() {
        Utilities.goPage(
                uploadButton,
                new FXMLLoader(AddSong.class.getResource("AddSong.fxml")));
    }

    public void goLibrary() {
        Utilities.goPage(
                libraryButton,
                new FXMLLoader(Library.class.getResource("Library.fxml")));
    }

    public void goUsers() {
        Utilities.goPage(
                usersButton,
                new FXMLLoader(Users.class.getResource("Users.fxml")));
    }

    public void logout() {
        Client.setClientId(null);
        Utilities.goPage(
                logoutButton,
                new FXMLLoader(Landing.class.getResource("Landing.fxml")));
    }
}
