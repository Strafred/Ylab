package model.meterdata;

import model.exceptions.DuplicateReadingException;
import model.exceptions.WrongReadingValueException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.YearMonth;
import java.util.HashMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatCode;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

public class MeterDataTest {
    @Test
    void givenADuplicateReading_addReading_throwsException() {
        MeterType meterType = new MeterType("TestMeterType");
        MeterData meterData = new MeterData(meterType);
        MeterDataReadings meterDataReadings = new MeterDataReadings(meterData, new HashMap<>());

        assertThatCode(() -> meterDataReadings.addReading(new ReadingData(1)))
                .doesNotThrowAnyException();

        assertThatExceptionOfType(DuplicateReadingException.class)
                .isThrownBy(() -> meterDataReadings.addReading(new ReadingData(2)));
    }

    @Test
    void givenALowerReading_addReading_throwsException() {
        MeterData meterData = new MeterData(new MeterType("TestMeterType"));

        YearMonth currentYearMonth = YearMonth.now();
        YearMonth previousYearMonth = currentYearMonth.minusMonths(1);
        int previousYear = previousYearMonth.getYear();
        int previousMonth = previousYearMonth.getMonthValue();
        var previousReadingDate = new ReadingDate(previousYear, previousMonth);

        MeterDataReadings meterDataReadings = new MeterDataReadings(meterData, new HashMap<>() {{
            put(previousReadingDate, new ReadingData(2));
        }});

        assertThatExceptionOfType(WrongReadingValueException.class)
                .isThrownBy(() -> meterDataReadings.addReading(new ReadingData(1)));
    }

    @Test
    void givenAReading_addReading_doesNotThrowException() {
        MeterData meterData = new MeterData(new MeterType("TestMeterType"));
        var meterDataReadings = new MeterDataReadings(meterData, new HashMap<>());

        assertThatCode(() -> meterDataReadings.addReading(new ReadingData(1)))
                .doesNotThrowAnyException();
    }
}
