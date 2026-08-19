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
            } else if (input.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + "." + tasks[i]);
                }
            } else if (input.startsWith("mark ")) {
                int index = Integer.parseInt(input.substring(5)) - 1;
                tasks[index].markAsDone();
                System.out.println("Nice! I've marked this task as done:");
                System.out.println("  " + tasks[index]);
            } else if (input.startsWith("unmark ")) {
                int index = Integer.parseInt(input.substring(7)) - 1;
                tasks[index].markAsNotDone();
                System.out.println("OK, I've marked this task as not done yet:");
                System.out.println("  " + tasks[index]);
            } else if (input.startsWith("todo ")) {
                String description = input.substring(5);
                tasks[taskCount] = new Task(description, 'T');
                taskCount++;
                printAddedMessage(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("deadline ")) {
                String rest = input.substring(9);
                int byIndex = rest.indexOf("/by ");
                String description = rest.substring(0, byIndex).trim();
                String by = rest.substring(byIndex + 4).trim();
                Task task = new Task(description, 'D');
                task.by = by;
                tasks[taskCount] = task;
                taskCount++;
                printAddedMessage(tasks[taskCount - 1], taskCount);
            } else if (input.startsWith("event ")) {
                String rest = input.substring(6);
                int fromIndex = rest.indexOf("/from ");
                int toIndex = rest.indexOf("/to ");
                String description = rest.substring(0, fromIndex).trim();
                String from = rest.substring(fromIndex + 6, toIndex).trim();
                String to = rest.substring(toIndex + 4).trim();
                Task task = new Task(description, 'E');
                task.from = from;
                task.to = to;
                tasks[taskCount] = task;
                taskCount++;
                printAddedMessage(tasks[taskCount - 1], taskCount);
            }
        }
        scanner.close();

        String farewell = "Bye. Hope to see you again soon!";
        System.out.println(farewell);
    }

    private static void printAddedMessage(Task task, int taskCount) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + task);
        System.out.println("Now you have " + taskCount + " tasks in the list.");
    }
}
