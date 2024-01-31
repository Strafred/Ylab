package application.port.in.dto;

import model.meterdata.MeterType;
import model.meterdata.ReadingData;
import model.meterdata.ReadingDate;

import java.util.Objects;

/**
 * DTO для передачи показаний счетчиков
 */
public class MeterReadingDTO {
    /**
     * Тип счетчика
     */
    MeterType meterType;
    /**
     * Дата показания
     */
    ReadingDate readingDate;
    /**
     * Показание счетчика
     */
    ReadingData readingData;

    public MeterReadingDTO(MeterType meterType, ReadingDate readingDate, ReadingData readingData) {
        this.meterType = meterType;
        this.readingDate = readingDate;
        this.readingData = readingData;
    }

    @Override
    public String toString() {
        return "MeterReadingDTO{" +
                "meterType: " + meterType +
                ", readingDate: " + readingDate +
                ", readingData: " + readingData +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MeterReadingDTO that = (MeterReadingDTO) o;
        return Objects.equals(meterType, that.meterType) && Objects.equals(readingDate, that.readingDate) && Objects.equals(readingData, that.readingData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meterType, readingDate, readingData);
    }
}
