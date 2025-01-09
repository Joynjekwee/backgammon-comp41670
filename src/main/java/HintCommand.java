/**
 * Command to display available commands in the game.
 * It delegates the action to the game's printCommands method.
 */

public class HintCommand implements Command{
    private Game game;

    public HintCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.printCommands();
    }
}
