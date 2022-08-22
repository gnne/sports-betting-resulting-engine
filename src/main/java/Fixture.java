import java.io.File;
import java.time.Duration;
import java.util.*;

public abstract class Fixture {

    static Duration lengthOfGame;
    static Set<IncidentType> incidentsForTypeOfGame;
    static Set<TeamType> teamsForTypeOfGame;
    static Set<PeriodType> periodsForTypeOfGame;

    File incidentsJson;
    Status event;
    List<Incident> gameIncidents;

    protected Fixture(File incidentsJson)  {
        this.incidentsJson = incidentsJson;
        List<IncidentFromJson> listFromJson = new IncidentFileReader(incidentsJson).getList();
        this.gameIncidents = Incidents.get(listFromJson);
        this.event = Status.PENDING;
    }

    public void setGameStatus(Status newStatus) {
        this.event = newStatus;
    }
}

/* status of game play */
enum Status{ PENDING, IN_PLAY, ENDED };

/* types of incident that could occur in a fixture */
enum IncidentType {CARD, CORNER, GOAL, PENALTY, OFFSIDE, RULE_BREAK};

/*  to store types of team etc. */
enum TeamType {HOME, AWAY, PLAYER_1, PLAYER_2, LAST_YEARS_WINNER, WORLD_CHAMPION, NONE;};
                                                    // added NONE for a draw or selection without a team

/* to store all labels for fixtures  */
enum PeriodType {FIRST_HALF, SECOND_HALF, EXTRA_TIME, SET, FRAME};


