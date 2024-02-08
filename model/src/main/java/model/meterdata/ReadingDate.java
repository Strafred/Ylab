package model.meterdata;

import java.util.Objects;

/**
 * Класс для хранения данных о дате снятия показания
 */
public class ReadingDate {
    /**
     * Год
     */
    private final int year;
    /**
     * Месяц
     */
    private final int month;

    /**
     * Конструктор
     * @param year год
     * @param month месяц
     */
    public ReadingDate(int year, int month) {
        this.year = year;
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ReadingDate otherDate = (ReadingDate) o;
        return year == otherDate.year && month == otherDate.month;
    }

    @Override
    public int hashCode() {
        return Objects.hash(year, month);
    }

    @Override
    public String toString() {
        return "year=" + year +
                ", month=" + month;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }
}
