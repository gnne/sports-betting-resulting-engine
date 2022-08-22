import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UtilitiesTest {

    @Test
    void convertJsonFieldToEnumValue() {
        String[] strings = {"PeriodSecondsElapsed", "TeamType", "PeriodType", "IncidentType"};
        String[] converted = {"PERIOD_SECONDS_ELAPSED", "TEAM_TYPE", "PERIOD_TYPE", "INCIDENT_TYPE"};
        for(int i = 0; i < strings.length; i++) {
            assertTrue(Utilities.mapJsonFieldNameToEnumValue(strings[i]).equals(converted[i])
                        , "String not converted properly to enum value");
        }
    }
}
