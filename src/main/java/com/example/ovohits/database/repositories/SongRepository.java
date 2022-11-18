package com.example.ovohits.database.repositories;

import com.example.ovohits.database.models.Song;

import java.sql.SQLException;
import java.util.List;

public interface SongRepository {
    int add(Song song) throws Exception;

    void delete(int id) throws  SQLException;

    void update(Song song) throws Exception;

    Song getSong(int id) throws Exception;

    List<Song> getSongs() throws SQLException;
}
