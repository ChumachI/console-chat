package edu.sockets.services;

import edu.sockets.models.Room;
import edu.sockets.repositories.RoomsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoomsServiceImpl implements RoomsService {

    private final RoomsRepository roomsRepository;

    @Autowired
    public RoomsServiceImpl(RoomsRepository roomsRepository) {
        this.roomsRepository = roomsRepository;
    }

    @Override
    public void saveRoom(Room room) {
        roomsRepository.save(room);
    }

    @Override
    public List<Room> findAll() {
        return roomsRepository.findAll();
    }
}
