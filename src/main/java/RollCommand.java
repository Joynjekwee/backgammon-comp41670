import java.util.List;

public class RollCommand implements Command {
   private Game game;

    public RollCommand(Game game) {
        this.game = game;
    }


    @Override
    public void execute() {
        game.handleRoll();
    }
}