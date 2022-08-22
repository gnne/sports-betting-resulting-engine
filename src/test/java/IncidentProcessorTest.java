import org.junit.jupiter.api.*;

import java.io.File;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IncidentProcessorTest {
    static File incidentsFile = new File("resulting_incidents.json");
    static Map<IncidentType, Integer> expectedAllIncidents = new HashMap<>();
    static Map<TeamType, Map<IncidentType, Integer>> expectedTeamIncidents = new HashMap<>();
    static List<Incident> incidents = new IncidentFileReader(incidentsFile).getList().stream().map(Incident::new).toList();
    static Fixture soccerGame;

    @BeforeAll
    static void soccerGameHasToBeInPlayToProcessIncidents() {
        soccerGame = new SoccerGame(incidentsFile);
        soccerGame.setGameStatus(Status.IN_PLAY);
    }
    @BeforeAll
    static void setExpectedForAll() {
        expectedAllIncidents.put(IncidentType.CARD, 6);
        expectedAllIncidents.put(IncidentType.GOAL, 4);
        expectedAllIncidents.put(IncidentType.CORNER, 5);
    }
    @BeforeAll
    static void setExpectedForTeams() {
        Map<IncidentType, Integer> homeTeamIncidents = new HashMap<>();
        homeTeamIncidents.put(IncidentType.CORNER, 2);
        homeTeamIncidents.put(IncidentType.GOAL, 3);
        homeTeamIncidents.put(IncidentType.CARD, 3);
        expectedTeamIncidents.put(TeamType.HOME, homeTeamIncidents);
        Map<IncidentType, Integer> awayTeamIncidents = new HashMap<>();
        awayTeamIncidents.put(IncidentType.CORNER, 3);
        awayTeamIncidents.put(IncidentType.GOAL, 1);
        awayTeamIncidents.put(IncidentType.CARD, 3);
        expectedTeamIncidents.put(TeamType.AWAY, awayTeamIncidents);
    }

    @Test @Order(0)
    void incidentTypesMatchFixtureType() {
        IncidentProcessor it = new IncidentProcessor(new SoccerGame(incidentsFile  ) );
        for(IncidentType fit : it.incidentTypes) {
            // System.out.println(fit);
            assertTrue(fit.equals(IncidentType.CARD)
                    || fit.equals(IncidentType.CORNER )
                    || fit.equals(IncidentType.GOAL ));
        }
    }

    @Test @Order(1)
    void initPeriodTypeTrackerWorks() {
    }

    @Test  @Order(2)
    void initAllIncidentsTrackerWorks() {
        IncidentProcessor it = new IncidentProcessor(new SoccerGame(incidentsFile ) );
        // method was called in constructor ^
        it.allTracker.forEach( (k, v) -> {
            System.out.println("init all: " + k + " : " + v);
            assertTrue(k.equals(IncidentType.CORNER) || k.equals(IncidentType.CARD) || k.equals(IncidentType.GOAL),
                                "Keys should be CORNER, CARD, GOAL for the SoccerGame fixture");
            assertTrue(v.equals(0), "Values should be initialized to 0");
        });
        assertTrue(it.allTracker.size() == 3, "SoccerGame should have exactly 3 incident types");
    }

    @Test  @Order(3)
    void initTeamsIncidentsTrackerWorks() {
        IncidentProcessor it = new IncidentProcessor(soccerGame);
        // method was called in constructor ^
        it.teamsTracker.forEach( (k, v) -> {
            System.out.println("init team: " + k + " : " + v);
            assertTrue(k.equals(TeamType.HOME) || k.equals(TeamType.AWAY),
                                "Keys should be HOME and AWAY for the SoccerGame fixture");
            v.forEach((incident, count) -> assertTrue(count.equals(0), "Values should be initialized to 0"));
        });
        assertTrue(it.teamsTracker.size() == 2, "SoccerGame should have exactly 2 team types");
    }


    @Test  @Order(5)
    void updateAllTrackerIncrementsValueBy1PerIncident() {
        IncidentProcessor it = new IncidentProcessor(soccerGame);
        Incident incident = soccerGame.gameIncidents.get(0);  
        IncidentType currentKey = incident.getIncident();
        Integer valueBeforeUpdate = it.allTracker.get(currentKey);
        System.out.println("all: value before update: " + valueBeforeUpdate);
        it.updateAllTracker(incident);
        Integer valueAfterUpdate = it.allTracker.get(currentKey);
        System.out.println("all: value after update: " + valueAfterUpdate);
        assertTrue(valueBeforeUpdate.equals(valueAfterUpdate - 1), "Single update: Incident type's value should increment by 1 per incident processed");
    }

    @Test  @Order(6)
    void allTrackerUpdatedCorrectlyAfterSeveralUpdates() {
        IncidentProcessor it = new IncidentProcessor(soccerGame);
        for(Incident incident : incidents) {
            it.updateAllTracker(incident);
        }
        it.allTracker.forEach(
                (k, v) -> {
                    System.out.println(k + ": " + v);
                    assertTrue(v.equals(expectedAllIncidents.get(k)), "Several updates: Values should match expectedAllIncidents");
                }
        );
    }

    @Test  @Order(7)
    void updateTeamsTrackerIncrementsValueBy1PerIncident() {
        IncidentProcessor it = new IncidentProcessor(soccerGame);
        Incident incident = soccerGame.gameIncidents.get(0);  
        TeamType key = incident.getTeam();
        IncidentType value = incident.getIncident();
        Integer valueBeforeUpdate = it.teamsTracker.get(key).get(value);
        System.out.println("teams: value before update: " + valueBeforeUpdate);
        it.updateTeamsTracker(incident);
        Integer valueAfterUpdate = it.teamsTracker.get(key).get(value);
        System.out.println("teams: value after update: " + valueAfterUpdate);
        assertTrue(valueBeforeUpdate.equals(valueAfterUpdate - 1), "Single update: Incident type's value should increment by 1 per incident processed");
    }

    @Test  @Order(8)
    void teamsTrackerUpdatedCorrectlyAfterSeveralUpdates() {
        IncidentProcessor it = new IncidentProcessor(soccerGame);
        for(Incident incident : incidents) {
            it.updateTeamsTracker(incident);
        }
        it.teamsTracker.forEach(
                (k, v) -> {
                    System.out.println(k + ": " + v);
                    v.forEach( (incident, count) -> {
                        assertTrue(count.equals(expectedTeamIncidents.get(k).get(incident)), "Several updates: Values should match expectedTeamIncidents");
                    });
                }
        );
    }

    //todo:
    /* public void updateTrackers(Incident incident) {
        updateAllTracker(incident);
        updateTeamsTracker(incident);
    }*/   // and delete private method tests?

    @AfterEach
    void printDivider() {
        System.out.println("--------------------------------------------");
    }
}
