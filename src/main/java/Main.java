import java.io.File;
import java.util.*;

public class Main {
    static File incidentsFile = new File("resulting_incidents.json");
    static Fixture soccerGame = new SoccerGame(incidentsFile);

    public static void main(String[] args)  {

        generateResults(soccerGame.gameIncidents);
    }

    static void generateResults(List<Incident> list) {
        IncidentProcessor incidentProcessor = new IncidentProcessor(soccerGame);
        List<Incident> realTimeIncidents = list;

        soccerGame.setGameStatus(Status.IN_PLAY);
        for(Incident incident : realTimeIncidents) {
            incidentProcessor.updateTrackers(incident);
        }
        soccerGame.setGameStatus(Status.ENDED);
    }
}


