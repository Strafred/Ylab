package application;

import application.port.out.MeterTypeRepository;
import application.service.meterdata.GetAccessibleMeterTypesService;
import model.meterdata.MeterType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class GetAccessibleMeterTypesServiceTest {
    @InjectMocks
    GetAccessibleMeterTypesService getAccessibleMeterTypesService;

    @Mock
    MeterTypeRepository meterTypeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAccessibleMeterTypes_shouldWork() {
        Mockito.when(meterTypeRepository.getMeterTypes())
                .thenReturn(new ArrayList<>(List.of(new MeterType("TestMeterType"))));

        List<MeterType> accessibleMeterTypes = getAccessibleMeterTypesService.getAccessibleMeterTypes();

        assertThat(accessibleMeterTypes.size()).isEqualTo(1);
        assertThat(accessibleMeterTypes.get(0).getName()).isEqualTo("TestMeterType");
    }
}
