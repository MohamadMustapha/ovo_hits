package com.example.ovohits;

import com.example.ovohits.backend.Response;
import org.apache.commons.lang3.SerializationUtils;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;
import java.sql.SQLException;

public class SocketConnection {
    private static Socket socket = null;

    public static Socket getServerConnection() {
        try {
            if (socket != null) return socket;
            return socket = new Socket("localhost", 6969);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static Response getResponse() {
        try {
            DataInputStream dataInputStream = new DataInputStream(
                    SocketConnection.getServerConnection().getInputStream());
            byte[] responseBuffer = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(responseBuffer);
            return SerializationUtils.deserialize(responseBuffer);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void sendRequest(Request request) {
        try {
            byte[] requestData = SerializationUtils.serialize(request);
            DataOutputStream dataOutputStream = new DataOutputStream(
                    SocketConnection.getServerConnection().getOutputStream());
            dataOutputStream.writeInt(requestData.length);
            dataOutputStream.write(requestData);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public static void terminateThread() {
        try { SocketConnection.sendRequest(new Request("@exit")); }
        catch (SQLException e) { throw new RuntimeException(e); }
    }

//    public static byte[] getByteFragments() throws IOException {
//        byte[] dataBuffer = new byte[64000];
//        DatagramPacket datagramPacket = new DatagramPacket(
//                dataBuffer,
//                dataBuffer.length);
//        SocketConnection.getDatagramSocket().receive(datagramPacket);
//        Integer numberOfPackets = SerializationUtils.deserialize(dataBuffer);
//
//        dataBuffer = SerializationUtils.serialize(new Request());
//        SocketConnection.getDatagramSocket().send(new DatagramPacket(
//                dataBuffer,
//                dataBuffer.length,
//                SocketConnection.getInetAddress(),
//                SocketConnection.getPort()));
//
//        byte[] requestData = null;
//        for (int i = 0; i < numberOfPackets; i++) {
//            dataBuffer = new byte[64000];
//            datagramPacket = new DatagramPacket(
//                    dataBuffer,
//                    dataBuffer.length);
//            SocketConnection.getDatagramSocket().receive(datagramPacket);
//            requestData = requestData != null ? ArrayUtils.addAll(requestData, dataBuffer) : dataBuffer;
//
//            dataBuffer = SerializationUtils.serialize(new Request());
//            SocketConnection.getDatagramSocket().send(new DatagramPacket(
//                    dataBuffer,
//                    dataBuffer.length,
//                    SocketConnection.getInetAddress(),
//                    SocketConnection.getPort()));
//        }
//        return requestData;
//    }
//
//    public static void sendByteFragments(byte[] dataBuffer) throws IOException {
//        List<List<Byte>> dataBuffers = Lists.partition(
//                new ArrayList<>(Bytes.asList(dataBuffer)),
//                64000);
//
//        dataBuffer = SerializationUtils.serialize(dataBuffers.size());
//        SocketConnection.getDatagramSocket().send(new DatagramPacket(
//                dataBuffer,
//                dataBuffer.length,
//                SocketConnection.getInetAddress(),
//                SocketConnection.getPort()));
//
//        DatagramPacket datagramPacket = new DatagramPacket(
//                dataBuffer,
//                dataBuffer.length);
//        SocketConnection.getDatagramSocket().receive(datagramPacket);
//
//        for (List<Byte> rawDataBuffer : dataBuffers) {
//            dataBuffer = ArrayUtils.toPrimitive(rawDataBuffer.toArray(new Byte[0]));
//            SocketConnection.getDatagramSocket().send(new DatagramPacket(
//                    dataBuffer,
//                    dataBuffer.length,
//                    SocketConnection.getInetAddress(),
//                    SocketConnection.getPort()));
//
//            datagramPacket = new DatagramPacket(
//                    dataBuffer,
//                    dataBuffer.length);
//            SocketConnection.getDatagramSocket().receive(datagramPacket);
//        }
//    }
}
