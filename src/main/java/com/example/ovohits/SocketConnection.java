package com.example.ovohits;

import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class SocketConnection {
    private static DatagramSocket datagramSocket = null;
    private static InetAddress inetAddress = null;
    private static Integer port = 6969;

    public static byte[] getByteFragments() throws IOException {
        byte[] dataBuffer = new byte[64000];
        DatagramPacket datagramPacket = new DatagramPacket(
                dataBuffer,
                dataBuffer.length);
        SocketConnection.getDatagramSocket().receive(datagramPacket);
        Integer numberOfPackets = SerializationUtils.deserialize(dataBuffer);

        dataBuffer = SerializationUtils.serialize(new Request());
        SocketConnection.getDatagramSocket().send(new DatagramPacket(
                dataBuffer,
                dataBuffer.length,
                SocketConnection.getInetAddress(),
                SocketConnection.getPort()));

        byte[] requestData = null;
        for (int i = 0; i < numberOfPackets; i++) {
            dataBuffer = new byte[64000];
            datagramPacket = new DatagramPacket(
                    dataBuffer,
                    dataBuffer.length);
            SocketConnection.getDatagramSocket().receive(datagramPacket);
            requestData = requestData != null ? ArrayUtils.addAll(requestData, dataBuffer) : dataBuffer;

            dataBuffer = SerializationUtils.serialize(new Request());
            SocketConnection.getDatagramSocket().send(new DatagramPacket(
                    dataBuffer,
                    dataBuffer.length,
                    SocketConnection.getInetAddress(),
                    SocketConnection.getPort()));
        }
        return requestData;
    }

    public static void sendByteFragments(byte[] dataBuffer) throws IOException {
        List<List<Byte>> dataBuffers = Lists.partition(
                new ArrayList<>(Bytes.asList(dataBuffer)),
                64000);

        dataBuffer = SerializationUtils.serialize(dataBuffers.size());
        SocketConnection.getDatagramSocket().send(new DatagramPacket(
                dataBuffer,
                dataBuffer.length,
                SocketConnection.getInetAddress(),
                SocketConnection.getPort()));

        DatagramPacket datagramPacket = new DatagramPacket(
                dataBuffer,
                dataBuffer.length);
        SocketConnection.getDatagramSocket().receive(datagramPacket);

        for (List<Byte> rawDataBuffer : dataBuffers) {
            dataBuffer = ArrayUtils.toPrimitive(rawDataBuffer.toArray(new Byte[0]));
            SocketConnection.getDatagramSocket().send(new DatagramPacket(
                    dataBuffer,
                    dataBuffer.length,
                    SocketConnection.getInetAddress(),
                    SocketConnection.getPort()));

            datagramPacket = new DatagramPacket(
                    dataBuffer,
                    dataBuffer.length);
            SocketConnection.getDatagramSocket().receive(datagramPacket);
        }
    }

    public static DatagramSocket getDatagramSocket() {
        if (datagramSocket != null) return datagramSocket;
        try { datagramSocket = new DatagramSocket(); }
        catch (SocketException e) { e.printStackTrace(); }
        return datagramSocket;
    }

    public static InetAddress getInetAddress() {
        if (inetAddress != null) return inetAddress;
        try { inetAddress = InetAddress.getLocalHost(); }
        catch (UnknownHostException e) { e.printStackTrace(); }
        return inetAddress;
    }

    public static Integer getPort() { return port; }

    public static void setPort(Integer port) { SocketConnection.port = port; }
}
