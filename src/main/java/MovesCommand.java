public class MovesCommand implements Command{
    private Game game;

    public MovesCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        game.handleMoreLegalMovesCommand();
    }
}
