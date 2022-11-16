package com.example.ovohits.database.repositories;

import com.example.ovohits.database.models.User;

import java.sql.SQLException;
import java.util.List;

public interface UserRepository {
    public int add(User user) throws SQLException;

    public void delete(int id) throws SQLException;

    public void update(User user) throws SQLException;

    public User getUser(int id) throws SQLException;

    public List<User> getUsers() throws SQLException;
}