package application.service.meterdata;

import application.port.in.meterdata.GetAccessibleMeterTypesUseCase;
import application.port.out.MeterTypeRepository;
import model.meterdata.MeterType;

import java.util.List;

/**
 * Сервис для получения списка доступных типов счетчиков
 */
public class GetAccessibleMeterTypesService implements GetAccessibleMeterTypesUseCase {
    MeterTypeRepository meterTypeRepository;

    public GetAccessibleMeterTypesService(MeterTypeRepository meterTypeRepository) {
        this.meterTypeRepository = meterTypeRepository;
    }

    /**
     * Получить список доступных типов счетчиков
     * @return список типов счетчиков
     */
    @Override
    public List<MeterType> getAccessibleMeterTypes() {
        return meterTypeRepository.getMeterTypes();
    }
}
