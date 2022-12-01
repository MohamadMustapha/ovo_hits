package com.example.ovohits;

import com.example.ovohits.backend.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;

public class LibraryController {
    @FXML
    private Button logoutButton;
    @FXML
    private Button songsButton;
    @FXML
    private Button uploadButton;
    @FXML
    private Button usersButton;
    @FXML
    private ListView<String> libraryView;

    public void initialize() {
        try {
            Client.sendRequest(new Request("@getSavedSongs"));
            Response response = Client.getResponse();
            ArrayList<Pair<String, Integer>> savedSongList = response.getSavedSongList();
            for (Pair<String, Integer> savedSong : savedSongList)
                libraryView.getItems().add(savedSong.getKey() + ".mp3     |     id: " + savedSong.getValue());
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void goAddSong() {
        Utilities.goPage(
                uploadButton,
                new FXMLLoader(AddSong.class.getResource("AddSong.fxml")));
    }

    public void goSongs() {
        Utilities.goPage(
                songsButton,
                new FXMLLoader(Songs.class.getResource("Songs.fxml")));
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

    public void deleteFromLibrary() {
        String song = libraryView.getSelectionModel().getSelectedItem();
        if (song == null) return;

        try {
            Client.sendRequest(new Request(
                    Integer.parseInt(song.substring(song.lastIndexOf(" ") + 1)),
                    "@deleteSavedSong"));
        } catch (SQLException e) { throw new RuntimeException(e); }

        libraryView.getItems().remove(libraryView.getSelectionModel().getSelectedItem());
    }
}
