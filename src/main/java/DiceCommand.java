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
