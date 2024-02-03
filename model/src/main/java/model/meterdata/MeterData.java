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
    private int meterDataId;
    /**
     * Тип счетчика
     */
    private final MeterType meterType;

    /**
     * Конструктор
     * @param meterType тип счетчика
     */
    public MeterData(MeterType meterType) {
        this.meterDataId = randomUUID().hashCode();
        this.meterType = meterType;
    }

    public MeterData(int meterDataId, MeterType meterType) {
        this.meterDataId = meterDataId;
        this.meterType = meterType;
    }

    /**
     * Получить тип счетчика
     * @return тип счетчика
     */
    public MeterType getMeterType() {
        return meterType;
    }

    public int getMeterDataId() {
        return meterDataId;
    }

    public void setMeterDataId(int meterId) {
        this.meterDataId = meterId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterData meterData = (MeterData) o;
        return Objects.equals(meterDataId, meterData.meterDataId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meterDataId);
    }
}
