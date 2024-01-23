package edu.sockets.services;

import edu.sockets.models.Message;
import edu.sockets.models.Room;
import edu.sockets.repositories.MessagesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MessagesServiceImpl implements MessagesService {
    private final MessagesRepository messagesRepository;

    @Autowired
    public MessagesServiceImpl(MessagesRepository messagesRepository) {
        this.messagesRepository = messagesRepository;
    }

    @Override
    public void saveMessage(Message message) {
        messagesRepository.save(message);
    }

    @Override
    public List<Message> findMessagesByRoom(Room currentRoom) {
        return messagesRepository.findMessagesByRoom(currentRoom);
    }
}
