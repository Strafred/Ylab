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
    private final String meterTypeId;

    /**
     * Название типа счетчика
     */
    private final String meterTypeName;

    /**
     * Конструктор
     * @param meterTypeName название типа счетчика
     */
    public MeterType(String meterTypeName) {
        this.meterTypeId = randomUUID().toString();
        this.meterTypeName = meterTypeName;
    }

    /**
     * Получить название типа счетчика
     * @return название типа счетчика
     */
    public String getName() {
        return meterTypeName;
    }

    public String toString() {
        return this.meterTypeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterType meterType = (MeterType) o;
        return Objects.equals(meterTypeName, meterType.meterTypeName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meterTypeName);
    }
}
