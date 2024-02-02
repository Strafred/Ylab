package ylab.adapter.repository.inmemory;

import application.port.repository.MeterDataRepository;
import model.meterdata.MeterData;

import java.util.ArrayList;
import java.util.List;

/**
 * Реализация репозитория для хранения данных о счетчиках в памяти
 */
public class InMemoryMeterDataRepository implements MeterDataRepository {
    /**
     * Список данных о счетчиках
     */
    private final List<MeterData> meterDataList = new ArrayList<>();

    @Override
    public void putMeterData(MeterData meterData) {
        int index = meterDataList.indexOf(meterData);
        if (index != -1) {
            meterDataList.set(index, meterData);
        } else {
            meterDataList.add(meterData);
        }
    }

    @Override
    public List<MeterData> getMeterData() {
        return meterDataList;
    }
}
