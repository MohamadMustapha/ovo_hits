package com.example.ovohits;

import java.net.DatagramSocket;

public class SocketConnection {
    private static DatagramSocket datagramSocket;
    private static DatagramSocket serverDatagramSocket;

    public static DatagramSocket getDatagramSocket() {
        try { datagramSocket = new DatagramSocket(); }
        catch (Exception e) { e.printStackTrace(); }
        return datagramSocket;
    }

    public static DatagramSocket getServerDatagramSocket(int port) {
        try { serverDatagramSocket = new DatagramSocket(port); }
        catch (Exception e) { e.printStackTrace(); }
        return  serverDatagramSocket;
    }
}
