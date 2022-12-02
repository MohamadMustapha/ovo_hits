package com.example.ovohits;

import com.example.ovohits.backend.PrintColor;
import com.example.ovohits.backend.ServerRequest;
import com.example.ovohits.backend.database.models.Song;
import com.example.ovohits.backend.database.services.SongService;
import javafx.util.Pair;
import org.apache.commons.lang3.SerializationUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class ServerHandler implements Runnable {
    private boolean running = true;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final int port;
    private final Socket socket;

    public ServerHandler(int port, Socket socket) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Initializing server handler thread..." + PrintColor.RESET);
        this.port = port;
        this.socket = socket;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) { throw new RuntimeException(e); }
        System.out.println(PrintColor.GREEN + "[Success]: Initialized server handler thread on port: "
                + PrintColor.PURPLE + port + PrintColor.RESET);
    }

    public ServerRequest getServerRequest() {
        try {
            byte[] requestData = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(requestData);
            return SerializationUtils.deserialize(requestData);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void sendClientResponse(ClientResponse clientResponse) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(Client.getSocket().getOutputStream());
            byte[] clientResponseData = SerializationUtils.serialize(clientResponse);
            dataOutputStream.writeInt(clientResponseData.length);
            dataOutputStream.write(clientResponseData);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void run() {
        while (running) {
            System.out.println(PrintColor.YELLOW + "[Pending]: Calling function... " + PrintColor.RESET);
            ServerRequest serverRequest = getServerRequest();
            System.out.println(PrintColor.GREEN + "[Success]: Called function: " + PrintColor.BLUE
                    + serverRequest.getFunction() + PrintColor.RESET);
            switch (serverRequest.getFunction()) {
                case "@exit" -> exit();
                case "@getSong" -> getSong(serverRequest);
                case "@getSongs" -> getSongs(serverRequest);
                default -> System.out.println(PrintColor.RED + "[Error]:   Request function invalid!"
                        + PrintColor.RESET);
            }
        }
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) { throw new RuntimeException(e); }
        System.out.println(PrintColor.GREEN + "[Success]: Terminated server handler on port: " + PrintColor.PURPLE
                + port + PrintColor.RESET);
    }

    public void getSong(ServerRequest serverRequest) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending song to server..." + PrintColor.RESET);
        Song song = new SongService().getSong(serverRequest.getModelId());
        ClientResponse clientResponse = new ClientResponse(
                song != null,
                SerializationUtils.serialize(song));
        sendClientResponse(clientResponse);
        System.out.println(PrintColor.GREEN + "[Success]: Sent song to server!" + PrintColor.RESET);
    }

    public void getSongs(ServerRequest serverRequest) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending songs to server..." + PrintColor.RESET);
        sendClientResponse(new ClientResponse(new ArrayList<>(
                serverRequest.getModelId() == -1 ?
                        new SongService().getSongs().stream().map(song ->
                                new Pair<>(song.getName(), song.getId())).toList() :
                        new SongService().getSongs(serverRequest.getModelId()).stream().map(song ->
                                new Pair<>(song.getName(), song.getId())).toList())));
        System.out.println(PrintColor.GREEN + "[Success]: Sent songs to server!" + PrintColor.RESET);
    }

    public void exit() { running = false; }
}
