package com.example.ovohits.backend;

import com.example.ovohits.Request;
import com.example.ovohits.backend.database.models.SavedSong;
import com.example.ovohits.backend.database.models.Song;
import com.example.ovohits.backend.database.models.User;
import com.example.ovohits.backend.database.services.SavedSongService;
import com.example.ovohits.backend.database.services.SongService;
import com.example.ovohits.backend.database.services.UserService;
import com.google.common.collect.Lists;
import com.google.common.primitives.Bytes;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientHandler extends Thread {
    private final AtomicBoolean running = new AtomicBoolean(true);
    private final DatagramSocket datagramSocket;
    private final int port;
    private final SocketAddress socketAddress;

    public ClientHandler(int port, SocketAddress socketAddress) throws IOException {
        System.out.println(PrintColor.YELLOW + "[Pending]: Initializing client thread..." + PrintColor.RESET);
        this.port = port;
        this.socketAddress = socketAddress;
        this.datagramSocket = new DatagramSocket(port);
        createClientThread();
    }

    private void createClientThread() throws IOException {
        byte[] dataBuffer = SerializationUtils.serialize(new Response(port));
        datagramSocket.send(new DatagramPacket(
                dataBuffer,
                dataBuffer.length,
                socketAddress));

        datagramSocket.receive(new DatagramPacket(
                dataBuffer,
                dataBuffer.length));
        System.out.println(PrintColor.GREEN + "[Success]: Created client thread on port: " + "\033[1;35m" + port
                + PrintColor.RESET);
    }

    private void invalidFunctionCall() throws IOException {
        Response response = new Response();
        response.setFunctionCalled(false);
        byte[] dataBuffer = SerializationUtils.serialize(response);
        datagramSocket.send(new DatagramPacket(
                dataBuffer,
                dataBuffer.length,
                socketAddress));
        System.out.println(PrintColor.RED + "[Error]:   Request function invalid!" + PrintColor.RESET);
    }

    private byte[] getByteFragments() throws IOException {
        byte[] dataBuffer = new byte[64000];
        DatagramPacket datagramPacket = new DatagramPacket(
                dataBuffer,
                dataBuffer.length);
        datagramSocket.receive(datagramPacket);
        Integer numberOfPackets = SerializationUtils.deserialize(dataBuffer);

        dataBuffer = SerializationUtils.serialize(new Response());
        datagramSocket.send(new DatagramPacket(
                dataBuffer,
                dataBuffer.length,
                socketAddress));

        byte[] requestData = null;
        for (int i = 0; i < numberOfPackets; i++) {
            dataBuffer = new byte[64000];
            datagramPacket = new DatagramPacket(
                    dataBuffer,
                    dataBuffer.length);
            datagramSocket.receive(datagramPacket);
            requestData = requestData != null ? ArrayUtils.addAll(requestData, dataBuffer) : dataBuffer;

            dataBuffer = SerializationUtils.serialize(new Response());
            datagramSocket.send(new DatagramPacket(
                    dataBuffer,
                    dataBuffer.length,
                    socketAddress));
        }
        return requestData;
    }

    private void sendByteFragments(byte[] dataBuffer) throws IOException {
        List<List<Byte>> dataBuffers = Lists.partition(
                new ArrayList<>(Bytes.asList(dataBuffer)),
                64000);

        dataBuffer = SerializationUtils.serialize(dataBuffers.size());
        datagramSocket.send(new DatagramPacket(
                dataBuffer,
                dataBuffer.length,
                socketAddress));

        datagramSocket.receive(new DatagramPacket(
                dataBuffer,
                dataBuffer.length));

        for (List<Byte> rawDataBuffer : dataBuffers) {
            dataBuffer = ArrayUtils.toPrimitive(rawDataBuffer.toArray(new Byte[0]));
            datagramSocket.send(new DatagramPacket(
                    dataBuffer,
                    dataBuffer.length,
                    socketAddress));

            datagramSocket.receive(new DatagramPacket(
                    dataBuffer,
                    dataBuffer.length));
        }
    }

    public void run() {
        while (running.get()) {
            Request request;
            try { request = SerializationUtils.deserialize(getByteFragments()); }
            catch (IOException e) { throw new RuntimeException(e); }
            System.out.println(PrintColor.GREEN + "[Success]: Called function: " + "\033[1;34m" + request.getFunction()
                    + PrintColor.RESET);
            switch (request.getFunction()) {
                case "@addSavedSong" -> {
                    try { addSavedSong(request); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
                case "@addSong" -> {
                    try { addSong(request, new Response()); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
                case "@addUser" -> {
                    try { addUser(request); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
                case "@deleteSavedSong" -> {
                    try { deleteSavedSong(request); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
                case "@exit" -> running.set(false);
                case "@getSong" -> {
                    try { getSong(request); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
                case "@getSavedSongs" -> {
                    try { getSavedSongs(request); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
                case "@getSongs" -> {
                    try { getSongs(); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
                case "@getUser" -> {
                    try { getUser(request); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
                case "@login" -> {
                    try { login(request); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
                case "@playSong" -> playSong();
                default -> {
                    try { invalidFunctionCall(); }
                    catch (IOException e) { throw new RuntimeException(e); }
                }
            }
        }
        System.out.println(PrintColor.GREEN + "[Success]: Terminated thread on port: " + "\033[1;35m"
                + port + PrintColor.RESET);
    }

    public void addSavedSong(Request request) throws IOException, SQLException {
        System.out.println(PrintColor.YELLOW + "[Pending]: Adding saved song to database..." + PrintColor.RESET);
        ArrayList<Integer> savedSongInfo = request.getSavedSongInfo();
        ArrayList<Integer> songIdList = new ArrayList<>(new SavedSongService()
                .getSavedSongs(savedSongInfo.get(1)).stream().map(SavedSong::getSongId).toList());
        if (songIdList.contains(savedSongInfo.get(0))) {
            System.out.println(PrintColor.RED + "[Error]:   Saved song already added!" + PrintColor.RESET);
            Response response = new Response();
            response.setFunctionCalled(false);
            sendByteFragments(SerializationUtils.serialize(response));
            return;
        }
        new SavedSongService().add(new SavedSong(
                savedSongInfo.get(0),
                savedSongInfo.get(1)));
        System.out.println(PrintColor.GREEN + "[Success]: Added saved song to database!" + PrintColor.RESET);
        sendByteFragments(SerializationUtils.serialize(new Response()));
    }

    public void addSong(Request request, Response response) throws IOException, SQLException {
        System.out.println(PrintColor.YELLOW + "[Pending]: Adding song to database..." + PrintColor.RESET);
        ArrayList<String> songInfo = request.getSongInfo();
        new SongService().add(new Song(
                new SerialBlob(request.getSongData()),
                songInfo.get(0),
                Integer.parseInt(songInfo.get(1))));
        System.out.println(PrintColor.GREEN + "[Success]: Added song to database!" + PrintColor.RESET);
        sendByteFragments(SerializationUtils.serialize(response));
    }

    public void addUser(Request request) throws IOException, SQLException {
        System.out.println(PrintColor.YELLOW + "[Pending]: Adding user to database..." + PrintColor.RESET);
        ArrayList<String> userInfo = request.getUserInfo();
        if (new UserService().getUser(userInfo.get(4)) != null) {
            System.out.println(PrintColor.RED + "[Error]:   Username already taken!" + PrintColor.RESET);
            Response response = new Response();
            response.setFunctionCalled(false);
            sendByteFragments(SerializationUtils.serialize(response));
            return;
        }
        new UserService().add(new User(
                userInfo.get(0),
                userInfo.get(1),
                userInfo.get(2),
                userInfo.get(3),
                userInfo.get(4)));
        Response response = new Response();
        response.setUserId(new UserService().getUser(userInfo.get(4)).getId());
        ArrayList<String> songInfo = request.getSongInfo();
        songInfo.set(1, Integer.toString(response.getUserId()));
        request.setSongInfo(songInfo);
        System.out.println(PrintColor.GREEN + "[Success]: Added user to database!" + PrintColor.RESET);
        addSong(request, response);
    }

    public void deleteSavedSong(Request request) throws IOException, SQLException {
        System.out.println(PrintColor.YELLOW + "[Pending]: Deleting saved song from database..." + PrintColor.RESET);
        new SavedSongService().delete(request.getModelId());
        sendByteFragments(SerializationUtils.serialize(new Response()));
        System.out.println(PrintColor.GREEN + "[Success]: Deleted saved song to database!" + PrintColor.RESET);
    }

    public void getSong(Request request) throws IOException, SQLException {
        Song song = new SongService().getSong(request.getModelId());
        Response response = new Response(song != null);
        response.setSongData(SerializationUtils.serialize(song));
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending song to client..." + PrintColor.RESET);
        sendByteFragments(SerializationUtils.serialize(response));
        System.out.println(PrintColor.GREEN + "[Success]: Sent song to client!" + PrintColor.RESET);
    }

    public void getSavedSongs(Request request) throws IOException, SQLException {
        ArrayList<byte[]> savedSongDataList = new ArrayList<>(new SavedSongService()
                .getSavedSongs(request.getModelId()).stream().map(SerializationUtils::serialize).toList());
        Response response = new Response();
        response.setSavedSongDataList(savedSongDataList);
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending saved songs to client..." + PrintColor.RESET);
        sendByteFragments(SerializationUtils.serialize(response));
        System.out.println(PrintColor.GREEN + "[Success]: Sent saved songs to client!" + PrintColor.RESET);
    }

    public void getSongs() throws IOException, SQLException {
        ArrayList<byte[]> songDataList = new ArrayList<>(new SongService().getSongs()
                .stream().map(SerializationUtils::serialize).toList());
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending songs to client..." + PrintColor.RESET);
        sendByteFragments(SerializationUtils.serialize(new Response(songDataList)));
        System.out.println(PrintColor.GREEN + "[Success]: Sent songs to client!" + PrintColor.RESET);
    }

    public void getUser(Request request) throws IOException, SQLException {
        User user = new UserService().getUser(request.getModelId());
        Response response = new Response(user != null);
        response.setUserData(SerializationUtils.serialize(user));
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending user to client..." + PrintColor.RESET);
        sendByteFragments(SerializationUtils.serialize(response));
        System.out.println(PrintColor.GREEN + "[Success]: Sent user to client!" + PrintColor.RESET);
    }

    public void login(Request request) throws IOException, SQLException {
        ArrayList<String> loginInfo = request.getLoginInfo();
        User user = new UserService().getUser(loginInfo.get(0));
        Response response = new Response(user != null && user.getPassword().equals(loginInfo.get(1)));
        response.setUserId(user != null ? user.getId() : -1);
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending login verification..." + PrintColor.RESET);
        sendByteFragments(SerializationUtils.serialize(response));
        if (response.isExists())
            System.out.println(PrintColor.GREEN + "[Success]: Login verification successful!" + PrintColor.RESET);
        else System.out.println(PrintColor.RED + "[Error]:   User doesn't exist!" + PrintColor.RESET);

    }

    public void playSong() {
        // TODO: Download and play audio packets sent from frontend
    }
}
