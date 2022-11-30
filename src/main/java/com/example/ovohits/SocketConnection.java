package com.example.ovohits;

import com.example.ovohits.backend.Response;
import org.apache.commons.lang3.SerializationUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;

public class SocketConnection {
    public static int port = 6969;
    private static Socket socket = null;

    public static void setPort(int port) {
        SocketConnection.port = port;
    }

    public static Socket getSocket() {
        return socket;
    }

    public static void setSocket() {
        try {
            SocketConnection.socket = new Socket("localhost", port);
//            SocketConnection.socket = new Socket("10.169.35.51", port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Response getResponse() {
        try {
            DataInputStream dataInputStream = new DataInputStream(getSocket().getInputStream());
            byte[] responseBuffer = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(responseBuffer);
            return SerializationUtils.deserialize(responseBuffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendRequest(Request request) {
        try {
            byte[] requestData = SerializationUtils.serialize(request);
            DataOutputStream dataOutputStream = new DataOutputStream(getSocket().getOutputStream());
            dataOutputStream.writeInt(requestData.length);
            dataOutputStream.write(requestData);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void terminateThread() {
        if (!Client.isThreadAlive()) return;
        try {
            sendRequest(new Request("@exit"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}