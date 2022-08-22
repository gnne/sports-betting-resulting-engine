import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SoccerGameTest {
    static String fileName = "resulting_incidents.json";
    static File incidentsFile = new File(fileName);

    @Test
    /* The Json Mapper API required a file of type java.io.File.
        This is an older API, and is problematic to test with (according to comment read online).

        Leaving incomplete for now due to time constraint.
     */
    void constructorThrowsFileNotFoundIfFileNotExists() throws FileNotFoundException {
        File incidentsFile = new File("directory_does_not_exist/" + "resulting_incidents.json");
        Fixture newSoccerGame = new SoccerGame(incidentsFile);
        Exception exception = assertThrows(
                FileNotFoundException.class,
                () ->  new SoccerGame(new File("directory_does_not_exist/" + fileName)),
                "assertThrows FileNotFoundException - test failed");
     }
     @Test
    void constructorThrowsNullPointerExceptionIfFileNotExists() throws FileNotFoundException {
        File incidentsFile = new File("directory_does_not_exist/" + "resulting_incidents.json");
        Fixture newSoccerGame = new SoccerGame(incidentsFile);
        Exception exception = assertThrows(
                NullPointerException.class,
                () ->  new SoccerGame(new File("directory_does_not_exist/" + fileName)),
                "assertThrows NullPointerException - test failed");
    }
}
