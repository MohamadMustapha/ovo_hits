package com.example.ovohits.database.repositories;

import com.example.ovohits.database.models.Song;

import java.sql.SQLException;
import java.util.List;

public interface SongRepository {
    public int add(Song song) throws SQLException;

    public void delete(int id) throws  SQLException;

    public void update(Song song) throws SQLException;

    public Song getSong(int id) throws SQLException;

    public List<Song> getSongs() throws SQLException;
}
