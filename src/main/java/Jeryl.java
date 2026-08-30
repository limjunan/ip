import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Jeryl {
    private static final String DATA_FILE_PATH = "./data/jeryl.txt";

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

        Storage storage = new Storage(DATA_FILE_PATH);
        ArrayList<Task> tasks = storage.load();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            int spaceIndex = input.indexOf(' ');
            String keyword = spaceIndex == -1 ? input : input.substring(0, spaceIndex);
            String rest = spaceIndex == -1 ? "" : input.substring(spaceIndex + 1);
            Command command = Command.fromKeyword(keyword);

            if (command == Command.BYE) {
                break;
            }
            try {
                switch (command) {
                case LIST:
                    listTasks(tasks);
                    break;
                case MARK:
                    markTask(tasks, rest);
                    storage.save(tasks);
                    break;
                case UNMARK:
                    unmarkTask(tasks, rest);
                    storage.save(tasks);
                    break;
                case DELETE:
                    deleteTask(tasks, rest);
                    storage.save(tasks);
                    break;
                case TODO:
                    addTodo(tasks, rest);
                    storage.save(tasks);
                    break;
                case DEADLINE:
                    addDeadline(tasks, rest);
                    storage.save(tasks);
                    break;
                case EVENT:
                    addEvent(tasks, rest);
                    storage.save(tasks);
                    break;
                default:
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

    private static void listTasks(ArrayList<Task> tasks) {
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    private static void markTask(ArrayList<Task> tasks, String args) throws JerylException {
        int index = parseTaskIndex(args, "mark", tasks.size());
        tasks.get(index).markAsDone();
        System.out.println("Nice! I've marked this task as done:");
        System.out.println("  " + tasks.get(index));
    }

    private static void unmarkTask(ArrayList<Task> tasks, String args) throws JerylException {
        int index = parseTaskIndex(args, "unmark", tasks.size());
        tasks.get(index).markAsNotDone();
        System.out.println("OK, I've marked this task as not done yet:");
        System.out.println("  " + tasks.get(index));
    }

    private static void deleteTask(ArrayList<Task> tasks, String args) throws JerylException {
        int index = parseTaskIndex(args, "delete", tasks.size());
        Task removed = tasks.remove(index);
        System.out.println("Noted. I've removed this task:");
        System.out.println("  " + removed);
        System.out.println("Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void addTodo(ArrayList<Task> tasks, String args) throws JerylException {
        String description = args.trim();
        if (description.isEmpty()) {
            throw new JerylException("OOPS!!! The description of a todo cannot be empty.");
        }
        tasks.add(new Todo(description));
        printAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
    }

    private static void addDeadline(ArrayList<Task> tasks, String args) throws JerylException {
        int byIndex = args.indexOf("/by ");
        if (byIndex == -1) {
            throw new JerylException("OOPS!!! A deadline must include \"/by <when>\".");
        }
        String description = args.substring(0, byIndex).trim();
        String by = args.substring(byIndex + 4).trim();
        if (description.isEmpty()) {
            throw new JerylException("OOPS!!! The description of a deadline cannot be empty.");
        }
        if (by.isEmpty()) {
            throw new JerylException("OOPS!!! The \"/by\" date/time of a deadline cannot be empty.");
        }
        tasks.add(new Deadline(description, parseDate(by)));
        printAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
    }

    private static void addEvent(ArrayList<Task> tasks, String args) throws JerylException {
        int fromIndex = args.indexOf("/from ");
        int toIndex = args.indexOf("/to ");
        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new JerylException("OOPS!!! An event must include \"/from <start>\" and \"/to <end>\" in that order.");
        }
        String description = args.substring(0, fromIndex).trim();
        String from = args.substring(fromIndex + 6, toIndex).trim();
        String to = args.substring(toIndex + 4).trim();
        if (description.isEmpty()) {
            throw new JerylException("OOPS!!! The description of an event cannot be empty.");
        }
        if (from.isEmpty() || to.isEmpty()) {
            throw new JerylException("OOPS!!! The \"/from\" and \"/to\" date/time of an event cannot be empty.");
        }
        tasks.add(new Event(description, parseDate(from), parseDate(to)));
        printAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
    }

    /**
     * Parses a date string in yyyy-MM-dd format (e.g. 2019-10-15), as
     * used by both the "/by" field of a deadline and the "/from"/"/to"
     * fields of an event.
     */
    private static LocalDate parseDate(String dateString) throws JerylException {
        try {
            return LocalDate.parse(dateString);
        } catch (DateTimeParseException e) {
            throw new JerylException(
                    "OOPS!!! Please give the date as yyyy-mm-dd, e.g. 2019-10-15. \""
                            + dateString + "\" isn't in that format.");
        }
    }

    /**
     * Parses the 1-based task index that follows a "mark"/"unmark"/"delete"
     * command and validates it against the current task count.
     */
    private static int parseTaskIndex(String args, String commandWord, int taskCount) throws JerylException {
        String indexString = args.trim();
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
