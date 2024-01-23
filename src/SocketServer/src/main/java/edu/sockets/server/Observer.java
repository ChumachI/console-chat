package edu.sockets.server;

import edu.sockets.models.Room;

import java.util.List;

public interface Observer {
    void update(String text, Room currentRoom);

    void addRoom(Room room);

    List<Room> getRooms();
}
