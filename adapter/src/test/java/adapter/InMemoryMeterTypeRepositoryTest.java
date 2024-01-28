package adapter;

import model.meterdata.MeterType;
import org.junit.jupiter.api.Test;
import ylab.adapter.out.InMemoryMeterTypeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryMeterTypeRepositoryTest {
    @Test
    void getMeterTypes_ShouldWork() {
        InMemoryMeterTypeRepository inMemoryMeterTypeRepository = new InMemoryMeterTypeRepository();

        assertThat(inMemoryMeterTypeRepository.getMeterTypes()).containsExactlyElementsOf(
                List.of(
                        new MeterType("Cold water"),
                        new MeterType("Hot water"),
                        new MeterType("Gas")
                )
        );
    }

    @Test
    void findMeterType_ShouldWork() {
        InMemoryMeterTypeRepository inMemoryMeterTypeRepository = new InMemoryMeterTypeRepository();

        assertThat(inMemoryMeterTypeRepository.findMeterType(new MeterType("Cold water"))).isPresent();
        assertThat(inMemoryMeterTypeRepository.findMeterType(new MeterType("Hot water"))).isPresent();
        assertThat(inMemoryMeterTypeRepository.findMeterType(new MeterType("Gas"))).isPresent();
        assertThat(inMemoryMeterTypeRepository.findMeterType(new MeterType("Electricity"))).isEmpty();
    }
}
