package application.port.in.dto;

import model.meterdata.MeterType;
import model.meterdata.ReadingData;
import model.meterdata.ReadingDate;

import java.util.Map;
import java.util.Objects;

/**
 * DTO для передачи данных о счетчике
 */
public class MeterDataDTO {
    /**
     * Тип счетчика
     */
    MeterType meterType;
    /**
     * Показания счетчика
     */
    Map<ReadingDate, ReadingData> readings;

    public MeterDataDTO(MeterType meterType, Map<ReadingDate, ReadingData> readings) {
        this.meterType = meterType;
        this.readings = readings;
    }


    @Override
    public String toString() {
        return "MeterDataDTO{" +
                "meterType: " + meterType +
                ", readings: " + readings +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterDataDTO that = (MeterDataDTO) o;
        return Objects.equals(meterType, that.meterType) && Objects.equals(readings, that.readings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meterType, readings);
    }
}
