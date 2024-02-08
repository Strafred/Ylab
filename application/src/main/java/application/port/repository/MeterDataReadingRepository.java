package application.port.repository;

import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;
import model.meterdata.MeterData;
import model.meterdata.MeterDataReadings;
import model.meterdata.ReadingData;

import java.sql.SQLException;

public interface MeterDataReadingRepository {
    void putMeterDataReadings(MeterDataReadings meterDataReadings) throws SQLException;
    MeterDataReadings getMeterDataReadingsByMeterData(MeterData meterData) throws SQLException, DuplicateReadingException;
    void putNewReadingByMeterData(MeterData meterData, ReadingData readingData) throws DuplicateReadingException, WrongReadingValueException, SQLException;
}
