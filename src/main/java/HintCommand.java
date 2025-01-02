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
