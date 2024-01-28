package ylab.adapter.out;

import application.port.out.MeterDataRepository;
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
    List<MeterData> meterDataList = new ArrayList<>();

    @Override
    public void putMeterData(MeterData meterData) {
        int index = meterDataList.indexOf(meterData);
        if (index != -1) {
            meterDataList.set(index, meterData);
        } else {
            meterDataList.add(meterData);
        }
    }

    public List<MeterData> getMeterDataList() {
        return meterDataList;
    }
}
