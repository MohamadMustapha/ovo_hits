package com.example.ovohits.database.services;

import com.example.ovohits.database.DatabaseConnection;
import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.repositories.SongRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SongService implements SongRepository {
    private static final Connection connection = DatabaseConnection.getConnection();

    private void prepare_query(PreparedStatement preparedStatement, Song song) throws SQLException {
        preparedStatement.setBlob(1, song.getData());
        preparedStatement.setString(2, song.getName());
        preparedStatement.setInt(3, song.getUser_id());
    }

    @Override
    public int add(Song song) throws SQLException {
        String query = "INSERT INTO SONG(" +
                       "data_, name_, user_id) " +
                       "VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        prepare_query(preparedStatement, song);
        return preparedStatement.executeUpdate();
    }

    @Override
    public void delete(int id) throws SQLException {
        String query = "DELETE FROM SONG WHERE id =?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(Song song) throws SQLException {
        String query = "UPDATE SONG SET " +
                       "data_=?, name_=?, user_id=? " +
                       "WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        prepare_query(preparedStatement, song);
        preparedStatement.setInt(4, song.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public Song getSong(int id) throws SQLException {
        String query = "SELECT * FROM SONG WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, id);

        Song song = new Song();
        ResultSet resultSet = preparedStatement.executeQuery();

        boolean check = false;
        while (resultSet.next()) {
            check = true;
            song.setId(resultSet.getInt("id"));
            song.setData(resultSet.getBlob("data_"));
            song.setName(resultSet.getString("name_"));
            song.setUser_id(resultSet.getInt("user_id"));
        }

        return check ? song : null;
    }

    @Override
    public List<Song> getSongs() throws SQLException {
        String query = "SELECT * FROM SONG";
        PreparedStatement preparedStatement = connection.prepareStatement(query);

        ResultSet resultSet = preparedStatement.executeQuery();
        List<Song> songList = new ArrayList<>();

        while (resultSet.next()) {
            Song song = new Song(
                    resultSet.getBlob("data_"),
                    resultSet.getString("name_"),
                    resultSet.getInt("user_id")
            );
            song.setId(resultSet.getInt("id"));
            songList.add(song);
        }

        return songList;
    }
}
