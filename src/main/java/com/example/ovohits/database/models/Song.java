package com.example.ovohits.database.models;

import java.sql.Blob;
import java.util.Arrays;

public class Song {
    private int id;
    private Blob data;
    private String name;
    private int user_id;

    public Song() { }

    public Song(Blob data, String name, int user_id) {
        this.data = data;
        this.name = name;
        this.user_id = user_id;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public Blob getData() { return data; }

    public void setData(Blob data) { this.data = data; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getUser_id() { return user_id; }

    public void setUser_id(int user_id) { this.user_id = user_id; }

    @Override
    public String toString() {
        return "Song [" +
                "id=" + id + ", " +
                "data=" + data + ", " +
                "name=" + name + ", " +
                "user_id=" + user_id + "]";
    }
}
