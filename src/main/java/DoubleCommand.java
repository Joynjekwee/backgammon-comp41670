/**
 * Command to handle doubling actions in the game.
 * It delegates the action to the game's doubleCommand method.
 */

public class DoubleCommand implements Command{
    private Game game;

    public DoubleCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
       game.doubleCommand();
    }
}
