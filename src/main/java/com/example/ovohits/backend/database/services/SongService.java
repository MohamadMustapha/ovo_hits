package com.example.ovohits.backend.database.services;

import com.example.ovohits.backend.database.DatabaseConnection;
import com.example.ovohits.backend.database.models.Song;
import com.example.ovohits.backend.database.repositories.SongRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.ArrayList;

public class SongService implements SongRepository {
    private static final Connection connection = DatabaseConnection.getConnection();

    private void prepareQuery(PreparedStatement preparedStatement, Song song) {
        try {
            preparedStatement.setBlob(1, song.getData());
            preparedStatement.setString(2, song.getName());
            preparedStatement.setInt(3, song.getUserId());
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    private ArrayList<Song> getSongList(PreparedStatement preparedStatement) {
        try {
            ResultSet resultSet = preparedStatement.executeQuery();
            ArrayList<Song> songList = new ArrayList<>();
            while (resultSet.next()) {
                Song song = new Song(
                        new SerialBlob(resultSet.getBlob("data_")),
                        resultSet.getString("name_"),
                        resultSet.getInt("user_id")
                );
                song.setId(resultSet.getInt("id"));
                songList.add(song);
            }
            return songList;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public int add(Song song) {
        String query = "INSERT INTO SONG(" +
                       "data_, name_, user_id) " +
                       "VALUES (?, ?, ?)";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            prepareQuery(preparedStatement, song);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void delete(int id) {
        String query = "DELETE FROM SONG WHERE id =?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public void update(Song song) {
        String query = "UPDATE SONG SET " +
                       "data_=?, name_=?, user_id=? " +
                       "WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            prepareQuery(preparedStatement, song);
            preparedStatement.setInt(4, song.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public Song getSong(int id) {
        String query = "SELECT * FROM SONG WHERE id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            Song song = new Song();
            boolean found = false;
            while (resultSet.next()) {
                found = true;
                song.setId(resultSet.getInt("id"));
                song.setData(new SerialBlob(resultSet.getBlob("data_")));
                song.setName(resultSet.getString("name_"));
                song.setUserId(resultSet.getInt("user_id"));
            }
            return found ? song : null;
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    @Override
    public ArrayList<Song> getSongs() {
        String query = "SELECT * FROM SONG";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            return getSongList(preparedStatement);
        } catch (SQLException e) { throw new RuntimeException(e); }
}

    @Override
    public ArrayList<Song> getSongs(int userId) {
        String query = "SELECT * FROM SONG WHERE user_id=?";
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, userId);
            return getSongList(preparedStatement);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }
}
