import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Handles the calculation and display of pip counts for players.
 * Pips represent the score based on checker positions on the board.
 */

public class PipCalculator {

    private Map<Integer, ArrayList<Checker>> board;
    private Constants constants;

    public PipCalculator(Map<Integer, ArrayList<Checker>> board) {
        this.board = board;
        this.constants = new Constants();
    }

    /**
     * Returns the pip number for a position based on the player's perspective.
     *
     * @param position the board position.
     * @param player   the player whose perspective is considered.
     * @return the pip number for the position.
     */

    private int getPipNumber(int position, Player player) {
        if (player.getSymbol().equals(constants.X)) {
            return position;
        } else {
            return 25 - position;
        }

    }

    private void displayPipNumbers(int start, int end, Player player, boolean reverse) {
        int count = 0;
        for (int i = start; (reverse ? i >= end : i <= end); i += (reverse ? -1 : 1)) {
            int pipNumber = getPipNumber(i, player);
            count++;
            if (pipNumber < 10) {
                System.out.print(constants.ZERO + pipNumber);
            } else {
                System.out.print(pipNumber);
            }
            if (count < 6) {
                System.out.print("--");
            }
        }
    }

    public void displayTopPipNumbers(Player player) {
        System.out.print(constants.SPACE);
        displayPipNumbers(13, 18, player, false);
        System.out.print("  BAR   ");
        displayPipNumbers(19, 24, player, false);
        System.out.print("  OFF");
        System.out.println();
    }

    public void displayBottomPipNumbers(Player player) {
        System.out.print(" ");
        displayPipNumbers(12, 7, player, true);
        System.out.print("  BAR   ");
        displayPipNumbers(6, 1, player, true);
        System.out.print("  OFF");
        System.out.println();
    }

    /**
     * Calculates the total pip count for the specified player.
     *
     * @param player the player whose pip count is calculated.
     * @return the total pip count.
     */
    public int calculateTotalPipCount(Player player) {
        int totalPipCount = 0;
        String playerSymbol = player.getSymbol();

        for (Map.Entry<Integer, ArrayList<Checker>> entry : board.entrySet()) {
            int position = entry.getKey();
            List<Checker> checkers = entry.getValue();
            int point = checkers.size();

            if (point > 0 && checkers.get(0).getSymbol().equals(playerSymbol)) {
                int pipScore = playerSymbol.equals("X") ? (position) * point : (25 - position) * point;
                totalPipCount += pipScore;
            }
        }

        return totalPipCount;
    }

    public int getPipCount(Player player) {
        return calculateTotalPipCount(player);
    }

    /**
     * Displays the total pip counts for both players.
     *
     * @param playerX the first player.
     * @param playerO the second player.
     */
    public void displayTotalPipCounts(Player playerX, Player playerO) {
        int totalPipCountX = calculateTotalPipCount(playerX);
        int totalPipCountO = calculateTotalPipCount(playerO);

        System.out.println("Total pip score for " + playerX.getName() + ": " + totalPipCountX);
        System.out.println("Total pip score for " + playerO.getName() + ": " + totalPipCountO);
    }
}
