package ylab.adapter.repository.inmemory;

import application.port.repository.UserRepository;
import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;
import model.user.User;
import model.user.UserRole;

import java.util.ArrayList;
import java.util.List;

import static application.service.UserServiceImpl.hashPassword;

/**
 * Реализация репозитория пользователей в памяти
 */
public class InMemoryUserRepository implements UserRepository {
    /**
     * Список пользователей
     */
    private final List<User> users = new ArrayList<>();

    public InMemoryUserRepository() {
        try {
            var user = new User("admin", hashPassword("123"), UserRole.ADMIN);
            this.users.add(user);
        } catch (WrongUsernameException | WrongPasswordException e) {
            System.err.println("Wrong admin init username or password!");
        }
    }

    @Override
    public User postUser(User user) {
        users.add(user);
        return user;
    }

    @Override
    public User getUser(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean userExists(String username) {
        return users.stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }
}
