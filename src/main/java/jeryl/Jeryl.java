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

public class Jeryl {
    private static final String DATA_FILE_PATH = "./data/jeryl.txt";

    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage(DATA_FILE_PATH);
        TaskList tasks = new TaskList(storage.load());

        while (ui.hasNextCommand()) {
            Parser.ParsedInput parsed = Parser.parse(ui.readCommand());
            Command command = parsed.command();
            String rest = parsed.arguments();

            if (command == Command.BYE) {
                break;
            }
            try {
                switch (command) {
                case LIST:
                    ui.showTaskList(tasks);
                    break;
                case MARK:
                    markTask(tasks, rest, ui);
                    storage.save(tasks.asArrayList());
                    break;
                case UNMARK:
                    unmarkTask(tasks, rest, ui);
                    storage.save(tasks.asArrayList());
                    break;
                case DELETE:
                    deleteTask(tasks, rest, ui);
                    storage.save(tasks.asArrayList());
                    break;
                case TODO:
                    addTodo(tasks, rest, ui);
                    storage.save(tasks.asArrayList());
                    break;
                case DEADLINE:
                    addDeadline(tasks, rest, ui);
                    storage.save(tasks.asArrayList());
                    break;
                case EVENT:
                    addEvent(tasks, rest, ui);
                    storage.save(tasks.asArrayList());
                    break;
                default:
                    throw new JerylException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (JerylException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.close();
        ui.showGoodbye();
    }

    private static void markTask(TaskList tasks, String args, Ui ui) throws JerylException {
        int index = parseTaskIndex(args, "mark", tasks.size());
        tasks.get(index).markAsDone();
        ui.showMarkedMessage(tasks.get(index));
    }

    private static void unmarkTask(TaskList tasks, String args, Ui ui) throws JerylException {
        int index = parseTaskIndex(args, "unmark", tasks.size());
        tasks.get(index).markAsNotDone();
        ui.showUnmarkedMessage(tasks.get(index));
    }

    private static void deleteTask(TaskList tasks, String args, Ui ui) throws JerylException {
        int index = parseTaskIndex(args, "delete", tasks.size());
        Task removed = tasks.delete(index);
        ui.showRemovedMessage(removed, tasks.size());
    }

    private static void addTodo(TaskList tasks, String args, Ui ui) throws JerylException {
        String description = args.trim();
        if (description.isEmpty()) {
            throw new JerylException("OOPS!!! The description of a todo cannot be empty.");
        }
        tasks.add(new Todo(description));
        ui.showAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
    }

    private static void addDeadline(TaskList tasks, String args, Ui ui) throws JerylException {
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
        ui.showAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
    }

    private static void addEvent(TaskList tasks, String args, Ui ui) throws JerylException {
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
        ui.showAddedMessage(tasks.get(tasks.size() - 1), tasks.size());
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
