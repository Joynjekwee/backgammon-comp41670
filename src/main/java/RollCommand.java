/**
 * Command to roll the dice in the game.
 */
public class RollCommand implements Command {
   private Game game;

    public RollCommand(Game game) {
        this.game = game;
    }


    @Override
    public void execute() {
        game.handleRollCommand();
    }
}