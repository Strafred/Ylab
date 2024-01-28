package application.port.out;

import model.meterdata.MeterData;

/**
 * Интерфейс для репозитория данных о счетчиках
 */
public interface MeterDataRepository {
    /**
     * Сохранить данные о счетчике
     * @param meterData данные о счетчике
     */
    void putMeterData(MeterData meterData);
}
