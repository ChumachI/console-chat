package edu.sockets.repositories;

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
public class RoomsRepositoryImpl implements RoomsRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public RoomsRepositoryImpl(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(Room entity) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("rooms")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", entity.getName());
        Number id = insert.executeAndReturnKey(params);
        entity.setId(id.longValue());
    }

    @Override
    public List<Room> findAll() {
        String query = "SELECT * FROM rooms";
        return jdbcTemplate.query(query,new BeanPropertyRowMapper<>(Room.class));
    }

    @Override
    public void update(Room entity) {
        throw new UnsupportedOperationException();
    }

}
