package com.example.ovohits;

import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.services.SongService;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javax.sql.rowset.serial.SerialBlob;

import java.io.*;

public class AddSongController {
    private File songData = null;

    @FXML
    private ListView<String> songInputView;

    @FXML
    private TextField songNameInput;

    public void uploadSong() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("MP3 Files", "*.mp3"));

        songData = fileChooser.showOpenDialog(null);
        if (songData != null) {
            if (songInputView.getItems().size() == 0)
                songInputView.getItems().add(songData.getName());
            else {
                songInputView.getItems().remove(0);
                songInputView.getItems().add(songData.getName());
            }
        }
    }

    public void addSong() throws Exception {
        if (songData == null || songInputView.getItems().size() == 0) return;

        FileInputStream fileInputStream = new FileInputStream(songData);
        byte[] songDataBytes = new byte[(int) songData.length()];
        if (fileInputStream.read(songDataBytes) == -1) {
            System.out.println("Read file failed!");
            return;
        }
        fileInputStream.close();

        SongService songService = new SongService();
        songService.add(new Song(
                new SerialBlob(songDataBytes),
                songNameInput.getText(),
                1
        ));
    }
}
