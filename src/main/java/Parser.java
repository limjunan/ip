/**
 * Deals with making sense of raw user input: splitting a line into a
 * command word and the remaining arguments.
 */
public class Parser {
    /**
     * A line of user input split into its command word (the first
     * whitespace-separated token) and everything after it.
     */
    public record ParsedInput(Command command, String arguments) {
    }

    public static ParsedInput parse(String input) {
        int spaceIndex = input.indexOf(' ');
        String keyword = spaceIndex == -1 ? input : input.substring(0, spaceIndex);
        String arguments = spaceIndex == -1 ? "" : input.substring(spaceIndex + 1);
        return new ParsedInput(Command.fromKeyword(keyword), arguments);
    }
}
