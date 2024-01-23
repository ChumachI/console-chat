package edu.sockets.services;

import edu.sockets.models.User;
import edu.sockets.repositories.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserServiceImpl implements UsersService{

    private final PasswordEncoder passwordEncoder;
    private final UsersRepository usersRepository;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UsersRepository usersRepository) {
        this.passwordEncoder = passwordEncoder;
        this.usersRepository = usersRepository;
    }

    @Override
    public void signUp(String name, String password) {
        password = passwordEncoder.encode(password);
        usersRepository.save(new User(null,name,password, null));
    }

    @Override
    public User signIn(String username, String password) {
        User user = usersRepository.findByName(username);
        if(passwordEncoder.matches(password, user.getPassword())){
            return user;
        }
        return null;
    }

    @Override
    public void update(User currentUser) {
        usersRepository.update(currentUser);
    }
}
