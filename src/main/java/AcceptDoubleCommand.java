/**
 * Command to accept a doubling offer in a game.
 * It delegates the action to the game's handleAcceptDouble method.
 */

public class AcceptDoubleCommand implements Command {
    private Game game;

    public AcceptDoubleCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.handleAcceptDouble();
    }
}
