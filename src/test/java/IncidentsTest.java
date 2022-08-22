import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncidentsTest {
    static List<IncidentFromJson> jsonObjects = new IncidentFileReader(new File("resulting_incidents.json")).getList();
    static List<Incident> list = Incidents.get(jsonObjects);

    @Test
    void getListOfIncidents() {
        for(Incident incident : list) {
            String refName = incident.getClass().getName() + "@";
            assertFalse(incident.toString().contains(refName), "Incident class should have a toString method");
            System.out.println(incident);
        }
    }

    @Test
    void incidentsGroupedByIncidentType() {
       Incidents.groupedByIncidentType(list).forEach((k, v) -> System.out.println(k + " : " +  v));
    }

    @Test
    void incidentsGroupedByPeriodType() {
        Incidents.groupedByPeriodType(list).forEach((k, v) -> System.out.println(k + " : " +  v));
    }


}
