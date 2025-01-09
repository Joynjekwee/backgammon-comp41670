
/**
 * Command to display pip counts for the current game state.
 */
public class PipCommand implements Command {
    private Game game;

    public PipCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.getBoard().displayTotalPipCounts(game.getPlayer1(), game.getPlayer2());
    }
}