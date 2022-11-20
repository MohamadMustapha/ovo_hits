package com.example.ovohits;

import java.net.DatagramSocket;

public class SocketConnection {
    public static DatagramSocket datagramSocket;

    public static DatagramSocket getDatagramSocket(){
        try {
            datagramSocket = new DatagramSocket();
        }
        catch (Exception e){ e.printStackTrace(); }
        return datagramSocket;

}
