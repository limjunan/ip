package jeryl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class JerylTest {
    @TempDir
    Path tempDir;

    private Jeryl newJeryl() {
        return new Jeryl(tempDir.resolve("jeryl.txt").toString());
    }

    @Test
    public void getResponse_todoWithHighPriority_addsTaskWithPriorityTag() {
        Jeryl jeryl = newJeryl();
        jeryl.getResponse("todo read book /priority high");

        String listing = jeryl.getResponse("list");
        assertTrue(listing.contains("[H][T][ ] read book"), listing);
    }

    @Test
    public void getResponse_todoWithoutPriority_addsTaskWithNoPriorityTag() {
        Jeryl jeryl = newJeryl();
        jeryl.getResponse("todo water the plants");

        String listing = jeryl.getResponse("list");
        assertTrue(listing.contains("[T][ ] water the plants") && !listing.contains("[H]") && !listing.contains("[M]")
                && !listing.contains("[L]"), listing);
    }

    @Test
    public void getResponse_todoWithInvalidPriority_returnsErrorAndDoesNotAddTask() {
        Jeryl jeryl = newJeryl();
        String response = jeryl.getResponse("todo read book /priority urgent");

        assertTrue(response.contains("Invalid priority \"urgent\""), response);
        assertTrue(jeryl.getResponse("list").isEmpty());
    }

    @Test
    public void getResponse_deadlineWithPriority_addsTaskWithPriorityTag() {
        Jeryl jeryl = newJeryl();
        jeryl.getResponse("deadline submit report /by 2026-09-20 /priority medium");

        String listing = jeryl.getResponse("list");
        assertTrue(listing.contains("[M][D][ ] submit report"), listing);
    }
}
