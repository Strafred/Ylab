package ylab.adapter.repository.postgresql;

import application.port.repository.MeterDataReadingRepository;
import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;
import model.meterdata.MeterData;
import model.meterdata.MeterDataReadings;
import model.meterdata.ReadingData;
import model.meterdata.ReadingDate;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.Map;

public class PostgresMeterDataReadingRepository implements MeterDataReadingRepository {
    Connection connection;

    public PostgresMeterDataReadingRepository(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void putMeterDataReadings(MeterDataReadings meterDataReadings) throws SQLException {
        Map<ReadingDate, ReadingData> readings = meterDataReadings.getAllReadings();

        for (Map.Entry<ReadingDate, ReadingData> entry : readings.entrySet()) {
            var statement = connection.prepareStatement("INSERT INTO monitoring_service_schema.meter_data_readings (meter_data_id, reading_date_year, reading_date_month, reading_data) VALUES (?, ?, ?, ?)");

            statement.setInt(1, meterDataReadings.getMeterData().getMeterDataId());

            statement.setInt(2, entry.getKey().getYear());
            statement.setInt(3, entry.getKey().getMonth());
            statement.setDouble(4, entry.getValue().getValue());
            statement.executeUpdate();
        }
    }

    @Override
    public MeterDataReadings getMeterDataReadingsByMeterData(MeterData meterData) throws SQLException, DuplicateReadingException {
        var statement = connection.prepareStatement("SELECT * FROM monitoring_service_schema.meter_data_readings WHERE meter_data_id = ?");
        statement.setInt(1, meterData.getMeterDataId());
        var result = statement.executeQuery();

        MeterDataReadings meterDataReadings = new MeterDataReadings(meterData);
        while (result.next()) {
            var readingDate = new ReadingDate(result.getInt("reading_date_year"), result.getInt("reading_date_month"));
            var readingData = new ReadingData(result.getInt("reading_data"));
            meterDataReadings.addReadingByDate(readingDate, readingData);
        }
        return meterDataReadings;
    }

    @Override
    public void putNewReadingByMeterData(MeterData meterData, ReadingData readingData) throws SQLException, DuplicateReadingException, WrongReadingValueException {
        var yearMonth = new ReadingDate(YearMonth.now().getYear(), YearMonth.now().getMonthValue());
        var statement = connection.prepareStatement("INSERT INTO monitoring_service_schema.meter_data_readings (meter_data_id, reading_date_year, reading_date_month, reading_data) VALUES (?, ?, ?, ?)");

        statement.setInt(1, meterData.getMeterDataId());
        statement.setInt(2, yearMonth.getYear());
        statement.setInt(3, yearMonth.getMonth());
        statement.setInt(4, readingData.getValue());

        statement.executeUpdate();
    }
}
