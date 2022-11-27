package com.example.ovohits.backend.database.models;

import java.io.Serializable;

public class User implements Serializable {
    private int id;
    private String email;
    private String firstName;
    private String lastName;
    private String password;
    private String username;

    public User() { }

    public User(String email, String firstName, String lastName, String password, String username) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
        this.username = username;
    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public String getFirstName() { return firstName; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    @Override
    public String toString() {
        return "User [" +
                "id=" + id + ", " +
                "email=" + email + ", " +
                "firstName=" + firstName + ", " +
                "lastName=" + lastName + ", " +
                "password=" + password + ", " +
                "username=" + username + "]";
    }
}
