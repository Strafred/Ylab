package application.port.repository;

import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;
import model.meterdata.MeterData;
import model.meterdata.MeterDataReadings;
import model.meterdata.ReadingData;

public interface MeterDataReadingRepository {
    void putMeterDataReadings(MeterDataReadings meterDataReadings);
    MeterDataReadings getMeterDataReadingsByMeterData(MeterData meterData);
    void putNewReadingByMeterData(MeterData meterData, ReadingData readingData) throws DuplicateReadingException, WrongReadingValueException;
}
