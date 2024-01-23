package edu.sockets.repositories;

import edu.sockets.models.Message;
import edu.sockets.models.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class MessagesRepositoryImpl implements MessagesRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public MessagesRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Message message) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("messages")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("sender", message.getSender());
        params.put("text", message.getText());
        params.put("date_time", message.getLocalDateTime());
        params.put("room", message.getRoom());
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(params);
        message.setId(generatedId.longValue());
    }

    @Override
    public List<Message> findAll() {
        return jdbcTemplate.query("SELECT * FROM messages", new BeanPropertyRowMapper<>(Message.class));
    }

    @Override
    public void update(Message entity) {
        throw new UnsupportedOperationException();
    }


    @Override
    public List<Message> findMessagesByRoom(Room currentRoom) {
        return jdbcTemplate.query("SELECT * FROM messages WHERE room=?", new BeanPropertyRowMapper<>(Message.class),currentRoom.getId());
    }
}
