package com.example.ovohits;

import java.io.Serializable;
import java.sql.Blob;
import java.util.ArrayList;

public class Request implements Serializable {
    private ArrayList<String> addSongArray;
    private ArrayList<String> addUserArray;
    private ArrayList<String> loginArray;
    private Blob songData;
    private String functionCall;

    public Request() { }

    public Request(ArrayList<String> addSongArray, ArrayList<String> addUserArray, ArrayList<String> loginArray,
                   Blob songData, String functionCall) {
        this.addSongArray = addSongArray;
        this.addUserArray = addUserArray;
        this.loginArray = loginArray;
        this.songData = songData;
        this.functionCall = functionCall;
    }

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

    @Override
    public String toString() {
        return "Request [" +
                "addSongArray=" + addSongArray.toString() + ", " +
                "addUserArray=" + addUserArray.toString() + "]";
    }
}
