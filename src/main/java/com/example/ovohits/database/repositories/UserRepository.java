package com.example.ovohits.database.repositories;

import com.example.ovohits.database.models.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface UserRepository {
    int add(User user) throws SQLException;

    void delete(int id) throws SQLException;

    void update(User user) throws SQLException;

    User getUser(int id) throws SQLException;

    User getUser(String username) throws SQLException;

    ArrayList<User> getUsers() throws SQLException;
}
