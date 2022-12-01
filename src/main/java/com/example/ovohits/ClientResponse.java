package com.example.ovohits;

import javafx.util.Pair;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientResponse implements Serializable {
    @Serial
    private static final long serialVersionUID = -7424243253847718175L;
    private ArrayList<Pair<String, Integer>> songList = new ArrayList<>();
    private boolean exists = false;
    private boolean functionCalled = true;
    private byte[] songData = new byte[0];

    public ClientResponse() { }

    public ClientResponse(ArrayList<Pair<String, Integer>> songList) { this.songList = songList; }

    public ClientResponse(boolean exists, byte[] songData) {
        this.exists = exists;
        this.songData = songData;
    }

    public ArrayList<Pair<String, Integer>> getSongList() { return songList; }

    public void setSongList(ArrayList<Pair<String, Integer>> songList) { this.songList = songList; }

    public boolean isExists() { return exists; }

    public void setExists(boolean exists) { this.exists = exists; }

    public boolean isFunctionCalled() { return functionCalled; }

    public void setFunctionCalled(boolean functionCalled) { this.functionCalled = functionCalled; }

    public byte[] getSongData() { return songData; }

    public void setSongData(byte[] songData) { this.songData = songData; }

    @Override
    public String toString() {
        return "Request [" +
                "songDataList=" + songList.toString() + ", " +
                "exists=" + exists + ", " +
                "functionCalled=" + functionCalled + ", " +
                "songData=" + Arrays.toString(songData) + "]";
    }
}
