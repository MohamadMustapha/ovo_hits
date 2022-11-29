package com.example.ovohits.backend.database.models;

import javax.sql.rowset.serial.SerialBlob;
import java.io.Serial;
import java.io.Serializable;

public class Song implements Serializable {
    @Serial
    private static final long serialVersionUID = 5801737563321030113L;
    private int id;
    private SerialBlob data;
    private String name;
    private int userId;

    public Song() { }

    public Song(SerialBlob data, String name, int userId) {
        this.data = data;
        this.name = name;
        this.userId = userId;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public SerialBlob getData() { return data; }

    public void setData(SerialBlob data) { this.data = data; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getUserId() { return userId; }

    public void setUserId(int userId) { this.userId = userId; }

    @Override
    public String toString() {
        return "Song [" +
                "id=" + id + ", " +
                "data=" + data + ", " +
                "name=" + name + ", " +
                "userId=" + userId + "]";
    }
}
