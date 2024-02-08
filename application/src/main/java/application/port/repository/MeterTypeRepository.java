package application.port.repository;

import model.meterdata.MeterType;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для получения типов счетчиков
 */
public interface MeterTypeRepository {
    /**
     * Получить список всех типов счетчиков
     * @return список типов счетчиков
     */
    List<MeterType> getMeterTypes() throws SQLException;
    /**
     * Получить тип счетчика по экземпляру типа счетчика, если такой существует в репозитории
     * @param meterType экземпляр типа счетчика
     * @return тип счетчика
     */
    Optional<MeterType> findMeterType(MeterType meterType) throws SQLException;

    /**
     * Добавить тип счетчика в репозиторий
     * @param meterType тип счетчика
     */
    void addMeterType(MeterType meterType) throws SQLException;
}
