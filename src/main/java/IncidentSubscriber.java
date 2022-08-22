import java.util.*;

public interface IncidentSubscriber{
    void update(PeriodType periodTypeTracker,
                Map<IncidentType, Integer> allTracker,
                Map<TeamType, Map<IncidentType, Integer>> teamsTracker);
}
