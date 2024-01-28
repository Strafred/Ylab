package model.meterdata;

import java.util.Objects;

/**
 * Класс для хранения данных о показании счетчика
 */
public class ReadingData {
    /**
     * Показание счетчика
     */
    int value;

    /**
     * Конструктор
     * @param value показание счетчика
     */
    public ReadingData(int value) {
        this.value = value;
    }

    /**
     * Получить показание счетчика
     * @return показание счетчика
     */
    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "value=" + value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReadingData that = (ReadingData) o;
        return value == that.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
