package com.example.ovohits.backend.database.repositories;

import com.example.ovohits.backend.database.models.SavedSong;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SavedSongRepository {
    int add(SavedSong savedSong) throws SQLException;

    void delete(int id) throws  SQLException;

    void update(SavedSong savedSong) throws SQLException;

    SavedSong getSavedSong(int songId) throws SQLException;

    ArrayList<SavedSong> getSavedSongs(int userId) throws SQLException;
}
