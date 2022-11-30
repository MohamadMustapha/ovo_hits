package com.example.ovohits.backend.database.repositories;

import com.example.ovohits.backend.database.models.Song;

import java.sql.SQLException;
import java.util.ArrayList;

public interface SongRepository {
    int add(Song song);

    void delete(int id);

    void update(Song song);

    Song getSong(int id);

    ArrayList<Song> getSongs();

    ArrayList<Song> getSongs(int userId);
}
