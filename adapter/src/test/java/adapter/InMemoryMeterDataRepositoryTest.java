package adapter;

import model.meterdata.MeterData;
import model.meterdata.MeterType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ylab.adapter.repository.InMemoryMeterDataRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InMemoryMeterDataRepositoryTest {
    @DisplayName("Given meter data, when putMeterData, then put meter data")
    @Test
    void givenMeterData_putMeterData_shouldPutMeterData() {
        MeterData meterData = new MeterData(new MeterType("123"));
        InMemoryMeterDataRepository meterDataRepository = new InMemoryMeterDataRepository();

        meterDataRepository.putMeterData(meterData);

        assertThat(meterDataRepository.getMeterData().size()).isEqualTo(1);
        assertThat(meterDataRepository.getMeterData().get(0)).isEqualTo(meterData);
    }
}
