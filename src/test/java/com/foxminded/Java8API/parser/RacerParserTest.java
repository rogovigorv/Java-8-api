package com.foxminded.Java8API.parser;

import com.foxminded.Java8API.domain.RadeDataDto;
import com.foxminded.Java8API.domain.Racer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RacerParserTest {
    private static final List<String> FROM_ABBREVIATION_TXT_FILE = new ArrayList<>();
    private static final List<String> FROM_START_LOG_FILE = new ArrayList<>();
    private static final List<String> FROM_END_LOG_FILE= new ArrayList<>();
    private static final Calendar SEBASTIAN_VETTEL_TIME = new GregorianCalendar();
    private static final Calendar DANIEL_RICCIARDO_TIME = new GregorianCalendar();
    private static final String EMPTY_STRING = "";
    private static final RadeDataDto DTO_IS_NULL = null;
    private static final List<String> EMPTY_LIST = Arrays.asList(EMPTY_STRING, EMPTY_STRING);

    @BeforeEach
    private void setup() {
        addToList();
        setTime();
    }

    @Mock
    private RadeDataDto radeDataDto;

    private final Parser racerParser = new RacerParser();

    @Test
    void makeParseDtoShouldReturnRacer() {
        when(radeDataDto.getRacersNames()).thenReturn(FROM_ABBREVIATION_TXT_FILE);
        when(radeDataDto.getStartRace()).thenReturn(FROM_START_LOG_FILE);
        when(radeDataDto.getEndRace()).thenReturn(FROM_END_LOG_FILE);

        final List<Racer> expected = new ArrayList<>();
        expected.add(new Racer("Sebastian Vettel",
                "FERRARI", SEBASTIAN_VETTEL_TIME));
        expected.add(new Racer("Daniel Ricciardo",
                "RED BULL RACING TAG HEUER", DANIEL_RICCIARDO_TIME));

        final List<Racer> actual = racerParser.parse(radeDataDto);

        assertEquals(expected.toString(), actual.toString());
    }

    @Test
    void makeParseDtoShouldThrowIllegalArgumentExceptionWhenNullPassed() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                racerParser.parse(DTO_IS_NULL));
        assertThat("DTO object is null", equalTo(exception.getMessage()));
    }

    @Test
    void makeParseDtoShouldThrowIllegalArgumentExceptionWhenAnyListIsNull(){
        when(radeDataDto.getRacersNames()).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                racerParser.parse(radeDataDto));
        assertThat("List from DTO object is null", equalTo(exception.getMessage()));
    }

    @Test
    void makeParseDtoShouldThrowIllegalArgumentExceptionWhenAnyListIsEmpty(){
        when(radeDataDto.getRacersNames()).thenReturn(EMPTY_LIST);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                racerParser.parse(radeDataDto));
        assertThat("List from DTO object is empty", equalTo(exception.getMessage()));
    }

    private void addToList() {
        FROM_ABBREVIATION_TXT_FILE.add("SVF_Sebastian Vettel_FERRARI");
        FROM_START_LOG_FILE.add("SVF2018-05-24_12:02:58.917");
        FROM_END_LOG_FILE.add("SVF2018-05-24_12:04:03.332");
        FROM_ABBREVIATION_TXT_FILE.add("DRR_Daniel Ricciardo_RED BULL RACING TAG HEUER");
        FROM_START_LOG_FILE.add("DRR2018-05-24_12:14:12.054");
        FROM_END_LOG_FILE.add("DRR2018-05-24_12:15:24.067");
    }

    private void setTime() {
        SEBASTIAN_VETTEL_TIME.setTimeInMillis(64415);
        DANIEL_RICCIARDO_TIME.setTimeInMillis(72013);
    }
}
