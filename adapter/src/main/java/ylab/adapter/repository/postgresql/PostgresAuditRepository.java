package ylab.adapter.repository.postgresql;

import application.port.repository.AuditRepository;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PostgresAuditRepository implements AuditRepository {
    Connection connection;

    public PostgresAuditRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveAudit(String username, String message) throws SQLException {
            String audit = username + ": " + message;
            String sql = "INSERT INTO monitoring_service_schema.audit (message) VALUES (?)";

            var statement = connection.prepareStatement(sql);
            statement.setString(1, audit);
            statement.executeUpdate();
    }

    @Override
    public List<String> getAudit() throws SQLException {
        String sql = "SELECT message FROM monitoring_service_schema.audit";

        Statement statement = connection.createStatement();
        ResultSet result = statement.executeQuery(sql);

        List<String> auditsList = new ArrayList<>();
        while (result.next()) {
            var audit = result.getString("message");
            auditsList.add(audit);
        }
        return auditsList;
    }
}
