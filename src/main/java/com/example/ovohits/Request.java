package com.example.ovohits;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = -5576525072357338113L;
    private ArrayList<String> loginInfo;
    private ArrayList<Integer> savedSongInfo;
    private ArrayList<String> songInfo;
    private ArrayList<String> userInfo;
    private Blob songData;
    private int modelId;
    private String function;

    public Request() { }

    public Request(ArrayList<String> loginInfo) {
        this.loginInfo = loginInfo;
        this.function = "@login";
    }

    public Request(ArrayList<String> songInfo, Blob songData) {
        this.songInfo = songInfo;
        this.songData = songData;
        this.function = "@addSong";
    }
    public Request(ArrayList<String> songInfo, ArrayList<String> userInfo, Blob songData) {
        this.songInfo = songInfo;
        this.userInfo = userInfo;
        this.songData = songData;
        this.function = "@addUser";
    }

    public Request(int modelId, String function) {
        this.modelId = modelId;
        this.function = function;
    }

    public Request(String function) { this.function = function; }

    public ArrayList<String> getLoginInfo() { return loginInfo; }

    public void setLoginInfo(ArrayList<String> loginInfo) { this.loginInfo = loginInfo; }

    public ArrayList<Integer> getSavedSongInfo() { return savedSongInfo; }

    public void setSavedSongInfo(ArrayList<Integer> savedSongInfo) { this.savedSongInfo = savedSongInfo; }

    public ArrayList<String> getSongInfo() { return songInfo; }

    public void setSongInfo(ArrayList<String> songInfo) { this.songInfo = songInfo; }

    public ArrayList<String> getUserInfo() { return userInfo; }

    public void setUserInfo(ArrayList<String> userInfo) { this.userInfo = userInfo; }

    public Blob getSongData() { return songData; }

    public void setSongData(Blob songData) { this.songData = songData; }

    public int getModelId() { return modelId; }

    public void setModelId(int modelId) { this.modelId = modelId; }

    public String getFunction() { return function; }

    public void setFunction(String function) { this.function = function; }

    @Override
    public String toString() {
        return "Request [" +
                "loginInfo=" + loginInfo.toString() + ", " +
                "savedSongInfo=" + savedSongInfo.toString() + ", " +
                "songInfo=" + songInfo.toString() + ", " +
                "userInfo=" + userInfo.toString() + ", " +
                "songData=" + songData + ", " +
                "modelId=" + modelId + ", " +
                "function" + function + "]";
    }
}
