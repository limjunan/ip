package jeryl.task;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A task that starts on a specific date and ends on a specific date.
 */
public class Event extends Task {
    private static final DateTimeFormatter PRINT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy");

    protected LocalDate from;
    protected LocalDate to;

    /**
     * Creates a not-yet-done event task spanning the given start and
     * end dates.
     */
    public Event(String description, LocalDate from, LocalDate to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from.format(PRINT_FORMAT)
                + " to: " + to.format(PRINT_FORMAT) + ")";
    }

    @Override
    public String toFileString() {
        return "E | " + super.toFileString() + " | " + from + " | " + to;
    }
}
