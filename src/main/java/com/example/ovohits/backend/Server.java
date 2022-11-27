package com.example.ovohits.backend;

import java.net.*;
import java.util.Scanner;

public class Server {
    private static final Scanner scan = new Scanner(System.in);
    public static Integer port = null;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws Exception {
        System.out.print("\033[1;37m" + "Please enter a port number:" + "\033[0m ");
        port = scan.nextInt();
        DatagramSocket datagramSocket = ServerSocketConnection.getDatagramSocket(port);
        System.out.println("\033[1;32m" + "[Success]: Server running on port: " + "\033[1;35m" + port + "\033[0m");

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
