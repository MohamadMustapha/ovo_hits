package com.example.ovohits.backend;

import java.net.*;
import java.util.Scanner;

public class Server {
    private static final Scanner scan = new Scanner(System.in);
    public static Integer port = null;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws Exception {
        System.out.print(PrintColor.WHITE + "Please enter a port number:" + PrintColor.RESET + " ");
        port = scan.nextInt();
        DatagramSocket datagramSocket = ServerSocketConnection.getDatagramSocket(port);
        System.out.println(PrintColor.GREEN + "[Success]: Server running on port: " + PrintColor.PURPLE + port
                + PrintColor.RESET);

        while (true) {
            byte[] dataBuffer = new byte[64000];
            DatagramPacket receiver = new DatagramPacket(
                    dataBuffer,
                    dataBuffer.length);
            datagramSocket.receive(receiver);

            Thread requestHandler = new ClientHandler(
                    ++port,
                    receiver.getSocketAddress());
            requestHandler.start();
        }
    }
}
