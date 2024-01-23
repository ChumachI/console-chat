package edu.sockets.repositories;

import edu.sockets.models.User;
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
public class UsersRepositoryImpl implements UsersRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UsersRepositoryImpl(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public void save(User user) {
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        Map<String, Object> params = new HashMap<>();
        params.put("name", user.getName());
        params.put("password", user.getPassword());
        params.put("roomId", user.getRoomId());
        Number generatedId = simpleJdbcInsert.executeAndReturnKey(params);
        user.setId(generatedId.longValue());
    }

    @Override
    public List<User> findAll() {
        return jdbcTemplate.query("SELECT * FROM users", new BeanPropertyRowMapper<>(User.class));
    }

    @Override
    public void update(User currentUser) {
        String query = "UPDATE users SET name=?, password=?, room=? WHERE id=?";
        jdbcTemplate.update(query,currentUser.getName(), currentUser.getPassword(), currentUser.getRoomId(), currentUser.getId());
    }

    @Override
    public User findByName(String username) {
        String query = "SELECT * FROM users WHERE name = ?";
        return jdbcTemplate.queryForObject(query,new BeanPropertyRowMapper<>(User.class), username);
    }
}
