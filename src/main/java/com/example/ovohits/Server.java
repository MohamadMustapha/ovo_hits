package com.example.ovohits;

import com.example.ovohits.database.models.User;
import com.example.ovohits.database.services.UserService;

import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Objects;

public class Server {
    public static void main(String[] args) throws Exception {
        //connection
        int port = 6969;
        DatagramSocket datagramSocket = new DatagramSocket(port);
        byte[] receivedDataBuffer = new byte[65535];

        // data handling structures
        DatagramPacket receiver;
        ArrayList<byte[]> bufferArray = new ArrayList<>();
        while(true){
            //receive data
            User user;
            receiver = new DatagramPacket(receivedDataBuffer,receivedDataBuffer.length);
            datagramSocket.receive(receiver);


            // registration
            if(!byteToString(receivedDataBuffer).equals("submit")) {
                bufferArray.add(receivedDataBuffer);
                System.out.println("client sent: " + byteToString(receivedDataBuffer));
            }
            // close the server
            else{
                // create the user
                user = registration(bufferArray);
                // add teh user to the db
                UserService userService = new UserService();
                userService.add(user);
                System.out.println(user);
                System.out.println("Client Exited");
                break;
            }
            receivedDataBuffer = new byte[65535];
        }



    }
    public static String byteToString(byte[] e){
        if (e==null)return null;
        StringBuilder s = new StringBuilder();
        int i =0;
        while(e[i]!=0){
            s.append((char)e[i]);
            i++;
        }
        return s.toString();
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
