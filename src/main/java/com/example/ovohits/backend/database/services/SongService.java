package com.example.ovohits.backend.database.services;

import com.example.ovohits.backend.database.DatabaseConnection;
import com.example.ovohits.backend.database.models.Song;
import com.example.ovohits.backend.database.repositories.SongRepository;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.*;
import java.util.ArrayList;

public class SongService implements SongRepository {
    private static final Connection connection = DatabaseConnection.getConnection();

    private void prepareQuery(PreparedStatement preparedStatement, Song song) throws SQLException {
        preparedStatement.setBlob(1, song.getData());
        preparedStatement.setString(2, song.getName());
        preparedStatement.setInt(3, song.getUser_id());
    }

    private ArrayList<Song> getSongList(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();

        ArrayList<Song> songArrayList = new ArrayList<>();
        while (resultSet.next()) {
            Song song = new Song(
                    new SerialBlob(resultSet.getBlob("data_")),
                    resultSet.getString("name_"),
                    resultSet.getInt("user_id")
            );
            song.setId(resultSet.getInt("id"));
            songArrayList.add(song);
        }

        return songArrayList;
    }

    @Override
    public int add(Song song) throws SQLException {
        String query = "INSERT INTO SONG(" +
                       "data_, name_, user_id) " +
                       "VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        prepareQuery(preparedStatement, song);
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
        prepareQuery(preparedStatement, song);
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

        boolean found = false;
        while (resultSet.next()) {
            found = true;
            song.setId(resultSet.getInt("id"));
            song.setData(new SerialBlob(resultSet.getBlob("data_")));
            song.setName(resultSet.getString("name_"));
            song.setUser_id(resultSet.getInt("user_id"));
        }

        return found ? song : null;
    }

    @Override
    public ArrayList<Song> getSongs() throws SQLException {
        String query = "SELECT * FROM SONG";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        return getSongList(preparedStatement);
    }
}
