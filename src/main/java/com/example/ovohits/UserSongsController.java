package com.example.ovohits;

import com.example.ovohits.backend.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.util.Pair;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserSongsController {
    private Integer userId = null;
    @FXML
    private Button logoutButton;
    @FXML
    private Button returnButton;
    @FXML
    private ListView<String> userSongsView;

    public void initialize() {
        try {
            if (userId == null) return;
            Client.sendRequest(new Request(userId, "@getSongs"));
            Client.callRequestFunction(Client.getServerRequest());
            Response response = Client.getResponse();
            ArrayList<Pair<String, Integer>> songList = response.getSongList();
            for (Pair<String, Integer> song : songList)
                userSongsView.getItems().add(song.getKey() + ".mp3     |     id: " + song.getValue());
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void addToLibrary() { Utilities.addToLibrary(userSongsView); }

    public void goUsers() {
        Utilities.goPage(
                returnButton,
                new FXMLLoader(Users.class.getResource("Users.fxml")));
    }

    public void logout() {
        Client.setClientId(null);
        Utilities.goPage(
                logoutButton,
                new FXMLLoader(Landing.class.getResource("Landing.fxml")));
    }

    public void setUserId(Integer userId) { this.userId = userId; }
}
