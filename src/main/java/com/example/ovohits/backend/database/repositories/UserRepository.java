package com.example.ovohits.backend.database.repositories;

import com.example.ovohits.backend.database.models.User;

import java.sql.SQLException;
import java.util.ArrayList;

public interface UserRepository {
    int add(User user);

    void delete(int id);

    void update(User user);

    User getUser(int id);

    User getUser(String username);

    ArrayList<User> getUsers();
}
