package bootstrap;

import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class BootstrapUtils {
    public static Connection initPostgresConnection(String jdbcUrl, String username, String password) {
        try {
            Class.forName("org.postgresql.Driver");
            var connection = DriverManager.getConnection(jdbcUrl, username, password);
            Database database =
                    DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            Liquibase liquibase = new Liquibase("db.changelog/changelog.xml", new ClassLoaderResourceAccessor(), database);
            liquibase.update();
            return connection;
        } catch (SQLException | LiquibaseException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
