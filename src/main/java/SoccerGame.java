import java.io.File;
import java.time.Duration;
import java.util.Set;

public class SoccerGame extends Fixture {

    static {
        lengthOfGame = Duration.ofMinutes(90);
        incidentsForTypeOfGame = Set.of(IncidentType.CARD, IncidentType.CORNER, IncidentType.GOAL);
        teamsForTypeOfGame =  Set.of(TeamType.HOME, TeamType.AWAY);
        periodsForTypeOfGame = Set.of(PeriodType.FIRST_HALF, PeriodType.SECOND_HALF);
    }

    SoccerGame(File incidentsJson) {
       super(incidentsJson);
    }

    public static TeamType getOtherSoccerTeam(TeamType team1) {
        TeamType otherTeam = null;
        for(TeamType aTeam : teamsForTypeOfGame) {
            if(aTeam.equals(team1)) continue;
            otherTeam = aTeam;
        }
        return otherTeam;
    }

    public void setGameStatus(Status newStatus) {
        this.event = newStatus;
    }

    public Status getGameStatus() {  return this.event; }
}

