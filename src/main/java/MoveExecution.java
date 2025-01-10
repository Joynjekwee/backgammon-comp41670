import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MoveExecution {
    private Map<Integer, ArrayList<Checker>> board;
    private Bar bar;
    private BearOffArea bearOffArea;

    public MoveExecution(Map<Integer, ArrayList<Checker>> board, Bar bar, BearOffArea bearOffArea) {
        this.board = board;
        this.bar = bar;
        this.bearOffArea = bearOffArea;
    }

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

    protected void moveFromBar(String symbol, int end) {
        Checker checker = bar.removeFromBar(symbol);
        checker.setPosition(end); //update checker position!!!
        board.get(end).add(checker);
        System.out.println("Moved checker from bar to " + end);
    }

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

    private boolean isOpponentHit(int start, int end, Player player, Constants c) {
        ArrayList<Checker> startCheckers = board.get(start);
        ArrayList<Checker> endCheckers = board.get(end);

        if (endCheckers.size() == 1 && !startCheckers.isEmpty()) {
            String opponentSymbol = player.getSymbol().equals(c.X) ? c.O : c.X;
            return endCheckers.get(0).getSymbol().equals(opponentSymbol);
        }

        return false;
    }

    private void hitOpponentChecker(int end, Player player, Constants c) {
        ArrayList<Checker> endCheckers = board.get(end);
        Checker hitChecker = endCheckers.remove(0);
        String opponentSymbol = player.getSymbol().equals(c.X) ? c.O : c.X;

        bar.addToBar(opponentSymbol, hitChecker);
        System.out.println("Hit opponent's checker and moved to bar: " + hitChecker.getSymbol());
    }

    private void moveChecker(int start, int end) {
        Checker checker = board.get(start).remove(0);
        board.get(end).add(checker);
        System.out.println("Moved checker from " + start + " to " + end);
    }
}

