package com.example.ovohits.backend;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;

public class ServerRequest implements Serializable {
    @Serial
    private static final long serialVersionUID = 7862484713161595351L;
    private ArrayList<String> songInfo = new ArrayList<>();
    private int modelId = -1;
    private String function = "";

    public ServerRequest() { }

    public ServerRequest(String function) { this.function = function; }

    public ServerRequest(int modelId, String function) {
        this.modelId = modelId;
        this.function = function;
    }

    public ArrayList<String> getSongInfo() { return songInfo; }

    public void setSongInfo(ArrayList<String> songInfo) { this.songInfo = songInfo; }

    public int getModelId() { return modelId; }

    public void setModelId(int modelId) { this.modelId = modelId; }

    public String getFunction() { return function; }

    public void setFunction(String function) { this.function = function; }

    @Override
    public String toString() {
        return "Request [" +
                "songInfo=" + songInfo.toString() + ", " +
                "modelId=" + modelId + ", " +
                "function" + function + "]";
    }
}
