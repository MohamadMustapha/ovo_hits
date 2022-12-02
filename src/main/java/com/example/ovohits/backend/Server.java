package com.example.ovohits.backend;

import org.apache.commons.lang3.SerializationUtils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Server {
    private static final HashMap<Integer, Socket> serverHandlers = new HashMap<>();
    private static final Scanner scan = new Scanner(System.in);

    public static ArrayList<Integer> getOnlineUsers() { return new ArrayList<>(serverHandlers.keySet()); }

    public static HashMap<Integer, Socket> getServerHandlers() { return serverHandlers; }

    public static void addServerHandler(Integer userId, Socket socket) { serverHandlers.put(userId, socket); }

    public static void deleteServerHandler(Integer userId) { serverHandlers.remove(userId); }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        System.out.print(PrintColor.WHITE + "Please enter a port number:" + PrintColor.RESET + " ");
        int port = scan.nextInt();
        System.out.println(PrintColor.GREEN + "[Success]: Server running on port: " + PrintColor.PURPLE + port
                + PrintColor.RESET);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
//            serverHandlers.put(-1, null);
            while (true) {
                Socket server = serverSocket.accept();
                DataOutputStream dataOutputStream = new DataOutputStream(server.getOutputStream());

                byte[] responseData = SerializationUtils.serialize(new Response(++port));
                dataOutputStream.writeInt(responseData.length);
                dataOutputStream.write(responseData);

                try (ServerSocket clientSocket = new ServerSocket(port)) {
                    ClientHandler clientHandler = new ClientHandler(port, clientSocket.accept());
                    new Thread(clientHandler).start();
                } catch (IOException e) { throw new RuntimeException(e); }
            }
        } catch(IOException e) { throw new RuntimeException(e); }
    }
}
