package com.example.ovohits.backend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Server {
    private static final ArrayList<Socket> clients = new ArrayList<>();
    private static final Scanner scan = new Scanner(System.in);
    public static Integer port = null;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        System.out.print(PrintColor.WHITE + "Please enter a port number:" + PrintColor.RESET + " ");
        port = scan.nextInt();
        System.out.println(PrintColor.GREEN + "[Success]: Server running on port: " + PrintColor.PURPLE + port
                + PrintColor.RESET);
        while (true) {
            try (ServerSocket serverSocket = new ServerSocket(port)) {
                Socket socket = serverSocket.accept();
                clients.add(socket);
                ClientHandler clientHandler = new ClientHandler(socket);
                new Thread(clientHandler).start();
            } catch (IOException e) { throw new RuntimeException(e); }
        }
    }
}
