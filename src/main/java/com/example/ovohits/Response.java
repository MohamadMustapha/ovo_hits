package com.example.ovohits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class Response implements Serializable {
    private ArrayList<byte[]> songDataArrayList;
    private boolean exists;
    private byte[] songData;
    private byte[] userData;
    private int userId;

    public Response() { }

    public Response(ArrayList<byte[]> songDataArrayList) { this.songDataArrayList = songDataArrayList; }

    public Response(boolean exists) { this.exists = exists; }

    public boolean getExists() { return exists; }

    public void setExists(boolean exists) { this.exists = exists; }

    public ArrayList<byte[]> getSongDataArrayList() { return songDataArrayList; }

    public void setSongDataArrayList(ArrayList<byte[]> songDataArrayList) {
        this.songDataArrayList = songDataArrayList;
    }

    public byte[] getSongData() { return songData; }

    public void setSongData(byte[] songData) { this.songData = songData; }

    public byte[] getUserData() { return userData; }

    public void setUserData(byte[] userData) { this.userData = userData; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Request [" +
                "songArrayList=" + songDataArrayList.toString() + ", " +
                "exists=" + exists + ", " +
                "song=" + Arrays.toString(songData) + ", " +
                "user=" + Arrays.toString(userData) +
                "userId=" + userId + "]";
    }
}
