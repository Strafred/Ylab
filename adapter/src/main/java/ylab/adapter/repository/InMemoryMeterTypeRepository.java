package ylab.adapter.repository;

import application.port.repository.MeterTypeRepository;
import model.meterdata.MeterType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Реализация репозитория для работы с типами счетчиков в памяти
 */
public class InMemoryMeterTypeRepository implements MeterTypeRepository {
    /**
     * Список типов счетчиков
     */
    private final List<MeterType> meterTypes = new ArrayList<>();

    public InMemoryMeterTypeRepository() {
        meterTypes.add(new MeterType("Cold water"));
        meterTypes.add(new MeterType("Hot water"));
        meterTypes.add(new MeterType("Gas"));
    }

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

    @Override
    public void addMeterType(MeterType meterType) {
        meterTypes.add(meterType);
    }
}
