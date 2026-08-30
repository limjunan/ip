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

    public void showGoodbye() {
        System.out.println("Bye. Hope to see you again soon!");
    }

    public void showError(String message) {
        System.out.println(message);
    }

    public void showTaskList(TaskList tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    public void showMatchingTasks(TaskList matches) {
        System.out.println("Here are the matching tasks in your list:");
        showTaskList(matches);
    }

    public void showAddedMessage(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }

    public void showMarkedMessage(Task task) {
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + task);
    }

    public void showUnmarkedMessage(Task task) {
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + task);
    }

    public void showRemovedMessage(Task task, int taskCount) {
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
