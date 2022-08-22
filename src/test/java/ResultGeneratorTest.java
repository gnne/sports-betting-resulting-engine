import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ResultGeneratorTest {
    static File incidentsFile = new File("resulting_incidents.json");
    static Fixture soccerGame = new SoccerGame(incidentsFile);

    @BeforeAll
    static void beforeAll() {
        IncidentProcessor incidentProcessor = new IncidentProcessor(soccerGame);
        List<Incident> realTimeIncidents = soccerGame.gameIncidents;
        soccerGame.event = Status.IN_PLAY;
        for(Incident incident : realTimeIncidents) {
            incidentProcessor.updateTrackers(incident);
        }
        soccerGame.event = Status.ENDED;
    }

    @Test
    void generateResults() {
    }
}