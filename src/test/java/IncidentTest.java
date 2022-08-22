import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class IncidentTest {
    static File incidentsJson = new File("resulting_incidents.json");

    @Test
    void checkThatIncidentHasToString() {
        IncidentFromJson jsonIncident = new IncidentFileReader(incidentsJson).getList().get(0);
        Incident incident = new Incident(jsonIncident);
        System.out.println(incident);
        assertTrue(!incident.toString().contains("@"), "Incident class should have toString method");
    }
}
