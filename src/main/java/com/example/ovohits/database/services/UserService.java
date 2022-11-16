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

    private void prepare_query(PreparedStatement preparedStatement, User user) throws SQLException {
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
        prepare_query(preparedStatement, user);
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
        prepare_query(preparedStatement, user);
        preparedStatement.setInt(6, user.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public User getUser(int id) throws SQLException {
        String query = "SELECT * FROM USER WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);

        User user = new User();
        ResultSet resultSet = preparedStatement.executeQuery();

        boolean check = false;
        while (resultSet.next()) {
            check = true;
            user.setId(resultSet.getInt("id"));
            user.setEmail(resultSet.getString("email"));
            user.setFirstName(resultSet.getString("first_name"));
            user.setLastName(resultSet.getString("last_name"));
            user.setPassword(resultSet.getString("password_"));
            user.setUsername(resultSet.getString("username"));
        }

        return check ? user : null;
    }

    @Override
    public List<User> getUsers() throws SQLException {
        String query = "SELECT * FROM USER";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<User> userList = new ArrayList<>();

        while (resultSet.next()) {
            User user = new User(
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
