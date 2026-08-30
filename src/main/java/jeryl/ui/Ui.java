package jeryl.ui;

import java.util.Scanner;

import jeryl.TaskList;
import jeryl.task.Task;

/**
 * Deals with all interactions with the user: printing messages to the
 * console and reading the user's input.
 */
public class Ui {
    private static final String BANNER = "     _                 _ \n"
            + "    | | ___ _ __ _   _| |\n"
            + " _  | |/ _ \\ '__| | | | |\n"
            + "| |_| |  __/ |  | |_| | |\n"
            + " \\___/ \\___|_|   \\__, |_|\n"
            + "                 |___/\n";

    private final Scanner scanner;

    /**
     * Creates a Ui that reads user input from standard input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Prints the app's banner and welcome greeting.
     */
    public void showWelcome() {
        System.out.println(BANNER);
        System.out.println("Hello! I'm Jeryl.\n" + "What can I do for you?");
    }

    /**
     * Returns true if there is another line of user input to read.
     */
    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    /**
     * Reads one line of raw user input.
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    /**
     * Releases the resources held by this Ui (e.g. the input scanner).
     */
    public void close() {
        scanner.close();
    }

    /**
     * Prints the farewell message shown when the user exits.
     */
    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    /**
     * Prints an error message, e.g. from a caught JerylException.
     */
    public void showError(String message) {
        System.out.println(message);
    }

    /**
     * Prints every task in the list, numbered from 1.
     */
    public void showTaskList(TaskList tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    /**
     * Prints the tasks matching a "find" search, under the required
     * header, numbered from 1.
     */
    public void showMatchingTasks(TaskList matches) {
        System.out.println("Here are the matching tasks in your list:");
        showTaskList(matches);
    }

    /**
     * Prints confirmation that a task was added, along with the new
     * total task count.
     */
    public void showAddedMessage(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    /**
     * Prints confirmation that a task was marked as done.
     */
    public void showMarkedMessage(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    /**
     * Prints confirmation that a task was marked as not done.
     */
    public void showUnmarkedMessage(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    /**
     * Prints confirmation that a task was removed, along with the new
     * total task count.
     */
    public void showRemovedMessage(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
