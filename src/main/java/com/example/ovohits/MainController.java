package com.example.ovohits;

import com.example.ovohits.backend.Response;
import com.example.ovohits.backend.database.models.SavedSong;
import com.example.ovohits.backend.database.models.Song;
import com.example.ovohits.backend.database.models.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.lang3.SerializationUtils;
import java.io.File;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

public class MainController {

    @FXML
    public ListView<String> usersView;
    @FXML
    private Button returnButton;
    @FXML
    private Button uploadSongButton;
    @FXML
    private ListView<String> myPlaylistView;
    @FXML
    private ListView<String> songsView;
    @FXML
    private ProgressBar songProgressBar;
    private Media media;
    private MediaPlayer mediaPlayer;
    private boolean isPlaying = false;
    private Timer timer;
    @FXML
    private Label songLabel;

    private void getAllSongs() {
        try {
            Response response = sendRequest(new Request("@getOnlineUsers"));
            usersView.getItems().addAll(response.getOnlineUsernameList().stream().map(username ->
                    "Status: Online | User: " + username).toArray(String[]::new));

            response = sendRequest(new Request("@getAllSongs"));
            ArrayList<Song> songList = new ArrayList<>(response.getSongDataList().stream()
                    .map(bytes -> (Song) SerializationUtils.deserialize(bytes)).toList());
            for (Song song : songList) {
                response = sendRequest(new Request(song.getUserId(), "@getUser"));
                User user = SerializationUtils.deserialize(response.getUserData());
                songsView.getItems().add(song.getName() + ".mp3   |   from: " + user.getUsername() + "   |   id: "
                        + song.getId());
            }
        } catch (SQLException e) { throw new RuntimeException(e); }

    }

    private Response sendRequest(Request request) {
        SocketConnection.sendRequest(request);
        return SocketConnection.getResponse();
    }

    public void initialize() {
        getAllSongs();
        try {
            Response response = sendRequest(new Request(
                    Client.getClientId(),
                    "@getSavedSongs"));
            ArrayList<SavedSong> savedSongList = new ArrayList<>(response.getSavedSongDataList().stream()
                    .map(bytes -> (SavedSong) SerializationUtils.deserialize(bytes)).toList());
            for (SavedSong savedSong : savedSongList) {
                response = sendRequest(new Request(savedSong.getSongId(), "@getSong"));
                Song song = SerializationUtils.deserialize(response.getSongData());
                response = sendRequest(new Request(savedSong.getUserId(), "@getUser"));
                User user = SerializationUtils.deserialize(response.getUserData());
                myPlaylistView.getItems().add(song.getName() + ".mp3   |   from: " + user.getUsername() + "   |   id: "
                        + song.getId());
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
    public void addToMyPlaylist() {
        String selectedSong = songsView.getSelectionModel().getSelectedItem();
        if (selectedSong == null) return;

        try {
            Request request = new Request("@addSavedSong");
            request.setSavedSongInfo(new ArrayList<>(Arrays.asList(
                    Integer.parseInt(selectedSong.substring(selectedSong.lastIndexOf(" ") + 1)),
                    Client.getClientId())));
            SocketConnection.sendRequest(request);
        } catch (SQLException e) { throw new RuntimeException(e); }

        Response response = SocketConnection.getResponse();
        if (response.isFunctionCalled()) myPlaylistView.getItems().add(songsView.getSelectionModel().getSelectedItem());
    }

    public void filterSongs() {
        String selectedUser = usersView.getSelectionModel().getSelectedItem();

        try {
            Request request = new Request("@getUser");
            request.setUsername(selectedUser.substring(Integer.parseInt(selectedUser.substring(
                    selectedUser.lastIndexOf(" ") + 1))));
            Response response = SocketConnection.getResponse();
            if (response.isFunctionCalled()) {
                User user = SerializationUtils.deserialize(response.getUserData());
                request = new Request("@getSongs");
                request.setModelId(user.getId());
                SocketConnection.sendRequest(request);

                response = SocketConnection.getResponse();
                ArrayList<Song> songList = new ArrayList<>(response.getSongDataList().stream()
                        .map(bytes -> (Song) SerializationUtils.deserialize(bytes)).toList());
                songsView.getItems().clear();
                for (Song song : songList) {
                    response = sendRequest(new Request(song.getUserId(), "@getUser"));
                    user = SerializationUtils.deserialize(response.getUserData());
                    songsView.getItems().add(song.getName() + ".mp3   |   from: " + user.getUsername() + "   |   id: "
                            + song.getId());
                }
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void logout() {
        Client.setClientId(null);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Landing.class.getResource("Landing.fxml"));
            Stage stage = (Stage) returnButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void resetSongs() { getAllSongs(); }

    public void selectSong() {
        if (isPlaying) {
            mediaPlayer.stop();
            isPlaying = false;
        }
        try {
            //ID to get
            String selectedSong = myPlaylistView.getSelectionModel().getSelectedItem();
            System.out.println(selectedSong);
            int id = Integer.parseInt(selectedSong.substring(selectedSong.lastIndexOf(" ") + 1));
            SocketConnection.sendRequest(new Request(id, "@getSong"));
            Response response = SocketConnection.getResponse();
            Song song = SerializationUtils.deserialize(response.getSongData());

            File f = new File( id+ ".mp3");
            songLabel.setText(id+ "");

            System.out.println(f.getAbsolutePath());
            if(f.exists()){
                media = new Media(f.toURI().toString());
                mediaPlayer = new MediaPlayer(media);
                return;
            }
            FileOutputStream fos = new FileOutputStream(f);
            byte[] data = song.getData().getBinaryStream().readAllBytes();
            for(byte i: data){
                fos.write(i);
            }
            media = new Media(f.toURI().toString());
            fos.close();

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void playSong() {
        selectSong();
        mediaPlayer = new MediaPlayer(media);
        beginTimer();
        mediaPlayer.play();
        isPlaying = true;
    }
    public void pauseSong() {
        if(isPlaying) {
//            selectSong();
//            mediaPlayer = new MediaPlayer(media);
            cancelTimer();
            mediaPlayer.pause();
            isPlaying = false;
        }
    }
    public void resetSong() {
//        selectSong();
//        mediaPlayer = new MediaPlayer(media);
        songProgressBar.setProgress(0);
        mediaPlayer.seek(Duration.seconds(0));
        isPlaying = true;
    }

    public void removeFromMyPlaylist() {
        String selectedSong = myPlaylistView.getSelectionModel().getSelectedItem();
        if (selectedSong == null) return;

        try {
            SocketConnection.sendRequest(new Request(
                    Integer.parseInt(selectedSong.substring(selectedSong.lastIndexOf(" ") + 1)),
                    "@deleteSavedSong"));
        } catch (SQLException e) { throw new RuntimeException(e); }

        myPlaylistView.getItems().remove(myPlaylistView.getSelectionModel().getSelectedIndex());
    }

    public void uploadSong() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(AddSong.class.getResource("AddSong.fxml"));
            Stage stage = (Stage) uploadSongButton.getScene().getWindow();
            stage.setScene(new Scene(fxmlLoader.load()));
        } catch (IOException e) { throw new RuntimeException(e); }
    }
    public void beginTimer(){
       timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                isPlaying = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                //System.out.println(current / end);
                songProgressBar.setProgress(current / end);

                if (current / end == 1) cancelTimer();

            }

        };
       timer.scheduleAtFixedRate(task, 1000, 1000);
    }
    public void cancelTimer(){
        isPlaying = false;
        timer.cancel();
    }
}
