package edu.sockets.services;

import edu.sockets.models.User;

public interface UsersService {
     void signUp(String name, String password);

    User signIn(String username, String password);

    void update(User currentUser);
}
