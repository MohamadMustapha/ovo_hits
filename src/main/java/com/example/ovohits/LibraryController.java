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
            Client.sendRequest(new Request(
                    Client.getClientId(),
                    "@getSavedSongs"));
            Response savedSongResponse = Client.getResponse();
            ArrayList<Pair<Integer, Integer>> savedSongList = savedSongResponse.getSavedSongList();
            ArrayList<Integer> savedSongIdList = new ArrayList<>(savedSongList.stream().map(Pair::getKey).toList());

            Client.sendRequest(new Request(
                    Client.getClientId(),
                    "@getSongs"));
            Client.callRequestFunction(Client.getServerRequest());
            Response songsResponse = Client.getResponse();
            ArrayList<Pair<String, Integer>> songList = songsResponse.getSongList();
            songList = new ArrayList<>(songList.stream().filter(song ->
                    savedSongIdList.contains(song.getValue())).toList());
            for (Pair<String, Integer> song : songList)
                libraryView.getItems().add(song.getKey() + ".mp3     |     id: " + song.getValue());
        } catch (SQLException e) { throw new RuntimeException(e); }
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
}
