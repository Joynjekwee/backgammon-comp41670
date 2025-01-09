/**
 * Command to handle test-related operations in the game.
 */
public class TestCommand implements Command {
    private Game game;

    public TestCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.handleTestCommand();
    }
}
