package application.port.repository;

import model.meterdata.MeterData;

import java.util.List;

/**
 * Интерфейс для репозитория данных о счетчиках
 */
public interface MeterDataRepository {
    /**
     * Сохранить данные о счетчике
     * @param meterData данные о счетчике
     */
    void putMeterData(MeterData meterData);

    /**
     * Получить данные о счетчиках
     * @return список данных о счетчиках
     */
    List<MeterData> getMeterData();
}
