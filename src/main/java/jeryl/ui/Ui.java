package jeryl.ui;

import java.util.Scanner;

import jeryl.TaskList;
import jeryl.task.Task;

/**
 * Builds the text of every message Jeryl shows the user, and reads raw
 * command-line input for the CLI. Formatting is kept separate from how
 * a message is actually displayed, so the same text can be printed to
 * the console (CLI) or shown in a chat bubble (GUI).
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
     * Returns the app's banner and welcome greeting.
     */
    public String welcomeMessage() {
        return BANNER + "\nHello! I'm Jeryl.\nWhat can I do for you?";
    }

    /**
     * Returns the farewell message shown when the user exits.
     */
    public String goodbyeMessage() {
        return "Bye. Hope to see you again soon!";
    }

    /**
     * Returns every task in the list, numbered from 1, one per line.
     */
    public String taskListMessage(TaskList tasks) {
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < tasks.size(); i++) {
            if (i > 0) {
                message.append('\n');
            }
            message.append(i + 1).append('.').append(tasks.get(i));
        }
        return message.toString();
    }

    /**
     * Returns the tasks matching a "find" search, under the required
     * header, numbered from 1.
     */
    public String matchingTasksMessage(TaskList matches) {
        return "Here are the matching tasks in your list:\n" + taskListMessage(matches);
    }

    /**
     * Returns confirmation that a task was added, along with the new
     * total task count.
     */
    public String addedMessage(Task task, int taskCount) {
        return "Got it. I've added this task:\n  " + task + "\nNow you have " + taskCount + " tasks in the list.";
    }

    /**
     * Returns confirmation that a task was marked as done.
     */
    public String markedMessage(Task task) {
        return "Nice! I've marked this task as done:\n  " + task;
    }

    /**
     * Returns confirmation that a task was marked as not done.
     */
    public String unmarkedMessage(Task task) {
        return "OK, I've marked this task as not done yet:\n  " + task;
    }

    /**
     * Returns confirmation that a task was removed, along with the new
     * total task count.
     */
    public String removedMessage(Task task, int taskCount) {
        return "Noted. I've removed this task:\n  " + task + "\nNow you have " + taskCount + " tasks in the list.";
    }
}
