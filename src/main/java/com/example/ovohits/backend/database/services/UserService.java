package com.example.ovohits.backend.database.services;

import com.example.ovohits.backend.database.DatabaseConnection;
import com.example.ovohits.backend.database.models.User;
import com.example.ovohits.backend.database.repositories.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class UserService implements UserRepository {
    private static final Connection connection = DatabaseConnection.getConnection();

    private User getUserData(PreparedStatement preparedStatement) {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();

            User user = new User();
            boolean found = false;
            while (resultSet.next()) {
                found = true;
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setPassword(resultSet.getString("password_"));
                user.setUsername(resultSet.getString("username"));
            }
            return found ? user : null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private void prepareQuery(PreparedStatement preparedStatement, User user) {
        try {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getFirstName());
            preparedStatement.setString(3, user.getLastName());
            preparedStatement.setString(4, user.getPassword());
            preparedStatement.setString(5, user.getUsername());
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public int add(User user) {
        String query = "INSERT INTO USER(" +
                       "email, first_name, last_name, password_, username) " +
                       "VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            prepareQuery(preparedStatement, user);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM USER WHERE id =?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(User user) {
        String query = "UPDATE USER SET " +
                       "email=?, first_name=?, last_name=?, password_=?, username=? " +
                       "WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            prepareQuery(preparedStatement, user);
            preparedStatement.setInt(6, user.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public User getUser(int id) {
        String query = "SELECT * FROM USER WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            return getUserData(preparedStatement);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public User getUser(String username) {
        String query = "SELECT * FROM USER WHERE username=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, username);
            return getUserData(preparedStatement);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public ArrayList<User> getUsers() {
        String query = "SELECT * FROM USER";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ArrayList<User> userList = new ArrayList<>();
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("email"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("password_"),
                        resultSet.getString("username")
                );
                user.setId(resultSet.getInt("id"));
                userList.add(user);
            }
            return userList;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
