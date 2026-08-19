/**
 * Represents a single task tracked by the chatbot, with a description
 * and a completion status.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    // 'T' = todo, 'D' = deadline, 'E' = event.
    protected char type;
    protected String by;
    protected String from;
    protected String to;

    public Task(String description, char type) {
        this.description = description;
        this.isDone = false;
        this.type = type;
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
        String result = "[" + type + "][" + getStatusIcon() + "] " + description;
        if (type == 'D') {
            result += " (by: " + by + ")";
        } else if (type == 'E') {
            result += " (from: " + from + " to: " + to + ")";
        }
        return result;
    }
}
