package edu.sockets.services;

import edu.sockets.models.Room;

import java.util.List;

public interface RoomsService {
    void saveRoom(Room room);
    List<Room> findAll();
}
