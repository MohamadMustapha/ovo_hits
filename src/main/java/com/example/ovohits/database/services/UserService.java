package com.example.ovohits.database.services;

import com.example.ovohits.database.DatabaseConnection;
import com.example.ovohits.database.models.User;
import com.example.ovohits.database.repositories.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserService implements UserRepository {
    private static final Connection connection = DatabaseConnection.getConnection();

    private User getUserData(PreparedStatement preparedStatement) throws SQLException {
        User user = new User();
        ResultSet resultSet = preparedStatement.executeQuery();

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
    }

    private void prepareQuery(PreparedStatement preparedStatement, User user) throws SQLException {
        preparedStatement.setString(1, user.getEmail());
        preparedStatement.setString(2, user.getFirstName());
        preparedStatement.setString(3, user.getLastName());
        preparedStatement.setString(4, user.getPassword());
        preparedStatement.setString(5, user.getUsername());
    }

    @Override
    public int add(User user) throws SQLException {
        String query = "INSERT INTO USER(" +
                       "email, first_name, last_name, password_, username) " +
                       "VALUES (?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        prepareQuery(preparedStatement, user);
        return preparedStatement.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM USER WHERE id =?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(User user) throws SQLException {
        String query = "UPDATE USER SET " +
                       "email=?, first_name=?, last_name=?, password_=?, username=? " +
                       "WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        prepareQuery(preparedStatement, user);
        preparedStatement.setInt(6, user.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public User getUser(int id) throws SQLException {
        String query = "SELECT * FROM USER WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        return getUserData(preparedStatement);
    }

    @Override
    public User getUser(String username) throws SQLException {
        String query = "SELECT * FROM USER WHERE username=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, username);
        return getUserData(preparedStatement);
    }

    @Override
    public ArrayList<User> getUsers() throws SQLException {
        String query = "SELECT * FROM USER";
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
    }
}
