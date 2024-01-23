package edu.sockets.server;

import edu.sockets.models.Room;
import edu.sockets.services.MessagesService;
import edu.sockets.services.RoomsService;
import edu.sockets.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

@Component
public class Server implements Observer {
    private final Integer port;
    private final UsersService usersService;
    private final MessagesService messagesService;
    private final RoomsService roomsService;
    private final List<ClientHandler> clients = new ArrayList<>();
    private List<Room> rooms = new ArrayList<>();

    @Autowired
    public Server(Integer port, UsersService usersService, MessagesService messagesService, RoomsService roomsService) {
        this.port = port;
        this.usersService = usersService;
        this.messagesService = messagesService;
        this.roomsService = roomsService;
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                rooms = roomsService.findAll();
                ClientHandler clientHandler = new ClientHandler(socket, usersService, messagesService, roomsService);
                clientHandler.setObserver(this);
                clients.add(clientHandler);
                new Thread(clientHandler).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(String text, Room currentRoom) {
        for (ClientHandler clientHandler : clients) {
            if (clientHandler.getCurrentRoom().getId().equals(currentRoom.getId())) {
                clientHandler.sendMessage(text);
            }
        }
    }

    @Override
    public void addRoom(Room room) {
        rooms.add(room);
    }

    @Override
    public List<Room> getRooms() {
        return rooms;
    }

}