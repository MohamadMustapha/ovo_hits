package com.example.ovohits.backend;

import com.example.ovohits.Request;
import com.example.ovohits.backend.database.models.SavedSong;
import com.example.ovohits.backend.database.models.Song;
import com.example.ovohits.backend.database.models.User;
import com.example.ovohits.backend.database.services.SavedSongService;
import com.example.ovohits.backend.database.services.SongService;
import com.example.ovohits.backend.database.services.UserService;
import org.apache.commons.lang3.SerializationUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private boolean running = true;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;
    private final int port;
    private final Socket socket;

    public ClientHandler(int port, Socket socket) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Initializing client thread..." + PrintColor.RESET);
        this.port = port;
        this.socket = socket;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) { throw new RuntimeException(e); }
        System.out.println(PrintColor.GREEN + "[Success]: Initialized client thread on port: " + PrintColor.PURPLE
                + port + PrintColor.RESET);
    }

    private Request getRequest() {
        try {
            byte[] requestData = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(requestData);
            return SerializationUtils.deserialize(requestData);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private void sendResponse(Response response) {
        try {
            byte[] responseData = SerializationUtils.serialize(response);
            dataOutputStream.writeInt(responseData.length);
            dataOutputStream.write(responseData);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void run() {
        while (running) {
            System.out.println(PrintColor.YELLOW + "[Pending]: Calling function... " + PrintColor.RESET);
            Request request = getRequest();
            System.out.println(PrintColor.GREEN + "[Success]: Called function: " + "\033[1;34m" + request.getFunction()
                    + PrintColor.RESET);
            switch (request.getFunction()) {
                case "@addSavedSong" -> addSavedSong(request);
                case "@addSong" -> addSong(request);
                case "@addUser" -> addUser(request);
                case "@deleteSavedSong" -> deleteSavedSong(request);
                case "@exit" -> exit(request);
                case "@getAllSongs" -> getAllSongs();
                case "@getOnlineUsers" -> getOnlineUsers();
                case "@getSong" -> {
                    if (request.getUsername().isBlank()) getSong(request);
                    else getSong(request, request.getUsername());
                }
                case "@getSavedSongs" -> getSavedSongs(request);
                case "@getSongs" -> {
                    if (request.getModelId() == -1) getSongs();
                    else getSongs(request.getModelId());
                }
                case "@getUser" -> getUser(request);
                case "@login" -> login(request);
                case "@playSong" -> playSong();
                default -> System.out.println(PrintColor.RED + "[Error]:   Request function invalid!"
                        + PrintColor.RESET);
            }
        }
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) { throw new RuntimeException(e); }
        System.out.println(PrintColor.GREEN + "[Success]: Terminated thread on port: " + PrintColor.PURPLE
                + port + PrintColor.RESET);
    }

    public void addSavedSong(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Adding saved song to database..." + PrintColor.RESET);
        ArrayList<Integer> savedSongInfo = request.getSavedSongInfo();
        ArrayList<Integer> songIdList = new ArrayList<>(new SavedSongService()
                .getSavedSongs(savedSongInfo.get(1)).stream().map(SavedSong::getSongId).toList());
        if (songIdList.contains(savedSongInfo.get(0))) {
            System.out.println(PrintColor.RED + "[Error]:   Saved song already added!" + PrintColor.RESET);
            return;
        }
        new SavedSongService().add(new SavedSong(
                savedSongInfo.get(0),
                savedSongInfo.get(1)));
        System.out.println(PrintColor.GREEN + "[Success]: Added saved song to database!" + PrintColor.RESET);
        sendResponse(new Response());
    }

    public void addSong(Request request) {
        try {
            System.out.println(PrintColor.YELLOW + "[Pending]: Adding song to database..." + PrintColor.RESET);
            ArrayList<String> songInfo = request.getSongInfo();
            new SongService().add(new Song(
                    new SerialBlob(request.getSongData()),
                    songInfo.get(0),
                    Integer.parseInt(songInfo.get(1))));
            System.out.println(PrintColor.GREEN + "[Success]: Added song to database!" + PrintColor.RESET);
        } catch (SQLException e) { throw new RuntimeException(e); }

    }

    public void addUser(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Adding user to database..." + PrintColor.RESET);
        ArrayList<String> userInfo = request.getUserInfo();
        if (new UserService().getUser(userInfo.get(4)) != null) {
            Response response = new Response();
            response.setFunctionCalled(false);
            sendResponse(response);
            System.out.println(PrintColor.RED + "[Error]:   Username already taken!" + PrintColor.RESET);
            return;
        }
        new UserService().add(new User(
                userInfo.get(0),
                userInfo.get(1),
                userInfo.get(2),
                userInfo.get(3),
                userInfo.get(4)));
        ArrayList<String> songInfo = request.getSongInfo();
        songInfo.set(1, Integer.toString(new UserService().getUser(userInfo.get(4)).getId()));
        request.setSongInfo(songInfo);
        System.out.println(PrintColor.GREEN + "[Success]: Added user to database!" + PrintColor.RESET);
        sendResponse(new Response());
        addSong(request);
    }

    public void deleteSavedSong(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Deleting saved song from database..." + PrintColor.RESET);
        new SavedSongService().delete(request.getModelId());
        System.out.println(PrintColor.GREEN + "[Success]: Deleted saved song to database!" + PrintColor.RESET);
    }

    public void exit(Request request) {
        running = false;
        if (request.getModelId() != -1) Server.deleteClient(request.getModelId());
    }

    public void getAllSongs() {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending all songs from database..." + PrintColor.RESET);
        ArrayList<byte[]> songDataList = new ArrayList<>(Server.getSongs().stream()
                .map(SerializationUtils::serialize).toList());
        sendResponse(new Response(songDataList));
        System.out.println(PrintColor.GREEN + "[SUCCESS]: Sent all songs from database!" + PrintColor.RESET);
    }

    public void getOnlineUsers() {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending online users from database..." + PrintColor.RESET);
        ArrayList<String> onlineUsernameList = new ArrayList<>(Server.getOnlineUsers().stream().map(integer ->
                new UserService().getUser(integer).getUsername()).toList());
        Response response = new Response();
        response.setOnlineUsernameList(onlineUsernameList);
        sendResponse(response);
        System.out.println(PrintColor.GREEN + "[SUCCESS]: Sent online users from database!" + PrintColor.RESET);
    }

    public void getSong(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending song to client..." + PrintColor.RESET);
        Song song = new SongService().getSong(request.getModelId());
        Response response = new Response(song != null);
        response.setSongData(SerializationUtils.serialize(song));
        sendResponse(response);
        System.out.println(PrintColor.GREEN + "[Success]: Sent song to client!" + PrintColor.RESET);
    }

    public void getSong(Request request, String username) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending song to client..." + PrintColor.RESET);
        Song song = new SongService().getSongs(new UserService().getUser(username).getId()).stream()
                .filter(song_ -> song_.getId() == request.getModelId()).findFirst().orElse(null);
        Response response = new Response(song != null);
        response.setSongData(SerializationUtils.serialize(song));
        sendResponse(response);
        System.out.println(PrintColor.GREEN + "[Success]: Sent song to client!" + PrintColor.RESET);
    }

    public void getSavedSongs(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending saved songs to client..." + PrintColor.RESET);
        ArrayList<byte[]> savedSongDataList = new ArrayList<>(new SavedSongService()
                .getSavedSongs(request.getModelId()).stream().map(SerializationUtils::serialize).toList());
        Response response = new Response();
        response.setSavedSongDataList(savedSongDataList);
        sendResponse(response);
        System.out.println(PrintColor.GREEN + "[Success]: Sent saved songs to client!" + PrintColor.RESET);
    }

    public void getSongs() {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending songs to client..." + PrintColor.RESET);
        ArrayList<byte[]> songDataList = new ArrayList<>(new SongService().getSongs()
                .stream().map(SerializationUtils::serialize).toList());
        sendResponse(new Response(songDataList));
        System.out.println(PrintColor.GREEN + "[Success]: Sent songs to client!" + PrintColor.RESET);
    }

    public void getSongs(int userId) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending songs to client..." + PrintColor.RESET);
        ArrayList<byte[]> songDataList = new ArrayList<>(new SongService().getSongs(userId)
                .stream().map(SerializationUtils::serialize).toList());
        sendResponse(new Response(songDataList));
        System.out.println(PrintColor.GREEN + "[Success]: Sent songs to client!" + PrintColor.RESET);
    }

    public void getUser(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending user to client..." + PrintColor.RESET);
        User user = request.getModelId() == -1 ? new UserService().getUser(request.getUsername()) :
                new UserService().getUser(request.getModelId());
        Response response = new Response(user != null);
        response.setUserData(SerializationUtils.serialize(user));
        sendResponse(response);
        System.out.println(PrintColor.GREEN + "[Success]: Sent user to client!" + PrintColor.RESET);
    }

    public void login(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending login verification..." + PrintColor.RESET);
        ArrayList<String> loginInfo = request.getLoginInfo();
        User user = new UserService().getUser(loginInfo.get(0));
        Response response = new Response(user != null && user.getPassword().equals(loginInfo.get(1)));
        response.setUserId(user != null ? user.getId() : -1);
        sendResponse(response);
        if (response.isExists()) {
            Server.addClient(response.getUserId(), socket.getLocalAddress(), port);
            System.out.println(PrintColor.GREEN + "[Success]: Login verification successful!" + PrintColor.RESET);
        }
        else System.out.println(PrintColor.RED + "[Error]:   User doesn't exist!" + PrintColor.RESET);
    }

    public void playSong() {
        // TODO: Download and play audio packets sent from frontend
    }
}
