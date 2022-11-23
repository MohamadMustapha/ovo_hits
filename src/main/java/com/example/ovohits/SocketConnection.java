package com.example.ovohits;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SocketConnection {
    private static DatagramSocket datagramSocket = null;
    private static DatagramSocket serverDatagramSocket = null;
    private static InetAddress inetAddress = null;

    public static DatagramSocket getDatagramSocket() {
        if (datagramSocket != null) return datagramSocket;
        try { datagramSocket = new DatagramSocket(); }
        catch (Exception e) { e.printStackTrace(); }
        return datagramSocket;
    }

    public static DatagramSocket getServerDatagramSocket(int port) {
        if (serverDatagramSocket != null) return serverDatagramSocket;
        try { serverDatagramSocket = new DatagramSocket(port); }
        catch (Exception e) { e.printStackTrace(); }
        return  serverDatagramSocket;
    }

    public static InetAddress getInetAddress() throws UnknownHostException {
        if (inetAddress != null) return inetAddress;
        return inetAddress = InetAddress.getLocalHost();
    }
}
