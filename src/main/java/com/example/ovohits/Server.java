package com.example.ovohits;

import com.example.ovohits.database.models.Song;
import com.example.ovohits.database.models.User;
import com.example.ovohits.database.services.SongService;
import com.example.ovohits.database.services.UserService;

import javax.sql.rowset.serial.SerialBlob;
import java.io.FileInputStream;
import java.net.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Server {
    private static String byteToString(byte[] bytes){
        if (bytes == null) return null;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; bytes[i] != 0; i++)
            stringBuilder.append((char) bytes[i]);
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws Exception {
        // Establish a connection with a Port: 6969
        DatagramSocket datagramSocket = new DatagramSocket(6969);

        // Manage data between client and server
        ArrayList<byte[]> dataBufferArray = new ArrayList<>();
        while (true){
            byte[] receivedDataBuffer = new byte[16777215];
            DatagramPacket receiver = new DatagramPacket(receivedDataBuffer, receivedDataBuffer.length);
            datagramSocket.receive(receiver);

            switch (byteToString(receivedDataBuffer)) {
                case "@addUser": addUser(dataBufferArray); break;
                default:
                    dataBufferArray.add(receivedDataBuffer);
                    System.out.println("Client sent: " + byteToString(receivedDataBuffer));
            }
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
    public static User registration(ArrayList<byte[]> user){
        User x = new User();
        for(int i=0;i<5;i++){
            String value = byteToString(user.get(i));
            switch (i) {
                case 0 -> x.setEmail(value);
                case 1 -> x.setFirstName(value);
                case 2 -> x.setLastName(value);
                case 3 -> x.setPassword(value);
                case 4 -> x.setUsername(value);
            }
        }
        return x;
    }
    public static boolean login(byte[] username, byte[] password) throws SQLException {
        UserService userService = new UserService();
        User user = userService.getUser(byteToString(username));
        return Objects.equals(user.getPassword(), byteToString(password));

    }
//    public void login() throws Exception {
//        String username = usernameField.getText();
//        String password = passwordField.getText();
//
//        UserService userService = new UserService();
//
//        if(username.equals("") || password.equals("")){
//            System.out.println("missing field");
//        }
//        else{
//            User user = userService.getUser(username);
//            if(Objects.equals(user.getPassword(),password)){
//                System.out.println("login successful");
//            }
//            else {
//                System.out.println("incorrect credentials");
//            }
//        }
}
