package application.service.user;

import application.port.in.user.AuthenticateUserUseCase;
import application.port.in.user.exceptions.UserAlreadyExistsException;
import application.port.in.user.exceptions.WrongLoginPasswordException;
import application.port.out.AuditRepository;
import application.port.out.UserMetersRepository;
import application.port.out.UserRepository;
import model.user.User;
import model.usermeter.UserMeters;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Сервис для аутентификации пользователя
 */
public class AuthenticateUserService implements AuthenticateUserUseCase {
    UserRepository userRepository;
    UserMetersRepository userMetersRepository;
    AuditRepository auditRepository;

    public AuthenticateUserService(UserRepository userRepository, UserMetersRepository userMetersRepository, AuditRepository auditRepository) {
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
    public void registerUser(String username, String password) throws UserAlreadyExistsException {
        auditRepository.saveAudit(username, username + " tries to register");

        if (userRepository.userExists(username)) {
            throw new UserAlreadyExistsException();
        }
        User user = new User(username, hashPassword(password));
        userRepository.saveUser(user);
        userMetersRepository.saveUserMeters(new UserMeters(user));

        auditRepository.saveAudit(username, username + " registered");
    }

    /**
     * Функция для аутентификации пользователя
     * @param username имя пользователя
     * @param password пароль
     * @return данные пользователя
     * @throws WrongLoginPasswordException если логин или пароль неверный
     */
    @Override
    public User loginUser(String username, String password) throws WrongLoginPasswordException {
        auditRepository.saveAudit(username, username + " tries to login");

        User user = userRepository.getUser(username);
        if (user == null) {
            throw new WrongLoginPasswordException();
        }
        if (!user.getEncodedPassword().equals(hashPassword(password))) {
            throw new WrongLoginPasswordException();
        }

        auditRepository.saveAudit(username, username + " logged in");
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
