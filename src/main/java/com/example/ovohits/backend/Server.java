package com.example.ovohits.backend;

import com.example.ovohits.backend.database.models.Song;
import com.example.ovohits.backend.database.services.SongService;
import javafx.util.Pair;
import org.apache.commons.lang3.SerializationUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Server {
    private static final HashMap<Integer, Pair<InetAddress, Integer>> clients = new HashMap<>();
    private static final Scanner scan = new Scanner(System.in);
    public static Integer port = null;

    public static void addClient(Integer userId, InetAddress inetAddress, Integer port) {
        clients.put(userId, new Pair<>(inetAddress, port));
    }

    public static ArrayList<Integer> getOnlineUsers() { return new ArrayList<>(clients.keySet()); }

    public static ArrayList<Song> getSongs() {
        ArrayList<Song> songList = new ArrayList<>();
        for (Map.Entry<Integer, Pair<InetAddress, Integer>> client : clients.entrySet())
            songList.addAll(new SongService().getSongs(client.getKey()));
        return songList;
    }

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) {
        System.out.print(PrintColor.WHITE + "Please enter a port number:" + PrintColor.RESET + " ");
        port = scan.nextInt();
        System.out.println(PrintColor.GREEN + "[Success]: Server running on port: " + PrintColor.PURPLE + port
                + PrintColor.RESET);

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket server = serverSocket.accept();

                DataInputStream dataInputStream = new DataInputStream(server.getInputStream());
                byte[] requestData = new byte[dataInputStream.readInt()];
                dataInputStream.readFully(requestData);

                DataOutputStream dataOutputStream = new DataOutputStream(server.getOutputStream());
                byte[] responseData = SerializationUtils.serialize(new Response(++port));
                dataOutputStream.writeInt(responseData.length);
                dataOutputStream.write(responseData);

                try (ServerSocket clientSocket = new ServerSocket(port)) {
                    Socket client = clientSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(port, client);
                    new Thread(clientHandler).start();
                } catch (IOException e) { throw new RuntimeException(e); }
            }
        } catch(IOException e) { throw new RuntimeException(e); }
    }
}
