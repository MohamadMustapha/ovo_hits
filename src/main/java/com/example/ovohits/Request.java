package com.example.ovohits;

import javax.sql.rowset.serial.SerialBlob;
import java.io.Serial;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

public class Request implements Serializable {
    @Serial
    private static final long serialVersionUID = -5576525072357338113L;
    private ArrayList<String> loginInfo = new ArrayList<>();
    private ArrayList<Integer> savedSongInfo = new ArrayList<>();
    private ArrayList<String> songInfo = new ArrayList<>();
    private ArrayList<String> userInfo = new ArrayList<>();
    private SerialBlob songData = new SerialBlob(new byte[0]);
    private int modelId = -1;
    private String function = "";

    public Request() throws SQLException { }

    public Request(ArrayList<String> loginInfo) throws SQLException {
        this.loginInfo = loginInfo;
        this.function = "@login";
    }

    public Request(ArrayList<String> songInfo, SerialBlob songData) throws SQLException {
        this.songInfo = songInfo;
        this.songData = songData;
        this.function = "@addSong";
    }
    public Request(ArrayList<String> songInfo, ArrayList<String> userInfo, SerialBlob songData) throws SQLException {
        this.songInfo = songInfo;
        this.userInfo = userInfo;
        this.songData = songData;
        this.function = "@addUser";
    }

    public Request(int modelId, String function) throws SQLException {
        this.modelId = modelId;
        this.function = function;
    }

    public Request(String function) throws SQLException { this.function = function; }

    public ArrayList<String> getLoginInfo() { return loginInfo; }

    public void setLoginInfo(ArrayList<String> loginInfo) { this.loginInfo = loginInfo; }

    public ArrayList<Integer> getSavedSongInfo() { return savedSongInfo; }

    public void setSavedSongInfo(ArrayList<Integer> savedSongInfo) { this.savedSongInfo = savedSongInfo; }

    public ArrayList<String> getSongInfo() { return songInfo; }

    public void setSongInfo(ArrayList<String> songInfo) { this.songInfo = songInfo; }

    public ArrayList<String> getUserInfo() { return userInfo; }

    public void setUserInfo(ArrayList<String> userInfo) { this.userInfo = userInfo; }

    public SerialBlob getSongData() { return songData; }

    public void setSongData(SerialBlob songData) { this.songData = songData; }

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
