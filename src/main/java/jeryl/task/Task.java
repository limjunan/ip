package jeryl.task;

/**
 * Represents a single task tracked by the chatbot, with a description
 * and a completion status. Subclasses add the type-specific details
 * (e.g. a deadline's due date).
 */
public class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public void markAsDone() {
        isDone = true;
    }

    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns "X" if the task is done, or a blank space otherwise.
     */
    public String getStatusIcon() {
        return isDone ? "X" : " ";
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    /**
     * Returns this task encoded as a single line for saving to disk, e.g.
     * "T | 1 | read book". Subclasses append their own type-specific fields.
     */
    public String toFileString() {
        return (isDone ? "1" : "0") + " | " + description;
    }
}
