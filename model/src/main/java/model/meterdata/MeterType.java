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
    private final int meterTypeId;

    /**
     * Название типа счетчика
     */
    private final String meterTypeName;

    /**
     * Конструктор
     * @param meterTypeName название типа счетчика
     */
    public MeterType(String meterTypeName) {
        this.meterTypeId = randomUUID().hashCode();
        this.meterTypeName = meterTypeName;
    }

    /**
     * Конструктор
     * @param meterTypeId идентификатор типа счетчика
     * @param meterTypeName название типа счетчика
     */
    public MeterType(int meterTypeId, String meterTypeName) {
        this.meterTypeId = meterTypeId;
        this.meterTypeName = meterTypeName;
    }

    /**
     * Получить идентификатор типа счетчика
     * @return идентификатор типа счетчика
     */
    public int getMeterTypeId() {
        return meterTypeId;
    }

    /**
     * Получить название типа счетчика
     * @return название типа счетчика
     */
    public String getMeterTypeName() {
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
