public class QuitCommand implements Command {
    private Game game;

    public QuitCommand(Game game) {
        this.game = game;
    }

    @Override
    public void execute() {
        System.out.println("Quitting Game Now...");
        game.setIfMatchOver(true);
        game.setGameOver(true);
    }
}
