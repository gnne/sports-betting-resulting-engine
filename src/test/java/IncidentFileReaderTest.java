import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.List;

public class IncidentFileReaderTest {
    static String fileName = "resulting_incidents.json";
    static File jsonFile = new File(fileName);
    IncidentFileReader reader = new IncidentFileReader(jsonFile);

    @Test
    void constructorTest() { }

    @Test
    void checkIncidentsFromJsonObject() {
        // constructor already called readObjectsFromFile(), so no need to here.
        List<IncidentFromJson> list = reader.getList();
        for(IncidentFromJson i : list) {
           System.out.println(i);
        }
    }
}