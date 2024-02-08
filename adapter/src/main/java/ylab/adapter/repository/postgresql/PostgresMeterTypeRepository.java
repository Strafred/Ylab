package ylab.adapter.repository.postgresql;

import application.port.repository.MeterTypeRepository;
import model.meterdata.MeterType;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PostgresMeterTypeRepository implements MeterTypeRepository {
    Connection connection;

    public PostgresMeterTypeRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<MeterType> getMeterTypes() throws SQLException {
        var statement = connection.createStatement();
        var result = statement.executeQuery("SELECT * FROM monitoring_service_schema.meter_type");

        List<MeterType> meterTypes = new ArrayList<>();
        while (result.next()) {
            var meterType = new MeterType(
                    result.getInt("meter_type_id"),
                    result.getString("meter_type_name")
            );
            meterTypes.add(meterType);
        }
        return meterTypes;
    }

    @Override
    public Optional<MeterType> findMeterType(MeterType meterType) throws SQLException {
        var statement = connection.prepareStatement("SELECT * FROM monitoring_service_schema.meter_type WHERE meter_type_name = ?");
        statement.setString(1, meterType.getMeterTypeName());
        var result = statement.executeQuery();

        if (result.next()) {
            return Optional.of(new MeterType(
                    result.getInt("meter_type_id"),
                    result.getString("meter_type_name")
            ));
        }
        return Optional.empty();
    }

    @Override
    public void addMeterType(MeterType meterType) throws SQLException {
        var statement = connection.prepareStatement("INSERT INTO monitoring_service_schema.meter_type (meter_type_name) VALUES (?)");
        statement.setString(1, meterType.getMeterTypeName());
        statement.executeUpdate();
    }
}
