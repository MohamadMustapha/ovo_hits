package com.example.ovohits.backend.database.repositories;

import com.example.ovohits.backend.database.models.Song;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SongRepository {
    int add(Song song) throws SQLException;

    void delete(int id) throws  SQLException;

    void update(Song song) throws SQLException;

    Song getSong(int id) throws SQLException;

    ArrayList<Song> getSongs() throws SQLException;
}
