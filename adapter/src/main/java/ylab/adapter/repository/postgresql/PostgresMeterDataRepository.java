package ylab.adapter.repository.postgresql;

import application.port.repository.MeterDataRepository;
import model.meterdata.MeterData;
import model.meterdata.MeterType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PostgresMeterDataRepository implements MeterDataRepository {
    Connection connection;

    public PostgresMeterDataRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public MeterData postMeterData(MeterData meterData, String username) throws SQLException {
        var sql = "INSERT INTO monitoring_service_schema.meter_data (meter_type_id, assigned_to) VALUES (?, ?)";
        var preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setInt(1, meterData.getMeterType().getMeterTypeId());
        preparedStatement.setString(2, username);
        preparedStatement.executeUpdate();

        sql = "SELECT meter_data_id FROM monitoring_service_schema.meter_data WHERE assigned_to = ? AND meter_type_id = ?";
        preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, username);
        preparedStatement.setInt(2, meterData.getMeterType().getMeterTypeId());
        var result = preparedStatement.executeQuery();
        result.next();
        meterData.setMeterDataId(result.getInt("meter_data_id"));

        return meterData;
    }

    @Override
    public List<MeterData> getMeterData() throws SQLException {
        var statement = connection.createStatement();
        var result = statement.executeQuery("SELECT * FROM monitoring_service_schema.meter_data");

        List<MeterData> meterData = new ArrayList<>();
        while (result.next()) {
            var meterDataId = result.getInt("meter_data_id");

            var meterTypeId = result.getInt("meter_type_id");
            var sql = "SELECT * FROM monitoring_service_schema.meter_type WHERE meter_type_id = ?";
            var preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, meterTypeId);
            var meterTypeResult = preparedStatement.executeQuery();
            meterTypeResult.next();

            var meterType = new MeterType(
                    meterTypeResult.getInt("meter_type_id"),
                    meterTypeResult.getString("meter_type_name")
            );

            var meterDataItem = new MeterData(meterDataId, meterType);
            meterData.add(meterDataItem);
        }
        return meterData;
    }
}
