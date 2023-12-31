package com.example.ovohits;

import com.example.ovohits.backend.database.models.User;
import com.example.ovohits.backend.Response;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class UsersController {
    @FXML
    private Button libraryButton;
    @FXML
    private Button logoutButton;
    @FXML
    private Button songsButton;
    @FXML
    private Button uploadButton;
    @FXML
    private Button viewButton;
    @FXML
    private ListView<String> usersView;

    public void initialize() {
        try {
            Client.sendRequest(new Request("@getOnlineUsers"));
            Response response = Client.getResponse();
            ArrayList<String> onlineUsernames = response.getOnlineUsernameList();
            for (String username : onlineUsernames)
                usersView.getItems().add(username + "     |     Status: Online");
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

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

    public void goSongs() {
        Utilities.goPage(
                songsButton,
                new FXMLLoader(Songs.class.getResource("Songs.fxml")));
    }

    public void goUser() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(UserSongs.class.getResource("UserSongs.fxml"));
            Stage stage = (Stage) viewButton.getScene().getWindow();

            String selectedUser = usersView.getSelectionModel().getSelectedItem();
            if (selectedUser == null) return;

            UserSongsController userController = new UserSongsController();

            Request request = new Request("@getUser");
            request.setUsername(selectedUser.split(" ", 2)[0]);
            Client.sendRequest(request);

            Response response = Client.getResponse();
            User user = SerializationUtils.deserialize(response.getUserData());
            userController.setUserId(user.getId());
            fxmlLoader.setController(userController);

            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException | SQLException e) { throw new RuntimeException(e); }
    }

    public void logout() {
        Client.setClientId(null);
        Utilities.goPage(
                logoutButton,
                new FXMLLoader(Landing.class.getResource("Landing.fxml")));
    }
}
