package com.example.ovohits.database.repositories;

import com.example.ovohits.database.models.Song;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SongRepository {
    int add(Song song) throws Exception;

    void delete(int id) throws  SQLException;

    void update(Song song) throws Exception;

    Song getSong(int id) throws Exception;

    ArrayList<Song> getSongs() throws SQLException;

    ArrayList<Song> getSongsById(int id) throws SQLException;
}
