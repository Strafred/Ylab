package ylab.adapter.repository.postgresql;

import application.port.repository.UserRepository;
import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;
import model.user.User;
import model.user.UserRole;

import java.sql.Connection;
import java.sql.SQLException;

public class PostgresUserRepository implements UserRepository {
    Connection connection;

    public PostgresUserRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User putUser(User user) throws SQLException, WrongUsernameException, WrongPasswordException {
        String sql = "INSERT INTO monitoring_service_schema.user (username, encoded_password, role) VALUES (?, ?, ?)";

        var statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        statement.setString(2, user.getEncodedPassword());
        statement.setString(3, user.getRole().toString());
        statement.executeUpdate();

        sql = "SELECT * FROM monitoring_service_schema.user WHERE username = ?";
        statement = connection.prepareStatement(sql);
        statement.setString(1, user.getUsername());
        var result = statement.executeQuery();
        if (result.next()) {
            return new User(
                    result.getInt("user_id"),
                    result.getString("username"),
                    result.getString("encoded_password"),
                    UserRole.valueOf(result.getString("role"))
            );
        }
        return null;
    }

    @Override
    public User getUser(String username) throws SQLException, WrongUsernameException, WrongPasswordException {
        String sql = "SELECT * FROM monitoring_service_schema.user WHERE username = ?";
        var statement = connection.prepareStatement(sql);

        statement.setString(1, username);
        var result = statement.executeQuery();

        if (result.next()) {
            return new User(
                    result.getInt("user_id"),
                    result.getString("username"),
                    result.getString("encoded_password"),
                    UserRole.valueOf(result.getString("role"))
            );
        }
        return null;
    }

    @Override
    public boolean userExists(String username) throws SQLException {
        String sql = "SELECT * FROM monitoring_service_schema.user WHERE username = ?";
        var statement = connection.prepareStatement(sql);

        statement.setString(1, username);
        var result = statement.executeQuery();

        return result.next();
    }
}
