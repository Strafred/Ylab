package adapter;

import model.meterdata.MeterType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ylab.adapter.repository.inmemory.InMemoryMeterTypeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class InMemoryMeterTypeRepositoryTest {
    @DisplayName("When getMeterTypes, then return meter types")
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

    @DisplayName("When findMeterType, then return meter type")
    @Test
    void findMeterType_ShouldWork() {
        InMemoryMeterTypeRepository inMemoryMeterTypeRepository = new InMemoryMeterTypeRepository();

        assertThat(inMemoryMeterTypeRepository.findMeterType(new MeterType("Cold water"))).isPresent();
        assertThat(inMemoryMeterTypeRepository.findMeterType(new MeterType("Hot water"))).isPresent();
        assertThat(inMemoryMeterTypeRepository.findMeterType(new MeterType("Gas"))).isPresent();
        assertThat(inMemoryMeterTypeRepository.findMeterType(new MeterType("Electricity"))).isEmpty();
    }
}
