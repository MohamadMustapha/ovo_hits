package com.example.ovohits;

import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.models.User;

import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;

public class Request implements Serializable {
    private ArrayList<Song> getSongArray;
    private ArrayList<String> addSongArray;
    private ArrayList<String> addUserArray;
    private ArrayList<String> loginArray;
    private Blob songData;
    private String functionCall;
    private int id;
    private User user;

    public Request() { }

    public Request(ArrayList<String> addSongArray, ArrayList<String> addUserArray, Blob songData) {
        this.addSongArray = addSongArray;
        this.addUserArray = addUserArray;
        this.songData = songData;
        this.functionCall = "@addUser";
    }

    public Request(ArrayList<String> addSongArray, Blob songData) {
        this.addSongArray = addSongArray;
        this.songData = songData;
        this.functionCall = "@addSong";
    }

    public Request(ArrayList<String> loginArray) {
        this.loginArray = loginArray;
        this.functionCall = "@login";
    }

    public Request(int id, String functionCall) {
        this.id = id;
        this.functionCall = functionCall;
    }

    public Request(String functionCall) { this.functionCall = functionCall; }

    public ArrayList<Song> getGetSongArray() { return getSongArray; }

    public void setGetSongArray(ArrayList<Song> getSongArray) { this.getSongArray = getSongArray; }

    public ArrayList<String> getAddSongArray() { return addSongArray; }

    public void setAddSongArray(ArrayList<String> addSongArray) { this.addSongArray = addSongArray; }

    public ArrayList<String> getAddUserArray() { return addUserArray; }

    public void setAddUserArray(ArrayList<String> addUserArray) { this.addUserArray = addUserArray; }

    public ArrayList<String> getLoginArray() { return loginArray; }

    public void setLoginArray(ArrayList<String> loginArray) { this.loginArray = loginArray; }

    public Blob getSongData() { return songData; }

    public void setSongData(Blob songData) { this.songData = songData; }

    public String getFunctionCall() { return functionCall; }

    public void setFunctionCall(String functionCall) { this.functionCall = functionCall; }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }

    public void setUser(User user) { this.user = user; }

    @Override
    public String toString() {
        return "Request [" +
                "getSongArray=" + getSongArray.toString() + ", " +
                "addSongArray=" + addSongArray.toString() + ", " +
                "addUserArray=" + addUserArray.toString() + ", " +
                "loginArray=" + loginArray.toString() + ", " +
                "songData=" + songData + ", " +
                "functionCall" + functionCall + "]";
    }
}
