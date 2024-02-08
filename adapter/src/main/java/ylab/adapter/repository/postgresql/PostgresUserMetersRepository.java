package ylab.adapter.repository.postgresql;

import application.port.repository.UserMetersRepository;
import model.exceptions.WrongPasswordException;
import model.exceptions.WrongUsernameException;
import model.meterdata.MeterData;
import model.meterdata.MeterType;
import model.user.User;
import model.user.UserRole;
import model.usermeter.UserMeters;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresUserMetersRepository implements UserMetersRepository {
    Connection connection;

    public PostgresUserMetersRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void saveUserMeters(UserMeters userMeters) throws SQLException {
        var user = userMeters.getUser();
        var userId = user.getUserId();

        var meters = userMeters.getMeters();
        for (MeterData meter : meters) {
            var meterDataId = meter.getMeterDataId();
            var sql = "INSERT INTO monitoring_service_schema.user_meters (user_id, meter_data_id) VALUES (?, ?)";
            var statement = connection.prepareStatement(sql);
            statement.setInt(1, userId);
            statement.setInt(2, meterDataId);
            statement.executeUpdate();
        }
    }

    @Override
    public UserMeters getUserMetersByUsername(String username) throws SQLException, WrongUsernameException, WrongPasswordException {
        var sql = "SELECT * FROM monitoring_service_schema.user WHERE username = ?";
        var statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        var result = statement.executeQuery();

        if (result.next()) {
            var userId = result.getInt("user_id");
            var user = new User(
                    userId,
                    result.getString("username"),
                    result.getString("encoded_password"),
                    UserRole.valueOf(result.getString("role"))
            );

            var sql2 = "SELECT * FROM monitoring_service_schema.user_meters WHERE user_id = ?";
            var statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, userId);
            var result2 = statement2.executeQuery();

            List<MeterData> meterDataList = new ArrayList<>();
            while (result2.next()) {
                var userMeterId = result2.getInt("user_meters_id");
                var meterDataId = result2.getInt("meter_data_id");

                var sql3 = "SELECT * FROM monitoring_service_schema.meter_data WHERE meter_data_id = ?";
                var statement3 = connection.prepareStatement(sql3);
                statement3.setInt(1, meterDataId);
                var result3 = statement3.executeQuery();

                MeterData meterData = null;
                if (result3.next()) {
                    var meterTypeId = result3.getInt("meter_type_id");
                    var sql4 = "SELECT * FROM monitoring_service_schema.meter_type WHERE meter_type_id = ?";
                    var statement4 = connection.prepareStatement(sql4);
                    statement4.setInt(1, meterTypeId);
                    var result4 = statement4.executeQuery();

                    MeterType meterType = null;
                    if (result4.next()) {
                        meterType = new MeterType(
                                result4.getInt("meter_type_id"),
                                result4.getString("meter_type_name")
                        );
                    }
                    meterData = new MeterData(meterDataId, meterType);
                }

                meterDataList.add(meterData);
            }

            return new UserMeters(user, meterDataList);
        }
        return null;
    }

    @Override
    public void putUserMeterByUsername(String username, MeterData meterData) throws SQLException {
        var sql = "SELECT * FROM monitoring_service_schema.user WHERE username = ?";
        var statement = connection.prepareStatement(sql);
        statement.setString(1, username);
        var result = statement.executeQuery();

        if (result.next()) {
            var userId = result.getInt("user_id");
            var meterDataId = meterData.getMeterDataId();

            var sql2 = "INSERT INTO monitoring_service_schema.user_meters (user_id, meter_data_id) VALUES (?, ?)";
            var statement2 = connection.prepareStatement(sql2);
            statement2.setInt(1, userId);
            statement2.setInt(2, meterDataId);
            statement2.executeUpdate();
        }
    }
}
