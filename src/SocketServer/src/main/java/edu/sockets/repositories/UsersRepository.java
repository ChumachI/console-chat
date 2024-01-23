package edu.sockets.repositories;

import edu.sockets.models.User;

public interface UsersRepository extends CrudRepository<User> {
    User findByName(String username);
}
