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
    private final Socket socket;
    private final DataInputStream dataInputStream;
    private final DataOutputStream dataOutputStream;

    public ClientHandler(Socket socket) {
        System.out.println(PrintColor.YELLOW + "[Pending]: Initializing client thread..." + PrintColor.RESET);
        this.socket = socket;
        try {
            this.dataInputStream = new DataInputStream(socket.getInputStream());
            this.dataOutputStream = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) { throw new RuntimeException(e); }
        System.out.println(PrintColor.GREEN + "[Success]: Initialized client thread!" + PrintColor.RESET);
    }

    public void run() {
        while (running) {
            System.out.println(PrintColor.YELLOW + "[Success]: Calling function... " + PrintColor.RESET);
            Request request = getRequest();
            System.out.println(PrintColor.GREEN + "[Success]: Called function: " + "\033[1;34m" + request.getFunction()
                    + PrintColor.RESET);
            switch (request.getFunction()) {
                case "@addSavedSong" -> {
                    try { addSavedSong(request); }
                    catch (Exception e) { throw new RuntimeException(e); }
                }
                case "@addSong" -> {
                    try { addSong(request); }
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
                case "@exit" -> running = false;
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
                default -> System.out.println(PrintColor.RED + "[Error]:   Request function invalid!"
                        + PrintColor.RESET);
            }
        }
        try {
            dataInputStream.close();
            dataOutputStream.close();
            socket.close();
        } catch (IOException e) { throw new RuntimeException(e); }
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

    public void addSavedSong(Request request) {
        try {
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
        } catch (SQLException e) { throw new RuntimeException(e); }
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
        try {
            System.out.println(PrintColor.YELLOW + "[Pending]: Adding user to database..." + PrintColor.RESET);
            ArrayList<String> userInfo = request.getUserInfo();
            if (new UserService().getUser(userInfo.get(4)) != null) {
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
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void deleteSavedSong(Request request) {
        try {
            System.out.println(PrintColor.YELLOW + "[Pending]: Deleting saved song from database..." + PrintColor.RESET);
            new SavedSongService().delete(request.getModelId());
            System.out.println(PrintColor.GREEN + "[Success]: Deleted saved song to database!" + PrintColor.RESET);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void getSong(Request request) {
        try {
            System.out.println(PrintColor.YELLOW + "[Pending]: Sending song to client..." + PrintColor.RESET);
            Song song = new SongService().getSong(request.getModelId());
            Response response = new Response(song != null);
            response.setSongData(SerializationUtils.serialize(song));
            sendResponse(response);
            System.out.println(PrintColor.GREEN + "[Success]: Sent song to client!" + PrintColor.RESET);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void getSavedSongs(Request request) {
        try {
            System.out.println(PrintColor.YELLOW + "[Pending]: Sending saved songs to client..." + PrintColor.RESET);
            ArrayList<byte[]> savedSongDataList = new ArrayList<>(new SavedSongService()
                    .getSavedSongs(request.getModelId()).stream().map(SerializationUtils::serialize).toList());
            Response response = new Response();
            response.setSavedSongDataList(savedSongDataList);
            sendResponse(response);
            System.out.println(PrintColor.GREEN + "[Success]: Sent saved songs to client!" + PrintColor.RESET);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void getSongs() {
        try {
            System.out.println(PrintColor.YELLOW + "[Pending]: Sending songs to client..." + PrintColor.RESET);
            ArrayList<byte[]> songDataList = new ArrayList<>(new SongService().getSongs()
                    .stream().map(SerializationUtils::serialize).toList());
            sendResponse(new Response(songDataList));
            System.out.println(PrintColor.GREEN + "[Success]: Sent songs to client!" + PrintColor.RESET);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void getUser(Request request) {
        try {
            System.out.println(PrintColor.YELLOW + "[Pending]: Sending user to client..." + PrintColor.RESET);
            User user = new UserService().getUser(request.getModelId());
            Response response = new Response(user != null);
            response.setUserData(SerializationUtils.serialize(user));
            sendResponse(response);
            System.out.println(PrintColor.GREEN + "[Success]: Sent user to client!" + PrintColor.RESET);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void login(Request request) {
        try {
            System.out.println(PrintColor.YELLOW + "[Pending]: Sending login verification..." + PrintColor.RESET);
            ArrayList<String> loginInfo = request.getLoginInfo();
            User user = new UserService().getUser(loginInfo.get(0));
            Response response = new Response(user != null && user.getPassword().equals(loginInfo.get(1)));
            response.setUserId(user != null ? user.getId() : -1);
            sendResponse(response);
            if (response.isExists())
                System.out.println(PrintColor.GREEN + "[Success]: Login verification successful!" + PrintColor.RESET);
            else System.out.println(PrintColor.RED + "[Error]:   User doesn't exist!" + PrintColor.RESET);
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void playSong() {
        // TODO: Download and play audio packets sent from frontend
    }
}
