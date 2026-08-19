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

        String[] tasks = new String[100];
        int taskCount = 0;

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                for (int i = 0; i < taskCount; i++) {
                    System.out.println((i + 1) + ". " + tasks[i]);
                }
            } else {
                tasks[taskCount] = input;
                taskCount++;
                System.out.println("added: " + input);
            }
        }
        scanner.close();

        String farewell = "Bye. Hope to see you again soon!";
        System.out.println(farewell);
    }
}
