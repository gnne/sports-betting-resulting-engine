import java.util.*;
import java.util.stream.Collectors;

/*  class to deal with incidents as a whole.
    Could have other methods to sort/group the fields */
public class Incidents {

    // called by Fixture constructor
    static List<Incident> get(List<IncidentFromJson> jsonObjects) {
        return jsonObjects.stream()
                          .map(Incident::new)
                          .toList();
    }

    static Map<IncidentType, List<Incident>> groupedByIncidentType(List<Incident> list) {
        return list.stream()
                   .collect(Collectors.groupingBy(Incident::getIncident));//, Collectors.counting()));
    }

    static Map<PeriodType, List<Incident>> groupedByPeriodType(List<Incident> list) {
        return list.stream()
                .collect(Collectors.groupingBy(Incident::getPeriod));//, Collectors.counting()));
    }
}
