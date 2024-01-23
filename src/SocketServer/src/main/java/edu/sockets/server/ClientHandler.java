package edu.sockets.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.sockets.models.Message;
import edu.sockets.models.Room;
import edu.sockets.models.User;
import edu.sockets.services.MessagesService;
import edu.sockets.services.RoomsService;
import edu.sockets.services.UsersService;
import org.springframework.dao.EmptyResultDataAccessException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class ClientHandler implements Runnable {

    private final Socket socket;
    private final UsersService usersService;
    private final MessagesService messagesService;
    private final RoomsService roomsService;
    private Scanner in;
    private PrintWriter out;
    private final ObjectMapper objectMapper;
    private Observer observer;
    private boolean exitFlag;
    private User currentUser;
    private Room currentRoom;

    public ClientHandler(Socket socket, UsersService usersService, MessagesService messagesService, RoomsService roomsService) {
        this.socket = socket;
        this.usersService = usersService;
        this.messagesService = messagesService;
        this.roomsService = roomsService;
        exitFlag = false;
        objectMapper = new ObjectMapper();
    }

    public void setObserver(Observer observer) {
        this.observer = observer;
    }

    @Override
    public void run() {
        try {
            in = new Scanner(socket.getInputStream());
            out = new PrintWriter(socket.getOutputStream());
            sendServerMessage("Hello from Server!");
            while (!exitFlag) {
                showMainMenu();
                String userResponse = acceptUserCommand();
                switch (userResponse) {
                    case "1":
                        signIn();
                        break;
                    case "2":
                        signUp();
                        break;
                    case "3":
                        exit();
                        break;
                    default:
                        sendServerMessage("Unknown command: " + userResponse + ".");
                        sendServerMessage("Try again.");
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String acceptUserCommand() throws JsonProcessingException {
        String json = in.nextLine();
        HashMap<String, Object> jsonMap = objectMapper.readValue(json, HashMap.class);
        return jsonMap.get("message").toString();
    }

    private void signIn() {
        try {
            sendServerMessage("Enter username:");
            String username = acceptUserCommand();
            sendServerMessage("Enter password:");
            String password = acceptUserCommand();
            currentUser = usersService.signIn(username, password);
            showRoomMenu();
            showPreviousMessages();
            sendServerMessage("Start messaging");
            startMessaging();
        } catch (EmptyResultDataAccessException e) {
            sendServerMessage("Incorrect user name or password");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void showPreviousMessages() throws JsonProcessingException {
        List<Message> messages = messagesService.findMessagesByRoom(currentRoom);
        int size = messages.size();
        if (size > 30) {
            size = 30;
        }
        for (int i = messages.size() - size; i < messages.size(); i++) {
            sendMessage(objectMapper.writeValueAsString(messages.get(i)));
        }
    }

    private void startMessaging() throws IOException {
        while (!exitFlag) {
            String input = acceptUserCommand();
            if (input != null && input.equalsIgnoreCase("exit")) {
                exit();
                break;
            }
            Message message = new Message(null, currentUser.getName(), input, new Timestamp(System.currentTimeMillis()), currentRoom.getId());
            messagesService.saveMessage(message);
            update(objectMapper.writeValueAsString(message), currentRoom);
        }
    }

    private void showRoomMenu() throws IOException {
        sendServerMessage("1.    Create room");
        sendServerMessage("2.    Choose room");
        sendServerMessage("3.    Exit");
        String response = acceptUserCommand();
        switch (response) {
            case "1":
                createRoom();
                break;
            case "2":
                chooseRoom();
                break;
            case "3":
                exit();
                break;
        }
    }

    private void chooseRoom() throws JsonProcessingException {
        List<Room> rooms = observer.getRooms();
        sendServerMessage("Rooms:");
        for (int i = 0; i < rooms.size(); i++) {
            sendServerMessage((i + 1) + ". " + rooms.get(i).getName());
        }
        boolean isNumerValid = false;
        String choice = acceptUserCommand();
        int choiceNum = -1;
        while (!isNumerValid) {

            try {
                choiceNum = Integer.parseInt(choice) - 1;
                isNumerValid = true;
            } catch (NumberFormatException e) {
                sendServerMessage("Invalid room index");
            }
        }
        currentRoom = rooms.get(choiceNum);
        currentUser.setRoomId(currentRoom.getId());
        usersService.update(currentUser);
        sendServerMessage(currentRoom.getName() + " ---");
    }

    private void createRoom() throws JsonProcessingException {
        sendServerMessage("Enter rooms name");
        String name = acceptUserCommand();
        Room room = new Room(null, name);
        roomsService.saveRoom(room);
        currentRoom = room;
        observer.addRoom(room);
        sendServerMessage("New room with id " + room.getId() + " created!");
    }

    private void signUp() throws JsonProcessingException {
        sendServerMessage("Enter username:");
        String username = acceptUserCommand();
        sendServerMessage("Enter password:");
        String password = acceptUserCommand();
        try {
            usersService.signUp(username, password);
            sendServerMessage("Successful!");
        } catch (RuntimeException e) {
            sendServerMessage("User already exists");
        }
    }

    public void sendMessage(String text) {
        out.println(text);
        out.flush();
    }

    public void sendServerMessage(String text) {
        out.println("{\"sender\":\"server\",\"text\": \"" + text + "\"}");
        out.flush();
    }

    public void update(String text, Room currentRoom) {
        observer.update(text, currentRoom);
    }

    private void exit() throws IOException {
        sendServerMessage("You have left the chat.");
        in.close();
        out.close();
        socket.close();
        exitFlag = true;
    }

    private void showMainMenu() {
        sendServerMessage("1. signIn");
        sendServerMessage("2. signUp");
        sendServerMessage("3. Exit");
    }

    public Room getCurrentRoom() {
        return currentRoom;
    }

    public User getCurrentUser() {
        return currentUser;
    }
}
