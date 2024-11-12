import java.util.ArrayList;

public class Validation {
    private Board board;

    public Validation(ArrayList<ArrayList<Checker>> board) {
       // this.board = board;
    }

    // Check if there are checkers on the bar for a specific player
    public boolean areThereCheckersOnTheBar(Player player) {
        if (player.getSymbol().equals("X")) {
            return board.getBarX() != null && !board.getBarX().isEmpty();
        } else if (player.getSymbol().equals("O")) {
            return board.getBarO() != null && !board.getBarO().isEmpty();
        }
        return false;
    }

    public ArrayList<String> canWeMakeAMove(int start, int die1, int die2, Player player) {

        ArrayList<String> potentialLegalMoves = new ArrayList<>();
        int[] newPositions = {
                start + die1,
                start + die2,
                start + die1 + die2
        };

        ArrayList<ArrayList<Checker>> boardst  = board.getBoard();
        String playerSymbol = player.getSymbol();

        for (int newPos : newPositions) {
            if (newPos >= 0 && newPos < boardst.size()) { // Check if within board bounds
                ArrayList<Checker> checkersAtNewPos = boardst.get(newPos);
                String opponentSymbol = playerSymbol.equals("X") ? "O" : "X";

                int opponentCheckerCount = 0;
                boolean isOwnedByPlayer = true;

                // Check if the position is valid (either empty or controlled by the player)
                for (Checker checker : checkersAtNewPos) {
                    if (checker.getPlayer().equals(opponentSymbol)) {
                        opponentCheckerCount++;
                        isOwnedByPlayer = false; // Position is not owned by the current player
                    }
                }

                if (checkersAtNewPos.isEmpty() || opponentCheckerCount <= 2 || isOwnedByPlayer) {
                    // 1 to represent board positions correctly
                    String moveDescription = "Initial position: " + start + ", Final position: " + (newPos + 1);
                    potentialLegalMoves.add(moveDescription);
                }
            }
        }

        return potentialLegalMoves; // Return potential moves (empty if none found)
    }
}


