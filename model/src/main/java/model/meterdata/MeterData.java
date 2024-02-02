package model.meterdata;

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
     * Конструктор
     * @param meterType тип счетчика
     */
    public MeterData(MeterType meterType) {
        this.meterId = randomUUID().toString();
        this.meterType = meterType;
    }

    /**
     * Получить тип счетчика
     * @return тип счетчика
     */
    public MeterType getMeterType() {
        return meterType;
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
