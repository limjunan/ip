package jeryl;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import jeryl.exception.JerylException;
import jeryl.parser.Command;
import jeryl.parser.Parser;
import jeryl.storage.Storage;
import jeryl.task.Deadline;
import jeryl.task.Event;
import jeryl.task.Task;
import jeryl.task.Todo;
import jeryl.ui.Ui;

/**
 * The Jeryl chatbot's core logic: given one line of user input, updates
 * the task list (and saves it to disk) and returns the response text.
 * Used by both the CLI loop in main() and the JavaFX GUI, so the two
 * front ends stay behaviorally identical.
 */
public class Jeryl {
    private static final String DATA_FILE_PATH = "./data/jeryl.txt";

    private final Storage storage;
    private final Ui ui;
    private final TaskList tasks;

    /**
     * Creates a Jeryl instance backed by the default save file location.
     */
    public Jeryl() {
        this(DATA_FILE_PATH);
    }

    /**
     * Creates a Jeryl instance backed by the given save file location,
     * loading any tasks already saved there.
     */
    public Jeryl(String filePath) {
        this.ui = new Ui();
        this.storage = new Storage(filePath);
        this.tasks = new TaskList(storage.load());
    }

    /**
     * Runs Jeryl's CLI read-parse-execute loop until the user says "bye".
     */
    public static void main(String[] args) {
        new Jeryl().runCli();
    }

    private void runCli() {
        System.out.println(ui.welcomeMessage());
        while (ui.hasNextCommand()) {
            String input = ui.readCommand();
            System.out.println(getResponse(input));
            if (isExit(input)) {
                break;
            }
        }
        ui.close();
    }

    /**
     * Returns true if the given raw input is the "bye" command.
     */
    public boolean isExit(String input) {
        return Parser.parse(input).command() == Command.BYE;
    }

    /**
     * Returns Jeryl's welcome/greeting message, shown once at startup.
     */
    public String welcomeMessage() {
        return ui.welcomeMessage();
    }

    /**
     * Processes one line of raw user input and returns Jeryl's response
     * text, updating the task list (and persisting it to disk) as a
     * side effect where applicable. Any JerylException raised while
     * handling the command is caught and its message returned as the
     * response, rather than propagated.
     */
    public String getResponse(String input) {
        Parser.ParsedInput parsed = Parser.parse(input);
        Command command = parsed.command();
        String args = parsed.arguments();

        try {
            switch (command) {
            case LIST:
                return ui.taskListMessage(tasks);
            case MARK:
                return withSave(markTask(args));
            case UNMARK:
                return withSave(unmarkTask(args));
            case DELETE:
                return withSave(deleteTask(args));
            case TODO:
                return withSave(addTodo(args));
            case DEADLINE:
                return withSave(addDeadline(args));
            case EVENT:
                return withSave(addEvent(args));
            case FIND:
                return findTasks(args);
            case BYE:
                return ui.goodbyeMessage();
            default:
                throw new JerylException("OOPS!!! I'm sorry, but I don't know what that means :-(");
            }
        } catch (JerylException e) {
            return e.getMessage();
        }
    }

    private String withSave(String response) {
        storage.save(tasks.asArrayList());
        return response;
    }

    private String markTask(String args) throws JerylException {
        int index = parseTaskIndex(args, "mark", tasks.size());
        tasks.get(index).markAsDone();
        return ui.markedMessage(tasks.get(index));
    }

    private String unmarkTask(String args) throws JerylException {
        int index = parseTaskIndex(args, "unmark", tasks.size());
        tasks.get(index).markAsNotDone();
        return ui.unmarkedMessage(tasks.get(index));
    }

    private String deleteTask(String args) throws JerylException {
        int index = parseTaskIndex(args, "delete", tasks.size());
        Task removed = tasks.delete(index);
        return ui.removedMessage(removed, tasks.size());
    }

    private String addTodo(String args) throws JerylException {
        String description = args.trim();
        if (description.isEmpty()) {
            throw new JerylException("OOPS!!! The description of a todo cannot be empty.");
        }
        tasks.add(new Todo(description));
        return ui.addedMessage(tasks.get(tasks.size() - 1), tasks.size());
    }

    private String addDeadline(String args) throws JerylException {
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
        return ui.addedMessage(tasks.get(tasks.size() - 1), tasks.size());
    }

    private String addEvent(String args) throws JerylException {
        int fromIndex = args.indexOf("/from ");
        int toIndex = args.indexOf("/to ");
        if (fromIndex == -1 || toIndex == -1 || toIndex < fromIndex) {
            throw new JerylException(
                    "OOPS!!! An event must include \"/from <start>\" and \"/to <end>\" in that order.");
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
        return ui.addedMessage(tasks.get(tasks.size() - 1), tasks.size());
    }

    private String findTasks(String args) throws JerylException {
        String keyword = args.trim();
        if (keyword.isEmpty()) {
            throw new JerylException("OOPS!!! Please specify a keyword to find.");
        }
        return ui.matchingTasksMessage(tasks.find(keyword));
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
}
