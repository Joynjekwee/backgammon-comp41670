import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoveExecution {
    private Map<Integer, ArrayList<Checker>> board; // Represents the current state of the game board
    private Bar bar;
    private BearOffArea bearOffArea;

    // Constructor to initialize the MoveExecution class
    public MoveExecution(Map<Integer, ArrayList<Checker>> board, Bar bar, BearOffArea bearOffArea) {
        this.board = board;
        this.bar = bar;
        this.bearOffArea = bearOffArea;
    }
    // Executes a move from a starting position to an ending position, handling varying scenarios
    public void executeMove(int start, int end, Player player, Constants c) {


        if (start == 0) { // Moving from the bar
            // Check if destination has opponent's checker and handle it
            ArrayList<Checker> destination = board.get(end);
            String opponentSymbol = player.getSymbol().equals(c.X) ? c.O : c.X;
            if (!destination.isEmpty() &&  destination.get(0).getSymbol().equals(opponentSymbol)) {
                if (destination.size() == 1) { // Single opponent checker - hit it
                    Checker hitChecker = destination.remove(0);
                    bar.addToBar(hitChecker.getSymbol(), hitChecker);
                    System.out.println("Hit opponent's checker and sent it to the bar!");
                } else {
                    System.out.println("Invalid move: Cannot move to a point occupied by multiple opponent checkers.");
                    return; // Invalid move, do nothing
                }
            }
            moveFromBar(player.getSymbol(), end);
            return;
        }

        if (end == 25) { // Handle bear-off
            executeBearOff(start, player);
            return;
        }

        if (isOpponentHit(start, end, player, c)) { // Hitting opponent's checker
            hitOpponentChecker(end, player, c);
        }

        moveChecker(start, end);
    }

    // Moves a checker from the bar to the specified position on the board
    protected void moveFromBar(String symbol, int end) {
        Checker checker = bar.removeFromBar(symbol);
        checker.setPosition(end); //update checker position!!!
        board.get(end).add(checker);
    }

    // Handles the bear-off logic when a checker is removed from the board
    protected void executeBearOff(int start, Player player) {
        ArrayList<Checker> startCheckers = board.get(start);
        if (startCheckers.isEmpty() || !startCheckers.get(0).getSymbol().equals(player.getSymbol())) {
            throw new IllegalStateException("No checkers at position to bear off.");
        }

        Checker checker = startCheckers.remove(0);
        checker.setPosition(25);//it is off the board
        bearOffArea.addChecker(player.getSymbol());
        System.out.println("Checker borne off for player: " + player.getSymbol());
    }

    // Checks if the move will hit an opponent's checker
    private boolean isOpponentHit(int start, int end, Player player, Constants c) {
        ArrayList<Checker> startCheckers = board.get(start);
        ArrayList<Checker> endCheckers = board.get(end);

        if (endCheckers.size() == 1 && !startCheckers.isEmpty()) {
            String opponentSymbol = player.getSymbol().equals(c.X) ? c.O : c.X;
            return endCheckers.get(0).getSymbol().equals(opponentSymbol); //true if hit!!!
        }

        return false;
    }
    //logic of hitting an opponent's checker
    private void hitOpponentChecker(int end, Player player, Constants c) {
        ArrayList<Checker> endCheckers = board.get(end);
        Checker hitChecker = endCheckers.remove(0); // Remove the opponent's checker
        String opponentSymbol = player.getSymbol().equals(c.X) ? c.O : c.X;

        bar.addToBar(opponentSymbol, hitChecker); //hit checker to bar
        System.out.println("Hit opponent's checker and moved to bar: " + hitChecker.getSymbol());
    }
    // Moves a checker from a starting position to an ending position on the board
    private void moveChecker(int start, int end) {
        Checker checker = board.get(start).remove(0);
        board.get(end).add(checker);
    }
}

