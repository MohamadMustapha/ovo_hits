package com.example.ovohits.backend.database.repositories;

import com.example.ovohits.backend.database.models.SavedSong;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SavedSongRepository {
    int add(SavedSong savedSong);

    void delete(int id);

    void update(SavedSong savedSong);

    SavedSong getSavedSong(int songId);

    ArrayList<SavedSong> getSavedSongs(int userId);
}
