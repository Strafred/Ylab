package ylab.adapter.repository.inmemory;

import application.port.repository.MeterDataReadingRepository;
import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;
import model.meterdata.MeterData;
import model.meterdata.MeterDataReadings;
import model.meterdata.ReadingData;

import java.util.ArrayList;
import java.util.List;

public class InMemoryMeterDataReadingRepository implements MeterDataReadingRepository {
    private final List<MeterDataReadings> meterDataReadings = new ArrayList<>();

    @Override
    public void putMeterDataReadings(MeterDataReadings newMeterDataReadings) {
        meterDataReadings.add(newMeterDataReadings);
    }

    @Override
    public MeterDataReadings getMeterDataReadingsByMeterData(MeterData meterData) {
        return meterDataReadings.stream()
                .filter(meterDataReadings -> meterDataReadings.getMeterData().equals(meterData))
                .findFirst()
                .orElse(null);
    }

    @Override
    public void putNewReadingByMeterData(MeterData meterData, ReadingData readingData) throws DuplicateReadingException, WrongReadingValueException {
        MeterDataReadings meterDataReadings = getMeterDataReadingsByMeterData(meterData);
        if (meterDataReadings != null) {
            meterDataReadings.addReading(readingData);
        } else {
            meterDataReadings = new MeterDataReadings(meterData);
            meterDataReadings.addReading(readingData);
            putMeterDataReadings(meterDataReadings);
        }
    }
}
