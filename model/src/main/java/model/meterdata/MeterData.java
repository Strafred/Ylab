package model.meterdata;

import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;

import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.UUID.randomUUID;

/**
 * Класс для хранения данных о счетчике
 */
public class MeterData {
    /**
     * Идентификатор счетчика
     */
    private final String meterId;
    /**
     * Тип счетчика
     */
    private final MeterType meterType;
    /**
     * Данные о показаниях счетчика
     */
    private final Map<ReadingDate, ReadingData> readings;

    /**
     * Конструктор
     * @param meterType тип счетчика
     */
    public MeterData(MeterType meterType) {
        this.meterId = randomUUID().toString();
        this.meterType = meterType;
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
     * Получить тип счетчика
     * @return тип счетчика
     */
    public MeterType getMeterType() {
        return meterType;
    }

    /**
     * Получить все показания счетчика
     * @return все показания счетчика
     */
    public Map<ReadingDate, ReadingData> getAllReadings() {
        return readings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterData meterData = (MeterData) o;
        return Objects.equals(meterId, meterData.meterId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meterId);
    }
}
