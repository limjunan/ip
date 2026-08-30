package jeryl;

import java.util.ArrayList;

import jeryl.task.Task;

/**
 * Wraps the list of tasks currently tracked by the chatbot, and provides
 * the operations needed to add to, remove from, and inspect that list.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    /**
     * Creates an empty task list.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list backed by the given tasks, e.g. ones just
     * loaded from disk.
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Appends a task to the end of the list.
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given 0-based index.
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Returns the task at the given 0-based index.
     */
    public Task get(int index) {
        return tasks.get(index);
    }

    /**
     * Returns the number of tasks currently in the list.
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the tasks whose description contains the given keyword
     * (case-insensitive), in their original order.
     */
    public TaskList find(String keyword) {
        ArrayList<Task> matches = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Task task : tasks) {
            if (task.getDescription().toLowerCase().contains(lowerKeyword)) {
                matches.add(task);
            }
        }
        return new TaskList(matches);
    }

    /**
     * Returns the underlying tasks as a plain ArrayList, e.g. so Storage
     * can write them all out to disk.
     */
    public ArrayList<Task> asArrayList() {
        return tasks;
    }
}
