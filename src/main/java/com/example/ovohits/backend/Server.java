package com.example.ovohits.backend;

import com.example.ovohits.SocketConnection;

import java.net.*;
import java.util.Scanner;

public class Server {
    private static final Scanner scan = new Scanner(System.in);
    public static Integer port = null;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws Exception {
        port = scan.nextInt();
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
}
