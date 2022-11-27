package com.example.ovohits.backend;

import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerSocketConnection {
    private static DatagramSocket datagramSocket = null;

    public static DatagramSocket getDatagramSocket(int port) {
        if (datagramSocket != null) return datagramSocket;
        try { datagramSocket = new DatagramSocket(port); }
        catch (SocketException e) { e.printStackTrace(); }
        return datagramSocket;
    }
}
