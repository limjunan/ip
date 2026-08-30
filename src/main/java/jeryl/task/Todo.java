package jeryl.task;

/**
 * A task without any date/time attached to it.
 */
public class Todo extends Task {
    /**
     * Creates a not-yet-done todo with the given description.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toFileString() {
        return "T | " + super.toFileString();
    }
}
