import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.util.List;

class IncidentFileReader {
    private File jsonFile;
    private List<IncidentFromJson> list;
    private ObjectMapper mapper;
    private SimpleModule module;

    IncidentFileReader(File jsonFile) {
        this.jsonFile = jsonFile;
        mapper = new ObjectMapper();
        module = new SimpleModule("IncidentDeserializer", new Version(1, 0, 0, null, null, null));
        module.addDeserializer(IncidentFromJson.class, new IncidentDeserializer());
        mapper.registerModule(module);
        readObjectsFromFile();
    }

    private void readObjectsFromFile() {
        try {
            list = mapper.readValue(jsonFile, new TypeReference<>(){});
        } catch (IOException e) {
            e.printStackTrace();
        } finally {  }
    }

    public List<IncidentFromJson> getList() {
        return this.list;
    }
}

/*
    https://www.baeldung.com/jackson-object-mapper-tutorial
 */
class IncidentDeserializer extends StdDeserializer<IncidentFromJson> {
    public IncidentDeserializer() {        this(null);    }
    public IncidentDeserializer(Class<?> vc) {        super(vc);    }

    @Override
    public IncidentFromJson deserialize(JsonParser parser,
                                    DeserializationContext deserializer) throws IOException {
        IncidentFromJson incidentFromJson = new IncidentFromJson();
        ObjectCodec codec = parser.getCodec();

        try {
            JsonNode node = codec.readTree(parser);
            incidentFromJson.setId(node.get("Id").asInt());
            incidentFromJson.setIncidentType(node.get("IncidentType").asText());
            incidentFromJson.setTeamType(node.get("TeamType").asText());
            incidentFromJson.setPeriodType(node.get("PeriodType").asText());
            incidentFromJson.setPeriodSecondsElapsed(node.get("PeriodSecondsElapsed").asInt());
        } catch (IOException ioe){
            ioe.printStackTrace();
        }

        return incidentFromJson;
    }
}

/*
    class created to map the Json objects (resulting_incidents.json)
    used by Fixture constructor
*/
class IncidentFromJson {
    private int id, periodSecondsElapsed;
    String incidentType, teamType, periodType;

    public int getId() {       return id;    }
    public String getIncidentType() {        return incidentType;    }
    public String getTeamType() {        return teamType;    }
    public String getPeriodType() {        return periodType;    }
    public int getPeriodSecondsElapsed() {        return periodSecondsElapsed;    }

    public void setId(int id) {        this.id = id;    }
    public void setIncidentType(String incidentType) {        this.incidentType = incidentType;    }
    public void setTeamType(String teamType) {       this.teamType = teamType;    }
    public void setPeriodType(String periodType) {        this.periodType = periodType;    }
    public void setPeriodSecondsElapsed(int periodSecondsElapsed) {        this.periodSecondsElapsed = periodSecondsElapsed;    }

    public String toString() {
        return String.format( "%2d %-9s%-6s%-13s%-7d", getId(), getIncidentType(), getTeamType(), getPeriodType(), getPeriodSecondsElapsed() );
    }
}