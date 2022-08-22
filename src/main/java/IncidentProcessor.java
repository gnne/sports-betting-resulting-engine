import java.util.*;

//   Main method creates instance of class:
public class IncidentProcessor {
    Set<PeriodType> periodTypes;
    Set<IncidentType> incidentTypes;
    Set<TeamType> teamTypes;

    PeriodType periodTypeTracker;                                 // e.g. FIRST_HALF
    Map<IncidentType, Integer> allTracker;                       // e.g. CARD : 3, CORNER: 0, GOAL : 3
    Map<TeamType, Map<IncidentType, Integer>> teamsTracker;     // e.g. HOME: {CARD : 3, CORNER: 0, GOAL : 3}

    Set<IncidentSubscriber> subscribers = Set.of(new ResultGenerator());

    public IncidentProcessor(Fixture game) {
        incidentTypes = Fixture.incidentsForTypeOfGame;  // CARD, CORNER, GOAL
        teamTypes = Fixture.teamsForTypeOfGame;         // HOME, AWAY
        periodTypes = Fixture.periodsForTypeOfGame;    // FIRST_HALF, SECOND_HALF
        initAllIncidentsTracker();
        initTeamsIncidentsTracker();
    }

    public void updateTrackers(Incident incident) {  // called by main method in loop through incidents
        updatePeriodTypeTracker(incident);
        updateAllTracker(incident);
        updateTeamsTracker(incident);
        notifySubscribers();
    }

    void notifySubscribers() {
        for(IncidentSubscriber subscriber : subscribers)
             subscriber.update(periodTypeTracker, allTracker, teamsTracker);
    }

    // initializers:
    /*private*/void initAllIncidentsTracker() {  // private - made default for tests
        allTracker = new HashMap<>();
        for(IncidentType incidentType : incidentTypes) {
            allTracker.put(incidentType, 0);
        }
    }
    /*private*/void initTeamsIncidentsTracker() {
        teamsTracker = new HashMap<>();
        for(TeamType teamType : teamTypes) {
            Map<IncidentType, Integer> teamTracker = new HashMap<>();
            for(IncidentType incidentType : incidentTypes) {
                teamTracker.put(incidentType, 0);
                teamsTracker.put(teamType, teamTracker);
            }
        }
    }

    // updaters:
    /*private*/void updatePeriodTypeTracker(Incident incident) {
        periodTypeTracker = incident.getPeriod();
    }
    /*private*/void updateAllTracker(Incident incident) {
        IncidentType key = incident.getIncident();
        allTracker.computeIfPresent(key, (k, v) -> ++v);
    }
    /*private*/void updateTeamsTracker(Incident incident) {
        TeamType teamKey = incident.getTeam();
        IncidentType value = incident.getIncident();
        teamsTracker.get(teamKey).computeIfPresent(value, (incidentType, count) -> ++count);
    }

    // getters:
    public PeriodType getPeriodTypeTracker() {        return periodTypeTracker;    }
    public Map<IncidentType, Integer> getAllTracker() {        return allTracker;    }
    public Map<TeamType, Map<IncidentType, Integer>> getTeamsTracker() {        return teamsTracker;    }
}
