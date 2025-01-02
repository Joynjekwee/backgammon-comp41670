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
