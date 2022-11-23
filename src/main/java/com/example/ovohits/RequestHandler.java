package com.example.ovohits;

import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.models.User;
import com.example.ovohits.database.services.SongService;
import com.example.ovohits.database.services.UserService;
import org.apache.commons.lang3.SerializationUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;

public class RequestHandler extends Thread {
    private final DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
    private final Request request;
    private final SocketAddress socketAddress;

    public RequestHandler(byte[] bufferData, SocketAddress socketAddress) {
        this.request = SerializationUtils.deserialize(bufferData);
        this.socketAddress = socketAddress;
    }

    public void run() {
        System.out.println("Function call: " + request.getFunctionCall());
        switch (request.getFunctionCall()) {
            case "@addSong" -> {
                try { addSong(); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            case "@addUser" -> {
                try { addUser(); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            case "@getSong" -> {
                try { getSong(); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            case "@getSongs" -> {
                try { getSongs(); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            case "@getUser" -> {
                try { getUser(); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            case "@login" -> {
                try { login(); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            case "@playSong" -> {
                try { playSong(); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            default -> System.out.println("Invalid function call!");
        }
    }

    public void addSong() throws SQLException {
        ArrayList<String> addSongArray = request.getAddSongArray();
        new SongService().add(new Song(
                new SerialBlob(request.getSongData()),
                addSongArray.get(0),
                Integer.parseInt(addSongArray.get(1))
        ));
    }

    public void addUser() throws SQLException {
        ArrayList<String> addUserArray = request.getAddUserArray();
        int user_id = new UserService().add(new User(
           addUserArray.get(0),
           addUserArray.get(1),
           addUserArray.get(2),
           addUserArray.get(3),
           addUserArray.get(4)
        ));
        ArrayList<String> addSongArray = request.getAddSongArray();
        addSongArray.set(1, Integer.toString(user_id));
        request.setAddSongArray(addSongArray);
        addSong();
    }

    public void getSong() throws IOException, SQLException {
        Song song = new SongService().getSong(request.getModelId());
        Response response = new Response(song != null);
        response.setSongData(SerializationUtils.serialize(song));
        byte[] dataBuffer = SerializationUtils.serialize(response);
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, socketAddress));
    }

    public void getSongs() throws IOException, SQLException {
        ArrayList<byte[]> songDataList = new ArrayList<>(new SongService().getSongsById(request.getModelId())
                .stream().map(SerializationUtils::serialize).toList());
        Response response = new Response(songDataList);
        byte[] dataBuffer = SerializationUtils.serialize(response);
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, socketAddress));
    }

    public void getUser() throws IOException, SQLException {
        User user = new UserService().getUser(request.getModelId());
        Response response = new Response(user != null);
        response.setUserData(SerializationUtils.serialize(user));
        byte[] dataBuffer = SerializationUtils.serialize(response);
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, socketAddress));
    }

    public void login() throws IOException, SQLException {
        ArrayList<String> loginArray = request.getLoginArray();
        User user = new UserService().getUser(loginArray.get(0));
        Response response = new Response(user != null && user.getPassword().equals(loginArray.get(1)));
        response.setUserId(user != null ? user.getId() : -1);
        byte[] dataBuffer = SerializationUtils.serialize(response);
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, socketAddress));
    }

    public void playSong() {
        // TODO: Download and play audio packets sent from frontend
    }
}
