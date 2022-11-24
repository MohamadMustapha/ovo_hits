package com.example.ovohits.backend.database.models;

import javax.sql.rowset.serial.SerialBlob;
import java.io.Serializable;

public class Song implements Serializable {
    private int id;
    private SerialBlob data;
    private String name;
    private int user_id;

    public Song() { }

    public Song(SerialBlob data, String name, int user_id) {
        this.data = data;
        this.name = name;
        this.user_id = user_id;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public SerialBlob getData() { return data; }

    public void setData(SerialBlob data) { this.data = data; }

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
