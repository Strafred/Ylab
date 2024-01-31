package ylab.adapter.out;

import application.port.out.MeterTypeRepository;
import model.meterdata.MeterType;

import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для работы с типами счетчиков в памяти
 */
public class InMemoryMeterTypeRepository implements MeterTypeRepository {
    /**
     * Список типов счетчиков
     */
    List<MeterType> meterTypes = List.of(
            new MeterType("Cold water"),
            new MeterType("Hot water"),
            new MeterType("Gas")
    );

    @Override
    public List<MeterType> getMeterTypes() {
        return meterTypes;
    }

    @Override
    public Optional<MeterType> findMeterType(MeterType meterType) {
        return meterTypes.stream()
                .filter(meterType::equals)
                .findFirst();
    }
}
