package model.meterdata;

import java.util.Objects;

import static java.util.UUID.randomUUID;

/**
 * Класс для хранения данных о типе счетчика
 */
public class MeterType {
    /**
     * Идентификатор типа счетчика
     */
    private String meterTypeId;

    /**
     * Название типа счетчика
     */
    private String name;

    /**
     * Конструктор
     * @param name название типа счетчика
     */
    public MeterType(String name) {
        this.meterTypeId = randomUUID().toString();
        this.name = name;
    }

    /**
     * Получить название типа счетчика
     * @return название типа счетчика
     */
    public String getName() {
        return name;
    }

    public String toString() {
        return this.name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterType meterType = (MeterType) o;
        return Objects.equals(name, meterType.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
