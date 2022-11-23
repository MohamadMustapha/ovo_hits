package com.example.ovohits;

import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.models.User;

import java.io.Serializable;
import java.util.ArrayList;

public class Response implements Serializable {
    private ArrayList<Song> songArrayList;
    private boolean exists;
    private Song song;
    private User user;

    public Response() { }

    public Response(ArrayList<Song> songArrayList) { this.songArrayList = songArrayList; }

    public Response(boolean exists) { this.exists = exists; }

    public boolean getExists() { return exists; }

    public void setExists(boolean exists) { this.exists = exists; }

    public ArrayList<Song> getSongArrayList() { return songArrayList; }

    public void setSongArrayList(ArrayList<Song> songArrayList) { this.songArrayList = songArrayList; }

    public Song getSong() { return song; }

    public void setSong(Song song) { this.song = song; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "Request [" +
                "songArrayList=" + songArrayList.toString() + ", " +
                "exists=" + exists + ", " +
                "song=" + song + ", " +
                "user=" + user + "]";
    }
}
