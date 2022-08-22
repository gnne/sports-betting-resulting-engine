import java.time.Duration;

public class Incident {
    private int id;
    private IncidentType incident;
    private TeamType team;
    private PeriodType period;
    private Duration periodSecondsElapsed;

    public Incident(IncidentFromJson fields) {
        this.id = fields.getId();
        this.incident = IncidentType.valueOf( getString(fields.getIncidentType()) );
        this.team = TeamType.valueOf( getString(fields.getTeamType()) );
        this.period = PeriodType.valueOf( getString(fields.getPeriodType()) );
        this.periodSecondsElapsed = Duration.ofSeconds( fields.getPeriodSecondsElapsed() );
    }

    private String getString(String type) {
        return Utilities.mapJsonFieldNameToEnumValue(type);
    }

    // getters
    public int getId() {        return id;    }
    public IncidentType getIncident() {        return incident;    }
    public TeamType getTeam() {        return team;    }
    public PeriodType getPeriod() {        return period;    }
    public Duration getPeriodSecondsElapsed() {        return periodSecondsElapsed;    }

    public String toString() {
        return "{" + getId() + ", " + getIncident()  + ", " +  getTeam()  + ", " +  getPeriod()  + ", " +  getPeriodSecondsElapsed().getSeconds() + "}";
    }
}


