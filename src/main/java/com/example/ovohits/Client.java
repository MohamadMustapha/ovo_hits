package com.example.ovohits;

import com.example.ovohits.backend.Response;
import javafx.application.Application;
import org.apache.commons.lang3.SerializationUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Client {
    private static boolean running = false;
    private static DataInputStream dataInputStream = null;
    private static DataOutputStream dataOutputStream = null;
    private static Integer clientId = null;
    private static Integer port = 6969;
    private static ServerSocket serverSocket = null;
    private static Socket socket = null;

    public static boolean isRunning() { return running; }

    public static void setRunning(boolean running) { Client.running = running; }

    public static Integer getClientId() { return clientId; }

    public static void setClientId(Integer clientId) { Client.clientId = clientId; }

    public static void setPort(int port) { Client.port = port; }

    public static void setSocket() {
        try {
            Client.socket = new Socket("localhost", port);
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
        }
        catch (IOException e) { throw new RuntimeException(e); }
    }

    public static Socket getSocket() { return socket; }

    public static Response getResponse() {
        try {
            byte[] responseBuffer = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(responseBuffer);
            return SerializationUtils.deserialize(responseBuffer);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void sendRequest(Request request) {
        try {
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

    public static void initializeServerHandler() {
        try {
            serverSocket = new ServerSocket(6969 * 2 - port);
            try {
                byte[] requestData = SerializationUtils.serialize(new Request(
                        clientId,
                        "@addServerHandler"));
                dataOutputStream.writeInt(requestData.length);
                dataOutputStream.write(requestData);
            } catch (SQLException e) { throw new RuntimeException(e); }

            ServerHandler serverHandler = new ServerHandler(6969 * 2 - port, serverSocket.accept());
            new Thread(serverHandler).start();
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void main(String[] args) {
        setSocket();
        setPort(getResponse().getPort());
        setSocket();
        setRunning(true);
        Application.launch(Landing.class, args);
    }
}
