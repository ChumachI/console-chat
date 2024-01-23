package edu.sockets.services;

import edu.sockets.models.Message;
import edu.sockets.models.Room;

import java.util.List;

public interface MessagesService {
    void saveMessage(Message message);

    List<Message> findMessagesByRoom(Room currentRoom);
}
