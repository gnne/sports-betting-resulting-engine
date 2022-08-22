import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import static java.nio.file.StandardOpenOption.CREATE;

public class ResultGenerator implements IncidentSubscriber {

    Map<TeamType, Integer> teamGoals;
    int totalGoals;
    Map<TeamType, Integer> secondHalfCorners;
    int homeFirstHalfCorner = 0, awayFirstHalfCorner = 0;
    TeamType firstCard;
    int incidentCounter = 0;
    Set<ResultSubscriber> markets = Set.of(MarketMatchWinner.INSTANCE, MarketTotalGoals.INSTANCE,
                                        MarketSecondHalfCornersWinner.INSTANCE, MarketFirstCard.INSTANCE);
    ResultGenerator() {
        teamGoals = new HashMap<>();
        secondHalfCorners = new HashMap<>();
    }

    @Override
    public void update(PeriodType periodTypeTracker,
                       Map<IncidentType, Integer> allTracker,
                       Map<TeamType, Map<IncidentType, Integer>> teamsTracker) {
        setTeamGoals(teamsTracker);
        setTotalGoals(allTracker);
        setSecondHalfCorners(periodTypeTracker, teamsTracker);
        setFirstCard(allTracker, teamsTracker);
        generateResult();
    }

    void setTeamGoals(Map<TeamType, Map<IncidentType, Integer>> teamsTracker) {
        teamsTracker.forEach( (team, v) -> {
            v.forEach( (incident, i) -> {    if(incident == IncidentType.GOAL) teamGoals.put(team, i);    });
        });
    }

    void setTotalGoals(Map<IncidentType, Integer> allTracker) {
        allTracker.forEach((incident, v) -> {  if(incident == IncidentType.GOAL) totalGoals = v;  });
    }

    void setSecondHalfCorners(PeriodType periodTypeTracker, Map<TeamType, Map<IncidentType, Integer>> teamsTracker) {
        teamsTracker.forEach( (team, val) -> {
            val.forEach( (incident, i) -> {
                if(incident == IncidentType.CORNER) {
                    if(periodTypeTracker.equals(PeriodType.FIRST_HALF)) {
                        if(team == TeamType.HOME) homeFirstHalfCorner = i; else awayFirstHalfCorner = i;
                        secondHalfCorners.put(TeamType.NONE, Integer.valueOf(1));
                    }
                    else if(periodTypeTracker.equals(PeriodType.SECOND_HALF)) {
                        secondHalfCorners.compute(team, (k, v) -> team == TeamType.HOME ?  i - homeFirstHalfCorner : i - awayFirstHalfCorner);
                        secondHalfCorners.put(TeamType.NONE, Integer.valueOf(0));
                    }
                }
            });
        });
    }

    void setFirstCard(Map<IncidentType, Integer> allTracker, Map<TeamType, Map<IncidentType, Integer>> teamsTracker) {
        if(firstCard == null) {
            if(allTracker.get(IncidentType.CARD) == 1) {
                firstCard = teamsTracker.get(TeamType.HOME).get(IncidentType.CARD) == 1 ? TeamType.HOME
                          : teamsTracker.get(TeamType.HOME).get(IncidentType.CARD) == 1 ? TeamType.AWAY : TeamType.NONE;
            }
        }
    }

    void generateResult()  {
        incidentCounter += 1;
        printResult();
        notifySubscribers();
        try {
            writeAllSelectionsToFile();
        } catch (IOException e) {
            System.out.println("ResultGenerator : getResult : problem with file on call to Market.writeAllSelectionsToFile()");
            e.printStackTrace();
        }
    }

    void printResult() {
        System.out.println("\n=================================== incident " + incidentCounter + " ===================================");
        System.out.print("Goals: ");
        teamGoals.forEach((k, v) -> System.out.print("\t" + k + " : " + v));
        System.out.println("\nTotal goals: " + totalGoals);
        System.out.print("Corners in 2nd half: ");
        secondHalfCorners.forEach((k, v) -> { System.out.print("\t" + k + " : " + v);  });
        System.out.println("\nFirst card: " + (firstCard == null ? "not yet" : firstCard + "\n"));
    }

    void notifySubscribers() {
        Map<TeamType, Integer> totalGoalsMap = new HashMap<>();
        totalGoalsMap.putIfAbsent(TeamType.NONE, Integer.valueOf(totalGoals));
        Map<TeamType, Integer> firstCardMap = new HashMap<>();
        if(firstCard != null) {
            firstCardMap.put(firstCard, 1);
            firstCardMap.put(SoccerGame.getOtherSoccerTeam(firstCard), 0);
            firstCardMap.put(TeamType.NONE, 0);
        }
        for(ResultSubscriber market : markets) {
            market.update(MarketMatchWinner.INSTANCE, teamGoals);
            market.update(MarketTotalGoals.INSTANCE, totalGoalsMap);
            market.update(MarketSecondHalfCornersWinner.INSTANCE, secondHalfCorners);
            market.update(MarketFirstCard.INSTANCE, firstCardMap);
        }
    }

    void writeAllSelectionsToFile() throws IOException {
        System.out.println("\nWriting selection results to file for incident " + incidentCounter);
        int id = 0;
        List<String> linesToWrite = new ArrayList<>();
        for(Market.Selection selection : Market.allSelections) {
            id = selection.getId();
            if(id == 0) continue; // skip 'no corners' selection
            linesToWrite.add(id + " : " + selection.getResult());
        }
        String fileName = "result_incident_" + incidentCounter + ".csv";
        Path path = Paths.get(fileName); // root of project folder
        Files.write(path, linesToWrite, CREATE);
    }
}
