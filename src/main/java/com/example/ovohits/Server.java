package com.example.ovohits;

import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.models.User;
import com.example.ovohits.database.services.SongService;
import com.example.ovohits.database.services.UserService;

import javax.sql.rowset.serial.SerialBlob;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Server {
    private static final Scanner scan = new Scanner(System.in);

    private static String byteToString(byte[] bytes) {
        if (bytes == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; bytes[i] != 0; i++)
            stringBuilder.append((char) bytes[i]);
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws Exception {
        int port = scan.nextInt();
        DatagramSocket datagramSocket = SocketConnection.getServerDatagramSocket(port);
        System.out.println("Server running at Port: " + port);

        while (true) {
            byte[] bufferData = new byte[16777215];
            DatagramPacket receiver = new DatagramPacket(bufferData, bufferData.length);
            datagramSocket.receive(receiver);
            Thread requestHandler = new RequestHandler(bufferData, receiver.getSocketAddress());
            requestHandler.start();
        }
    }

    public static void addUser(ArrayList<byte[]> dataBufferArray) throws Exception {
        int user_id = new UserService().add(new User(
                byteToString(dataBufferArray.get(0)),
                byteToString(dataBufferArray.get(1)),
                byteToString(dataBufferArray.get(2)),
                byteToString(dataBufferArray.get(3)),
                byteToString(dataBufferArray.get(4))
        ));
        ArrayList<byte[]> songDataBufferArray = (ArrayList<byte[]>) Arrays.asList(dataBufferArray.get(5),
                dataBufferArray.get(6), Integer.toString(user_id).getBytes());
        addSong(songDataBufferArray);
    }

    public static void addSong(ArrayList<byte[]> dataBufferArray) throws Exception {
        new SongService().add(new Song(
                new SerialBlob(dataBufferArray.get(0)),
                new String(dataBufferArray.get(1)),
                ByteBuffer.wrap(dataBufferArray.get(2)).getInt()
        ));
    }

    //    public static boolean isEmail(String email)
//    {
//        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
//                "[a-zA-Z0-9_+&*-]+)*@" +
//                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
//                "A-Z]{2,7}$";
//
//        Pattern pat = Pattern.compile(emailRegex);
//        if (email == null)
//            return false;
//        return pat.matcher(email).matches();
//    }

    public static void login(ArrayList<byte[]> dataBufferArray, SocketAddress socketAddress) throws Exception {
        User user = new UserService().getUser(byteToString(dataBufferArray.get(0)));

        DatagramSocket datagramSocket = SocketConnection.getDatagramSocket();
        byte[] sendingBufferArray;
        // 0 for false 1 for true
        if(!Objects.equals(user.getPassword(), byteToString(dataBufferArray.get(1)))){
            sendingBufferArray = new byte[]{(byte) 0};
           datagramSocket.send(new DatagramPacket(sendingBufferArray,sendingBufferArray.length, socketAddress));
       }
       else {
            sendingBufferArray = new byte[]{(byte) 1};
            datagramSocket.send(new DatagramPacket(sendingBufferArray,sendingBufferArray.length, socketAddress));
       }
}
}
