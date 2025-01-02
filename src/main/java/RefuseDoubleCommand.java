public class RefuseDoubleCommand implements Command {
    private Game game;

    public RefuseDoubleCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.handleRefuseDouble();
    }
}
