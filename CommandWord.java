/**
 * Enumeration of valid Commands in the Game.
 *
 */
public enum CommandWord
{
    GO("go"), QUIT("quit"), BACK("back"), HELP("help"), UNKNOWN("?"),
    TAKE("take"), DROP("drop"), ITEMS("items");

    private String commandString;
    
    CommandWord (String commandString) {
        this.commandString = commandString;
    }

    public String toString() {
        return commandString;
    }
}
