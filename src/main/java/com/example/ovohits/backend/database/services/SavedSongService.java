package com.example.ovohits.backend.database.services;

import com.example.ovohits.backend.database.DatabaseConnection;
import com.example.ovohits.backend.database.models.SavedSong;
import com.example.ovohits.backend.database.repositories.SavedSongRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class SavedSongService implements SavedSongRepository {
    private static final Connection connection = DatabaseConnection.getConnection();

    private void prepareQuery(PreparedStatement preparedStatement, SavedSong savedSong) throws SQLException {
        preparedStatement.setInt(1, savedSong.getSongId());
        preparedStatement.setInt(2, savedSong.getUserId());
    }

    @Override
    public int add(SavedSong savedSong) throws SQLException {
        String query = "INSERT INTO SAVED_SONG(" +
                       "song_id, user_id) " +
                       "VALUES (?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        prepareQuery(preparedStatement, savedSong);
        return preparedStatement.executeUpdate();
    }

    @Override
    public void delete(int songId) throws SQLException {
        String query = "DELETE FROM SAVED_SONG WHERE song_id =?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, songId);
        preparedStatement.executeUpdate();
    }

    @Override
    public void update(SavedSong savedSong) throws SQLException {
        String query = "UPDATE saved_song SET " +
                       "song_id=?, user_id=? " +
                       "WHERE id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        prepareQuery(preparedStatement, savedSong);
        preparedStatement.setInt(3, savedSong.getId());
        preparedStatement.executeUpdate();
    }

    @Override
    public SavedSong getSavedSong(int songId) throws SQLException {
        String query = "SELECT * FROM SAVED_SONG WHERE song_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, songId);
        ResultSet resultSet = preparedStatement.executeQuery();

        SavedSong savedSong = new SavedSong();
        boolean found = false;
        while (resultSet.next()) {
            found = true;
            savedSong.setId(resultSet.getInt("id"));
            savedSong.setSongId(resultSet.getInt("song_id"));
            savedSong.setUserId(resultSet.getInt("user_id"));
        }
        return found ? savedSong : null;
    }

    @Override
    public ArrayList<SavedSong> getSavedSongs(int userId) throws SQLException {
        String query = "SELECT * FROM SAVED_SONG WHERE user_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<SavedSong> savedSongList = new ArrayList<>();
        while (resultSet.next()) {
            SavedSong savedSong = new SavedSong(
                    resultSet.getInt("song_id"),
                    resultSet.getInt("user_id")
            );
            savedSong.setId(resultSet.getInt("id"));
            savedSongList.add(savedSong);
        }
        return savedSongList;
    }
}
