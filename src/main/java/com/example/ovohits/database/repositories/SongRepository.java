package com.example.ovohits.database.repositories;

import com.example.ovohits.database.models.Song;

import java.sql.SQLException;
import java.util.List;

public interface SongRepository {
    int add(Song song) throws SQLException;

    void delete(int id) throws  SQLException;

    void update(Song song) throws SQLException;

    Song getSong(int id) throws SQLException;

    List<Song> getSongs() throws SQLException;
}
