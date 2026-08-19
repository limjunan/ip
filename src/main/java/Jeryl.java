import java.util.Scanner;

public class Jeryl {
    public static void main(String[] args) {
        String banner = "     _                 _ \n"
                + "    | | ___ _ __ _   _| |\n"
                + " _  | |/ _ \\ '__| | | | |\n"
                + "| |_| |  __/ |  | |_| | |\n"
                + " \\___/ \\___|_|   \\__, |_|\n"
                + "                 |___/\n";
        System.out.println(banner);

        String greeting = "Hello! I'm Jeryl.\n" + "What can I do for you?";
        System.out.println(greeting);

        Task[] tasks = new Task[100];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            }
            try {
                if (input.equals("list")) {
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println((i + 1) + "." + tasks[i]);
                    }
                } else if (input.startsWith("mark ") || input.equals("mark")) {
                    int index = parseTaskIndex(input, "mark", taskCount);
                    tasks[index].markAsDone();
                    System.out.println("Nice! I've marked this task as done:");
                    System.out.println("  " + tasks[index]);
                } else if (input.startsWith("unmark ") || input.equals("unmark")) {
                    int index = parseTaskIndex(input, "unmark", taskCount);
                    tasks[index].markAsNotDone();
                    System.out.println("OK, I've marked this task as not done yet:");
                    System.out.println("  " + tasks[index]);
                } else if (input.startsWith("delete ") || input.equals("delete")) {
                    int index = parseTaskIndex(input, "delete", taskCount);
                    Task removed = tasks[index];
                    for (int i = index; i < taskCount - 1; i++) {
                        tasks[i] = tasks[i + 1];
                    }
                    taskCount--;
                    tasks[taskCount] = null;
                    System.out.println("Noted. I've removed this task:");
                    System.out.println("  " + removed);
                    System.out.println("Now you have " + taskCount + " tasks in the list.");
                } else if (input.equals("todo") || input.startsWith("todo ")) {
                    String description = input.equals("todo") ? "" : input.substring(5).trim();
                    if (description.isEmpty()) {
                        throw new JerylException("OOPS!!! The description of a todo cannot be empty.");
                    }
                    tasks[taskCount] = new Todo(description);
                    taskCount++;
                    printAddedMessage(tasks[taskCount - 1], taskCount);
                } else if (input.equals("deadline") || input.startsWith("deadline ")) {
                    String rest = input.equals("deadline") ? "" : input.substring(9);
                    int byIndex = rest.indexOf("/by ");
                    if (byIndex == -1) {
                        throw new JerylException("OOPS!!! A deadline must include \"/by <when>\".");
                    }
                    String description = rest.substring(0, byIndex).trim();
                    String by = rest.substring(byIndex + 4).trim();
                    if (description.isEmpty()) {
                        throw new JerylException("OOPS!!! The description of a deadline cannot be empty.");
                    }
                    if (by.isEmpty()) {
                        throw new JerylException("OOPS!!! The \"/by\" date/time of a deadline cannot be empty.");
                    }
                    tasks[taskCount] = new Deadline(description, by);
                    taskCount++;
                    printAddedMessage(tasks[taskCount - 1], taskCount);
                } else if (input.equals("event") || input.startsWith("event ")) {
                    String rest = input.equals("event") ? "" : input.substring(6);
                    int fromIndex = rest.indexOf("/from ");
                    int toIndex = rest.indexOf("/to ");
                    if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
                        throw new JerylException("OOPS!!! An event must include \"/from <start>\" and \"/to <end>\" in that order.");
                    }
                    String description = rest.substring(0, fromIndex).trim();
                    String from = rest.substring(fromIndex + 6, toIndex).trim();
                    String to = rest.substring(toIndex + 4).trim();
                    if (description.isEmpty()) {
                        throw new JerylException("OOPS!!! The description of an event cannot be empty.");
                    }
                    if (from.isEmpty() || to.isEmpty()) {
                        throw new JerylException("OOPS!!! The \"/from\" and \"/to\" date/time of an event cannot be empty.");
                    }
                    tasks[taskCount] = new Event(description, from, to);
                    taskCount++;
                    printAddedMessage(tasks[taskCount - 1], taskCount);
                } else {
                    throw new JerylException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (JerylException e) {
                System.out.println(e.getMessage());
            }
        }
        scanner.close();

        String farewell = "Bye. Hope to see you again soon!";
        System.out.println(farewell);
    }

    /**
     * Parses the 1-based task index following a "mark"/"unmark" command
     * and validates it against the current task list.
     */
    private static int parseTaskIndex(String input, String commandWord, int taskCount) throws JerylException {
        String indexString = input.length() > commandWord.length()
                ? input.substring(commandWord.length()).trim()
                : "";
        if (indexString.isEmpty()) {
            throw new JerylException("OOPS!!! Please specify which task number to " + commandWord + ".");
        }
        int index;
        try {
            index = Integer.parseInt(indexString) - 1;
        } catch (NumberFormatException e) {
            throw new JerylException("OOPS!!! The task number must be a whole number.");
        }
        if (index < 0 || index >= taskCount) {
            throw new JerylException("OOPS!!! There is no task with that number.");
        }
        return index;
    }

    private static void printAddedMessage(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
