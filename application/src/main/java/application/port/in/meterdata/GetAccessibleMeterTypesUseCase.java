package application.port.in.meterdata;

import model.meterdata.MeterType;

import java.util.List;

/**
 * Сценарий: получить список доступных типов счетчиков
 */
public interface GetAccessibleMeterTypesUseCase {
    /**
     * Получить список доступных типов счетчиков
     * @return список доступных типов счетчиков
     */
    List<MeterType> getAccessibleMeterTypes();
}
