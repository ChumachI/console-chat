package edu.sockets.repositories;

import edu.sockets.models.Message;
import edu.sockets.models.Room;

import java.util.List;

public interface MessagesRepository extends CrudRepository<Message> {


    List<Message> findMessagesByRoom(Room currentRoom);
}
