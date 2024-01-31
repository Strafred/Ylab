package ylab.adapter.out;

import application.port.out.UserRepository;
import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;
import model.user.User;
import model.user.UserRole;

import java.util.ArrayList;
import java.util.List;

import static application.service.user.AuthenticateUserService.hashPassword;

/**
 * Реализация репозитория пользователей в памяти
 */
public class InMemoryUserRepository implements UserRepository {
    /**
     * Список пользователей
     */
    List<User> users = new ArrayList<>();

    public InMemoryUserRepository() {
        try {
            var user = new User("admin", hashPassword("123"), UserRole.ADMIN);
            this.users.add(user);
        } catch (WrongUsernameException | WrongPasswordException e) {
            System.err.println("Wrong admin init username or password!");
        }
    }

    @Override
    public void saveUser(User user) {
        users.add(user);
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
