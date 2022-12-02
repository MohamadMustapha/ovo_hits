package com.example.ovohits.backend;

import javafx.util.Pair;

import java.io.Serial;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 4806366312215947674L;
    private ArrayList<String> onlineUsernameList = new ArrayList<>();
    private ArrayList<Pair<Integer, Integer>> savedSongList = new ArrayList<>();
    private ArrayList<Pair<String, Integer>> songList = new ArrayList<>();
    private boolean exists = false;
    private boolean functionCalled = true;
    private byte[] socketData = new byte[0];
    private byte[] songData = new byte[0];
    private byte[] userData = new byte[0];
    private int port = -1;
    private int userId = -1;

    public Response() { }

    public Response(ArrayList<Pair<String, Integer>> songList) { this.songList = songList; }

    public Response(boolean exists) { this.exists = exists; }

    public Response(int port) { this.port = port; }

    public ArrayList<Pair<Integer, Integer>> getSavedSongList() { return savedSongList; }

    public void setSavedSongList(ArrayList<Pair<Integer, Integer>> savedSongList) {
        this.savedSongList = savedSongList;
    }

    public ArrayList<Pair<String, Integer>> getSongList() { return songList; }

    public void setSongList(ArrayList<Pair<String, Integer>> songList) { this.songList = songList; }

    public ArrayList<String> getOnlineUsernameList() { return onlineUsernameList; }

    public void setOnlineUsernameList(ArrayList<String> onlineUsernameList) {
        this.onlineUsernameList = onlineUsernameList;
    }

    public boolean isExists() { return exists; }

    public void setExists(boolean exists) { this.exists = exists; }

    public boolean isFunctionCalled() { return functionCalled; }

    public void setFunctionCalled(boolean functionCalled) { this.functionCalled = functionCalled; }

    public byte[] getSocketData() { return socketData; }

    public void setSocketData(byte[] socketData) { this.socketData = socketData; }

    public byte[] getSongData() { return songData; }

    public void setSongData(byte[] songData) { this.songData = songData; }

    public byte[] getUserData() { return userData; }

    public void setUserData(byte[] userData) { this.userData = userData; }

    public int getPort() { return port; }

    public void setPort(int port) { this.port = port; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Request [" +
                "onlineUsernameList=" + onlineUsernameList.toString() + ", " +
                "savedSongList=" + savedSongList.toString() + ", " +
                "songDataList=" + songList.toString() + ", " +
                "exists=" + exists + ", " +
                "functionCalled=" + functionCalled + ", " +
                "songData=" + Arrays.toString(songData) + ", " +
                "userData=" + Arrays.toString(userData) +
                "port=" + port + ", " +
                "userId=" + userId + "]";
    }
}
