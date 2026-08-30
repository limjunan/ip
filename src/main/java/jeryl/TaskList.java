package jeryl;

import java.util.ArrayList;

import jeryl.task.Task;

/**
 * Wraps the list of tasks currently tracked by the chatbot, and provides
 * the operations needed to add to, remove from, and inspect that list.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task delete(int index) {
        return tasks.remove(index);
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying tasks as a plain ArrayList, e.g. so Storage
     * can write them all out to disk.
     */
    public ArrayList<Task> asArrayList() {
        return tasks;
    }
}
