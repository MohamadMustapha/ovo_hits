package com.example.ovohits.backend;

import com.example.ovohits.ClientResponse;
import com.example.ovohits.Request;
import com.example.ovohits.backend.database.models.SavedSong;
import com.example.ovohits.backend.database.models.Song;
import com.example.ovohits.backend.database.models.User;
import com.example.ovohits.backend.database.services.SavedSongService;
import com.example.ovohits.backend.database.services.SongService;
import com.example.ovohits.backend.database.services.UserService;
import javafx.util.Pair;
import org.apache.commons.lang3.SerializationUtils;

import javax.sql.rowset.serial.SerialBlob;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

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

    private ClientResponse getClientResponse() {
        try {
            byte[] clientResponseData = new byte[dataInputStream.readInt()];
            dataInputStream.readFully(clientResponseData);
            return SerializationUtils.deserialize(clientResponseData);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    private void sendServerRequest(ServerRequest serverRequest, Socket socket) {
        try {
            DataOutputStream clientDataOutputStream = new DataOutputStream(socket.getOutputStream());
            byte[] serverRequestData = SerializationUtils.serialize(serverRequest);
            clientDataOutputStream.writeInt(serverRequestData.length);
            clientDataOutputStream.write(serverRequestData);
        } catch (IOException e) { throw new RuntimeException(e); }
    }

    public void run() {
        while (running) {
            System.out.println(PrintColor.YELLOW + "[Pending]: Calling function... " + PrintColor.RESET);
            Request request = getRequest();
            System.out.println(PrintColor.GREEN + "[Success]: Called function: " + PrintColor.BLUE
                    + request.getFunction() + PrintColor.RESET);
            switch (request.getFunction()) {
                case "@addSavedSong" -> addSavedSong(request);
                case "@addServerHandler" -> addServerHandler(request);
                case "@addSong" -> addSong(request);
                case "@addUser" -> addUser(request);
                case "@deleteSavedSong" -> deleteSavedSong(request);
                case "@exit" -> exit(request);
                case "@filterSongs" -> filterSongs(request);
                case "@getOnlineUsers" -> getOnlineUsers();
                case "@getSong" -> getSong(request);
                case "@getSavedSongs" -> getSavedSongs(request);
                case "@getSongs" -> getSongs(request);
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

    public void addServerHandler(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Adding server handler to server..." + PrintColor.RESET);
        try (Socket serverHandlerSocket = new Socket(socket.getLocalAddress(), 6969 * 2 - port)) {
            Server.addServerHandler(request.getModelId(), serverHandlerSocket);
        } catch (IOException e) { throw new RuntimeException(e); }
        System.out.println(PrintColor.GREEN + "[Success]: Added server handler to server!" + PrintColor.RESET);
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
        Server.addServerHandler(new UserService().getUser(userInfo.get(4)).getId(), socket);
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
        if (request.getModelId() != -1)
            Server.deleteServerHandler(request.getModelId());
    }

    public void filterSongs(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending filtered songs from database..." + PrintColor.RESET);
        User user = new UserService().getUser(request.getUsername());
        if (user == null) {
            Response response = new Response();
            response.setFunctionCalled(false);
            sendResponse(response);
            System.out.println(PrintColor.RED + "[Error]:   User not found!" + PrintColor.RESET);
            return;
        }
        ArrayList<Pair<String, Integer>> songList = new ArrayList<>(new SongService().getSongs(user.getId()).stream()
                .map(song -> new Pair<>(song.getName(), song.getId())).toList());
        sendResponse(new Response(songList));
        System.out.println(PrintColor.GREEN + "[SUCCESS]: Sent Filtered songs from database!" + PrintColor.RESET);
    }

    public void getOnlineUsers() {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending online users from database..." + PrintColor.RESET);
        ArrayList<String> onlineUsernameList = new ArrayList<>(Server.getOnlineUsers().stream()
                .filter(userId -> userId != -1).map(userId ->
                        new UserService().getUser(userId).getUsername()).toList());
        Response response = new Response();
        response.setOnlineUsernameList(onlineUsernameList);
        sendResponse(response);
        System.out.println(PrintColor.GREEN + "[SUCCESS]: Sent online users from database!" + PrintColor.RESET);
    }

    public void getSavedSongs(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending saved songs to client..." + PrintColor.RESET);
        ArrayList<Pair<Integer, Integer>> savedSongList = new ArrayList<>(new SavedSongService()
                .getSavedSongs(request.getModelId()).stream().map(
                        savedSong -> new Pair<>(
                                savedSong.getSongId(),
                                savedSong.getUserId())).toList());
        Response response = new Response();
        response.setSavedSongList(savedSongList);
        sendResponse(response);
        System.out.println(PrintColor.GREEN + "[Success]: Sent saved songs to client!" + PrintColor.RESET);
    }

    public void getSong(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending song to client..." + PrintColor.RESET);
        Response response = new Response(false);
        for (Map.Entry<Integer, Socket> client : Server.getServerHandlers().entrySet()) {
            if (client.getKey() == -1) {
                Song song = request.getUsername().isBlank() ?
                        new SongService().getSong(request.getModelId()) :
                        new SongService().getSongs(new UserService().getUser(request.getUsername()).getId()).stream()
                                .filter(song_ -> song_.getId() == request.getModelId()).findFirst().orElse(null);
                if (song != null) {
                    response.setExists(true);
                    response.setSongData(SerializationUtils.serialize(song));
                    break;
                }
            }
            else {
                sendServerRequest(
                        new ServerRequest(request.getUsername().isBlank() ?
                                new UserService().getUser(request.getUsername()).getId() :
                                request.getModelId(),
                                "@getSong"),
                        client.getValue());
                ClientResponse clientResponse = getClientResponse();
                Song song = SerializationUtils.deserialize(clientResponse.getSongData());
                if (song != null) {
                    response.setExists(true);
                    response.setSongData(clientResponse.getSongData());
                    break;
                }
            }
        }
        sendResponse(response);
        System.out.println(PrintColor.GREEN + "[Success]: Sent song to client!" + PrintColor.RESET);
    }

    public void getSongs(Request request) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Sending songs to client..." + PrintColor.RESET);
        ArrayList<Pair<String, Integer>> songList = new ArrayList<>();
        for (Map.Entry<Integer, Socket> client : Server.getServerHandlers().entrySet()) {
            if (client.getKey() == -1)
                songList.addAll(request.getModelId() == -1 ?
                        new SongService().getSongs().stream().map(song ->
                                new Pair<>(song.getName(), song.getId())).toList() :
                        new SongService().getSongs(request.getModelId()).stream().map(song ->
                                new Pair<>(song.getName(), song.getId())).toList());
            else {
                sendServerRequest(request.getModelId() == -1 ?
                        new ServerRequest("@getSongs") :
                        new ServerRequest(request.getModelId(), "@getSongs"), client.getValue());
                ClientResponse clientResponse = getClientResponse();
                songList.addAll(clientResponse.getSongList());
            }
        }
        sendResponse(new Response(songList));
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
        Response response = new Response(
                user != null && user.getPassword().equals(loginInfo.get(1)) &&
                        !Server.getOnlineUsers().contains(user.getId()));
        response.setUserId(user != null ? user.getId() : -1);
        sendResponse(response);
        if (response.isExists())
            System.out.println(PrintColor.GREEN + "[Success]: Login verification successful!" + PrintColor.RESET);
        else {
            if (user == null) System.out.println(PrintColor.RED + "[Error]:   User doesn't exist!" + PrintColor.RESET);
            else System.out.println(PrintColor.RED + "[Error]:   User already logged in!" + PrintColor.RESET);
        }
    }

    public void playSong() {
        // TODO: Download and play audio packets sent from frontend
    }
}
