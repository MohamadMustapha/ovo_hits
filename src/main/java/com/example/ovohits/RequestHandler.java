package com.example.ovohits;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;

public class RequestHandler extends Thread {
    public DatagramSocket datagramSocket;
    public DatagramPacket datagramPacket;

    public RequestHandler(DatagramSocket datagramSocket, DatagramPacket datagramPacket) {
        this.datagramSocket = datagramSocket;
        this.datagramPacket = datagramPacket;
    }

    public void run() {
        ArrayList<byte[]> dataBufferArray = new ArrayList<>();
        while (true) {

        }
    }
}
