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
