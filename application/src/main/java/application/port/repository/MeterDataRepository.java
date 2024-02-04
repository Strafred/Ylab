package application.port.repository;

import model.meterdata.MeterData;

import java.sql.SQLException;
import java.util.List;

/**
 * Интерфейс для репозитория данных о счетчиках
 */
public interface MeterDataRepository {
    /**
     * Сохранить данные о счетчике
     *
     * @param meterData данные о счетчике
     * @param username
     * @return данные о счетчике
     */
    MeterData postMeterData(MeterData meterData, String username) throws SQLException;

    /**
     * Получить данные о счетчиках
     * @return список данных о счетчиках
     */
    List<MeterData> getMeterData() throws SQLException;
}
