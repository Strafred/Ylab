package adapter;

import model.meterdata.MeterData;
import model.meterdata.MeterType;
import org.junit.jupiter.api.Test;
import ylab.adapter.out.InMemoryMeterDataRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InMemoryMeterDataRepositoryTest {
    @Test
    void givenMeterData_putMeterData_shouldPutMeterData() {
        MeterData meterData = new MeterData(new MeterType("123"));
        InMemoryMeterDataRepository meterDataRepository = new InMemoryMeterDataRepository();

        meterDataRepository.putMeterData(meterData);

        assertThat(meterDataRepository.getMeterDataList().size()).isEqualTo(1);
        assertThat(meterDataRepository.getMeterDataList().get(0)).isEqualTo(meterData);
    }
}
