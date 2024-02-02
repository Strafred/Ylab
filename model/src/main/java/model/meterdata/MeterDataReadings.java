package model.meterdata;

import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

public class MeterDataReadings {
    /**
     * Данные о счетчике
     */
    private final MeterData meterData;

    /**
     * Данные о показаниях счетчика
     */
    private final Map<ReadingDate, ReadingData> readings;

    public MeterDataReadings(MeterData meterData, Map<ReadingDate, ReadingData> readings) {
        this.meterData = meterData;
        this.readings = readings;
    }

    public MeterDataReadings(MeterData meterData) {
        this.meterData = meterData;
        this.readings = new HashMap<>();
    }

    /**
     * Добавить показание счетчика
     * @param readingData показание счетчика
     */
    public void addReading(ReadingData readingData) throws DuplicateReadingException, WrongReadingValueException {
        YearMonth currentYearMonth = YearMonth.now();

        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        int previousYear = previousYearMonth.getYear();
        int previousMonth = previousYearMonth.getMonthValue();
        var previousReadingDate = new ReadingDate(previousYear, previousMonth);

        if (getAllReadings().containsKey(previousReadingDate) && getAllReadings().get(previousReadingDate).getValue() > readingData.getValue()) {
            throw new WrongReadingValueException("Reading value is lower than previous reading value!");
        }

        int currentYear = currentYearMonth.getYear();
        int currentMonth = currentYearMonth.getMonthValue();
        var readingDate = new ReadingDate(currentYear, currentMonth);

        if (getAllReadings().putIfAbsent(readingDate, readingData) != null) {
            throw new DuplicateReadingException("Reading for this month already exists!");
        }
    }

    /**
     * Получить все показания счетчика
     * @return все показания счетчика
     */
    public Map<ReadingDate, ReadingData> getAllReadings() {
        return readings;
    }

    public MeterData getMeterData() {
        return meterData;
    }
}
