package application.service;

import application.port.in.UserService;
import application.port.in.exceptions.UserAlreadyExistsException;
import application.port.in.exceptions.WrongLoginPasswordException;
import application.port.repository.AuditRepository;
import application.port.repository.UserMetersRepository;
import application.port.repository.UserRepository;
import application.service.exceptions.UnsafePasswordException;
import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;
import model.user.User;
import model.usermeter.UserMeters;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Сервис для аутентификации пользователя
 */
public class UserServiceImpl implements UserService {
    private final Connection connection;
    private final UserRepository userRepository;
    private final UserMetersRepository userMetersRepository;
    private final AuditRepository auditRepository;

    public UserServiceImpl(Connection connection, UserRepository userRepository, UserMetersRepository userMetersRepository, AuditRepository auditRepository) {
        this.connection = connection;
        this.userRepository = userRepository;
        this.userMetersRepository = userMetersRepository;
        this.auditRepository = auditRepository;
    }

    /**
     * Функция для регистрации пользователя
     * @param username имя пользователя
     * @param password пароль
     * @throws UserAlreadyExistsException если пользователь с таким именем уже существует
     */
    @Override
    public void registerUser(String username, String password) throws UserAlreadyExistsException, WrongUsernameException, WrongPasswordException, UnsafePasswordException, SQLException {
        connection.setAutoCommit(false);
        auditRepository.saveAudit(username, username + " tries to register");
        connection.commit();

        if (userRepository.userExists(username)) {
            throw new UserAlreadyExistsException();
        }
        if (password.length() < 4) {
            throw new UnsafePasswordException("Password is too short!");
        }
        User user = new User(username, hashPassword(password));

        connection.setAutoCommit(false);
        user = userRepository.postUser(user);
        userMetersRepository.saveUserMeters(new UserMeters(user));
        auditRepository.saveAudit(username, username + " registered");
        connection.commit();
    }

    /**
     * Функция для аутентификации пользователя
     * @param username имя пользователя
     * @param password пароль
     * @return данные пользователя
     * @throws WrongLoginPasswordException если логин или пароль неверный
     */
    @Override
    public User loginUser(String username, String password) throws WrongLoginPasswordException, SQLException, WrongUsernameException, WrongPasswordException {
        connection.setAutoCommit(false);
        auditRepository.saveAudit(username, username + " tries to login");
        connection.commit();

        User user = userRepository.getUser(username);
        if (user == null) {
            throw new WrongLoginPasswordException();
        }
        if (!user.getEncodedPassword().equals(hashPassword(password))) {
            throw new WrongLoginPasswordException();
        }

        connection.setAutoCommit(false);
        auditRepository.saveAudit(username, username + " logged in");
        connection.commit();
        return user;
    }

    /**
     * Функция для хэширования пароля
     * @param password пароль
     * @return хэш пароля
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            byte[] hashedBytes = digest.digest(password.getBytes());

            StringBuilder hexString = new StringBuilder();
            for (byte hashedByte : hashedBytes) {
                String hex = Integer.toHexString(0xff & hashedByte);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
}
