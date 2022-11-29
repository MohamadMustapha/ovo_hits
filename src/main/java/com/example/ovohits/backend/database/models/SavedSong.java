package com.example.ovohits.backend.database.models;

import java.io.Serial;
import java.io.Serializable;

public class SavedSong implements Serializable {
    @Serial
    private static final long serialVersionUID = -7695323309134993674L;
    private int id;
    private int songId;
    private int userId;

    public SavedSong() { }

    public SavedSong(int songId, int userId) {
        this.songId = songId;
        this.userId = userId;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public int getSongId() { return songId; }

    public void setSongId(int songId) { this.songId = songId; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Song [" +
                "id=" + id + ", " +
                "songId=" + songId + ", " +
                "userId=" + userId + "]";
    }
}
