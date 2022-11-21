package com.example.ovohits;

import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.models.User;
import com.example.ovohits.database.services.SongService;
import com.example.ovohits.database.services.UserService;
import org.apache.commons.lang3.SerializationUtils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class RequestHandler extends Thread {
    public Request request;
    public SocketAddress socketAddress;

    public RequestHandler(byte[] bufferData, SocketAddress socketAddress) {
        this.request = SerializationUtils.deserialize(bufferData);
        this.socketAddress = socketAddress;
    }

    public void run() {
        switch (request.getFunctionCall()) {
            case "@addSong" -> {
                try { addSong(); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            case "@addUser" -> {
                try { addUser(); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            case "@login" -> {
                try { login(); }
                catch (Exception e) { throw new RuntimeException(e); }
            }
            default -> System.out.println("Invalid function call!");
        }
    }

    public void addSong() throws Exception {
        ArrayList<String> addSongArray = request.getAddSongArray();

        new SongService().add(new Song(
                request.getSongData(),
                addSongArray.get(0),
                Integer.parseInt(addSongArray.get(1))
        ));
    }

    public void addUser() throws Exception {
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

    public void login() throws Exception {
        ArrayList<String> loginArray = request.getLoginArray();
        User user = new UserService().getUser(loginArray.get(0));
        byte[] dataBuffer = user.getPassword().equals(loginArray.get(1)) ? new byte[]{(byte) 1} : new byte[]{(byte) 0};
        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        datagramSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, socketAddress));
    }
}
