package com.example.ovohits;

import com.example.ovohits.backend.PrintColor;
import com.example.ovohits.backend.Response;
import com.example.ovohits.backend.ServerRequest;
import com.example.ovohits.backend.database.models.Song;
import com.example.ovohits.backend.database.services.SongService;
import javafx.application.Application;
import javafx.util.Pair;
import org.apache.commons.lang3.SerializationUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class Client {
    private static boolean running = false;
    private static Integer clientId = null;
    private static Integer port = 6969;
    private static Socket socket = null;

    public static boolean isRunning() { return running; }

    public static void setRunning(boolean running) { Client.running = running; }

    public static Integer getClientId() { return clientId; }

    public static void setClientId(Integer clientId) { Client.clientId = clientId; }

    public static void setPort(int port) { Client.port = port; }

    public static void setSocket() {
        try { Client.socket = new Socket("10.169.29.218", port); }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void callRequestFunction(ServerRequest serverRequest) {
        System.out.println(PrintColor.GREEN + "[Success]: Called function: " + "\033[1;34m"
                + serverRequest.getFunction() + PrintColor.RESET);
        switch (serverRequest.getFunction()) {
            case "@getSong" -> getSong(serverRequest);
            case "@getSongs" -> getSongs();
            default -> System.out.println(PrintColor.RED + "[Error]:   Request function invalid!"
                    + PrintColor.RESET);
        }
    }

    public static void getSong(ServerRequest serverRequest) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending song to server..." + PrintColor.RESET);
        Song song = new SongService().getSong(serverRequest.getModelId());
        ClientResponse clientResponse = new ClientResponse(
                song != null,
                SerializationUtils.serialize(song));
        sendClientResponse(clientResponse);
        System.out.println(PrintColor.GREEN + "[Success]: Sent song to server!" + PrintColor.RESET);
    }

    public static void getSongs() {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending songs to server..." + PrintColor.RESET);
        sendClientResponse(new ClientResponse(new ArrayList<>(new SongService().getSongs().stream().map(song ->
                new Pair<>(song.getName(), song.getId())).toList())));
        System.out.println(PrintColor.GREEN + "[Success]: Sent songs to server!" + PrintColor.RESET);
    }

    public static ServerRequest getServerRequest() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            byte[] requestData = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(requestData);
            return SerializationUtils.deserialize(requestData);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void sendClientResponse(ClientResponse clientResponse) {
        System.out.println("TESTING1");
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            byte[] clientResponseData = SerializationUtils.serialize(clientResponse);
            dataOutputStream.writeInt(clientResponseData.length);
            dataOutputStream.write(clientResponseData);
        } catch (IOException e) { throw new RuntimeException(e); }
        System.out.println("TESTING2");
    }

    public static Response getResponse() {
        try {
            DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
            byte[] responseBuffer = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(responseBuffer);
            return SerializationUtils.deserialize(responseBuffer);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void sendRequest(Request request) {
        try {
            DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
            byte[] requestData = SerializationUtils.serialize(request);
            dataOutputStream.writeInt(requestData.length);
            dataOutputStream.write(requestData);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void terminateThread() {
        if (!isRunning()) return;
        if (getClientId() == null)
            try { sendRequest(new Request("@exit")); }
            catch (SQLException e) { throw new RuntimeException(e); }
        else
            try { sendRequest(new Request(getClientId(), "@exit")); }
            catch (SQLException e) { throw new RuntimeException(e); }
    }

    public static void main(String[] args) {
        setSocket();
        setPort(getResponse().getPort());
        setSocket();
        setRunning(true);
        Application.launch(Landing.class, args);
    }
}
