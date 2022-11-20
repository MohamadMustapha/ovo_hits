package com.example.ovohits;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;

public class MainController {
    @FXML
    private ListView availableSongsView;

    @FXML
    private ListView myPlaylistView;

    @FXML
    private ButtonBase playSongButton;

    @FXML
    private ButtonBase pauseSongButton;

    @FXML
    private ButtonBase stopSongButton;

    @FXML
    private ButtonBase resetSongButton;

    private Media media;
    private MediaPlayer mediaPlayer;

    public void initialize() {
        File file = new File("C:\\Users\\User\\Desktop\\OVO HITS\\OVO HITS\\src\\main\\resources\\audio\\Drake - God's Plan.mp3");
        media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.setStartTime(Duration.seconds(0));
        mediaPlayer.setStopTime(Duration.seconds(30));
    }

    public void goToAddSong()throws IOException {
        Stage stage = (Stage) availableSongsView.getScene().getWindow();
        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("AddSong.fxml")));
        stage.setScene(scene);
    }

    public void pauseSong() {
        mediaPlayer.pause();
    }

    public void playSong() {
        mediaPlayer.play();
    }

    public void resetSong() {
        mediaPlayer.seek(Duration.ZERO);

    }

    private class Media {
        private String title;
        private String artist;
        private String album;
        private String genre;
        private String year;
        private String path;

        public Media(String title) {
            this.title = title;
            this.artist = artist;
            this.album = album;
            this.genre = genre;
            this.year = year;
            this.path = path;
        }

        public String getTitle() {
            return title;
        }

        public String getArtist() {
            return artist;
        }

        public String getAlbum() {
            return album;
        }

        public String getGenre() {
            return genre;
        }

        public String getYear() {
            return year;
        }

        public String getPath() {
            return path;
        }
    }

    private class MediaPlayer {
        public static final Object INDEFINITE = null;
        private Media media;
        private Duration startTime;

        public MediaPlayer(Media media) {
            this.media = media;
        }

        public void play() {

        }

        public void pause() {

        }

        public void stop() {

        }

        public void reset() {

        }

        public void seek(Duration zero) {
        }

        public void setAutoPlay(boolean b) {

        }

        public void setCycleCount(Object indefinite) {

        }

        public void setStopTime(Duration seconds) {

        }

        public void setStartTime(Duration startTime) {
            this.startTime = startTime;
        }

        public Duration getStartTime() {
            return startTime;
        }
    }
}
