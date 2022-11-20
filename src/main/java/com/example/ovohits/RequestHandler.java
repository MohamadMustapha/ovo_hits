package com.example.ovohits;

import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.models.User;
import com.example.ovohits.database.services.SongService;
import com.example.ovohits.database.services.UserService;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class RequestHandler extends Thread {
    public DatagramSocket datagramSocket;

    public RequestHandler(DatagramSocket datagramSocket) {
        this.datagramSocket = datagramSocket;
    }

    private static String byteToString(byte[] bytes) {
        if (bytes == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; bytes[i] != 0; i++)
            stringBuilder.append((char) bytes[i]);
        return stringBuilder.toString();
    }

    public void run() {
        ArrayList<byte[]> dataBufferArray = new ArrayList<>();
        while (true) {
            byte[] dataBuffer = new byte[16777215];
            DatagramPacket datagramPacket = new DatagramPacket(dataBuffer, dataBuffer.length);
            try {
                datagramSocket.receive(datagramPacket);
                switch (byteToString(dataBuffer)) {
                    case "@addUser":
                        addUser(dataBufferArray);
                        dataBufferArray.clear();
                        break;
                    case "@login":
                        login(dataBufferArray, datagramPacket.getSocketAddress());
                        dataBufferArray.clear();
                        break;
                    default:
                        dataBufferArray.add(dataBuffer);
                        System.out.println("Client sent: " + new String(dataBuffer));
                }
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    public void addSong(ArrayList<byte[]> dataBufferArray) throws Exception {
        new SongService().add(new Song(
                new SerialBlob(dataBufferArray.get(0)),
                new String(dataBufferArray.get(1)),
                ByteBuffer.wrap(dataBufferArray.get(2)).getInt()
        ));
    }

    public void addUser(ArrayList<byte[]> dataBufferArray) throws Exception {
        int user_id = new UserService().add(new User(
                new String(dataBufferArray.get(0)),
                new String(dataBufferArray.get(1)),
                new String(dataBufferArray.get(2)),
                new String(dataBufferArray.get(3)),
                new String(dataBufferArray.get(4))
        ));
        ArrayList<byte[]> songDataBufferArray = (ArrayList<byte[]>) Arrays.asList(dataBufferArray.get(5),
                dataBufferArray.get(6), Integer.toString(user_id).getBytes());
        addSong(songDataBufferArray);
    }

    public void login(ArrayList<byte[]> dataBufferArray, SocketAddress socketAddress) throws Exception {
        User user = new UserService().getUser(new String(dataBufferArray.get(0)));

        DatagramSocket clientSocket = SocketConnection.getDatagramSocket();
        byte[] dataBuffer = Objects.equals(user.getPassword(), byteToString(dataBufferArray.get(1))) ?
                new byte[]{(byte) 1} : new byte[]{(byte) 0};
        clientSocket.send(new DatagramPacket(dataBuffer, dataBuffer.length, socketAddress));
    }
}
