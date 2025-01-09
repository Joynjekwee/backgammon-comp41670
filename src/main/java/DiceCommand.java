/**
 * Command to handle a custom dice action in the game.
 * It delegates the action to the game's handleCustomDiceCommand method.
 */

public class DiceCommand implements Command {
    private Game game;

    public DiceCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.handleCustomDiceCommand();
    }
}
