package com.example.ovohits.backend;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Response implements Serializable {
    @Serial
    private static final long serialVersionUID = 4806366312215947674L;
    private ArrayList<byte[]> savedSongDataList;
    private ArrayList<byte[]> songDataList;
    private boolean exists;
    private boolean functionCalled = true;
    private byte[] songData;
    private byte[] userData;
    private int port;
    private int userId;

    public Response() { }

    public Response(ArrayList<byte[]> songDataList) { this.songDataList = songDataList; }

    public Response(boolean exists) { this.exists = exists; }

    public Response(int port) { this.port = port; }

    public ArrayList<byte[]> getSavedSongDataList() { return savedSongDataList; }

    public void setSavedSongDataList(ArrayList<byte[]> savedSongDataList) {
        this.savedSongDataList = savedSongDataList;
    }

    public ArrayList<byte[]> getSongDataList() { return songDataList; }

    public void setSongDataList(ArrayList<byte[]> songDataList) { this.songDataList = songDataList; }

    public boolean isExists() { return exists; }

    public void setExists(boolean exists) { this.exists = exists; }

    public boolean isFunctionCalled() { return functionCalled; }

    public void setFunctionCalled(boolean functionCalled) { this.functionCalled = functionCalled; }

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
                "savedSongDataList=" + savedSongDataList.toString() + ", " +
                "songDataList=" + songDataList.toString() + ", " +
                "exists=" + exists + ", " +
                "functionCalled=" + functionCalled + ", " +
                "songData=" + Arrays.toString(songData) + ", " +
                "userData=" + Arrays.toString(userData) +
                "port=" + port + ", " +
                "userId=" + userId + "]";
    }
}
