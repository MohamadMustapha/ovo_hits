package com.example.ovohits;

import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Arrays;

public class Request implements Serializable {
    private ArrayList<String> addSongArray;
    private ArrayList<String> addUserArray;
    private ArrayList<String> loginArray;
    private Blob songData;
    private int modelId;
    private String functionCall;

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

    public Request(int modelId, String functionCall) {
        this.modelId = modelId;
        this.functionCall = functionCall;
    }

    public Request(String functionCall) { this.functionCall = functionCall; }

    public ArrayList<String> getAddSongArray() { return addSongArray; }

    public void setAddSongArray(ArrayList<String> addSongArray) { this.addSongArray = addSongArray; }

    public ArrayList<String> getAddUserArray() { return addUserArray; }

    public void setAddUserArray(ArrayList<String> addUserArray) { this.addUserArray = addUserArray; }

    public ArrayList<String> getLoginArray() { return loginArray; }

    public void setLoginArray(ArrayList<String> loginArray) { this.loginArray = loginArray; }

    public Blob getSongData() { return songData; }

    public void setSongData(Blob songData) { this.songData = songData; }

    public int getModelId() { return modelId; }

    public void setModelId(int modelId) { this.modelId = modelId; }

    public String getFunctionCall() { return functionCall; }

    public void setFunctionCall(String functionCall) { this.functionCall = functionCall; }

    @Override
    public String toString() {
        return "Request [" +
                "addSongArray=" + addSongArray.toString() + ", " +
                "addUserArray=" + addUserArray.toString() + ", " +
                "loginArray=" + loginArray.toString() + ", " +
                "songData=" + songData + ", " +
                "modelId=" + modelId + ", " +
                "functionCall" + functionCall + "]";
    }
}
