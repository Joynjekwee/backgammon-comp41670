import java.util.*;

/**
 * Handles the rendering and display of the Backgammon board,
 * including bar, bear-off areas, and active board positions.
 */
public class BoardDisplay {
    private final Board board;
    private int countXOnBar = 0;
    private int countOOnBar = 0;

    public BoardDisplay(Board board) {
        this.board = board;
    }

    private PipCalculator getPipCalculator() {
        return new PipCalculator(board.getBoardState()); // Always operate on the latest board state
    }

    public static void displayMatchScore(Player player1, Player player2, int matchLength) {
        System.out.printf("Match Score: %s (%d points) vs %s (%d points) | First to %d points wins.\n",
                player1.getName(), player1.getScore(), player2.getName(), player2.getScore(), matchLength);
    }

    // Display pip numbers for the top row
    public void displayTopPipNumbers(Player player) {
        getPipCalculator().displayTopPipNumbers(player);
    }

    // Display pip numbers for the bottom row
    public void displayBottomPipNumbers(Player player) {
        getPipCalculator().displayBottomPipNumbers(player);
    }

    // Find maximum number of rows for checkers at any point
    public int findCurrentMaxRows() {
        int maxRows = 0;
        for (ArrayList<Checker> point : board.getBoardState().values()) {
            if (point.size() > maxRows) {
                maxRows = point.size();
            }
        }
        return maxRows;
    }

    public void printChecker(ArrayList<Checker> point, int row) {
        if (row < point.size()) {
            Checker checker = point.get(row);
            if (checker != null) {
                System.out.print(checker.getPlayer() + "   ");
            }
        } else {
            System.out.print("|   ");
        }
    }

    // Print checkers on the bar
    public void printCheckerOnBar() {
        List<Checker> barX = board.getBar("X");
        List<Checker> barO = board.getBar("O");
        if (!barX.isEmpty() && barX.size() > countXOnBar) {
            System.out.print(barX.get(countXOnBar).getSymbol() + "     ");
            countXOnBar++;
        } else if (!barO.isEmpty() && barO.size() > countOOnBar) {
            System.out.print(barO.get(countOOnBar).getSymbol() + "     ");
            countOOnBar++;
        } else {
            System.out.print("BAR   ");
        }
    }

    // Main display method
    public void display(Player player) {
        countXOnBar = 0;
        countOOnBar = 0;

        // Display pip numbers for the top row
        displayTopPipNumbers(player);
        int maxRowsNew = findCurrentMaxRows();

        // Display upper part of the board (points 13 to 24)
        for (int row = 0; row <= maxRowsNew - 1; row++) {
            System.out.print(" ");
            for (int pointIndex = 13; pointIndex <= 18; pointIndex++) {
                printChecker(board.getCheckersAt(pointIndex), row);
            }
            printCheckerOnBar();
            for (int pointIndex = 19; pointIndex <= 24; pointIndex++) {
                printChecker(board.getCheckersAt(pointIndex), row);
            }
            System.out.println();
        }

        System.out.println();

        // Display lower part of the board (points 12 to 1, reverse order)
        for (int row = maxRowsNew - 1; row >= 0; row--) {
            System.out.print(" ");
            for (int pointIndex = 12; pointIndex >= 7; pointIndex--) {
                printChecker(board.getCheckersAt(pointIndex), row);
            }
            printCheckerOnBar();
            for (int pointIndex = 6; pointIndex >= 1; pointIndex--) {
                printChecker(board.getCheckersAt(pointIndex), row);
            }
            System.out.println();
        }

        // Display pip numbers for the bottom row
        displayBottomPipNumbers(player);
        System.out.println("\n");

        // Display bear-off counts
        System.out.println("Player X bear-off count: " + board.getBearOffCount("X"));
        System.out.println("Player O bear-off count: " + board.getBearOffCount("O"));
    }


    public void displayDoublingStatus(DoublingCube doublingCube) {
        System.out.println("Current Stake: " + doublingCube.getStake());
        if (doublingCube.isDoublingOffered()) {
            System.out.println("Doubling offered by: " + doublingCube.getOwner().getName());
        } else {
            System.out.println("Doubling Cube is owned by: " + (doublingCube.getOwner() != null ? doublingCube.getOwner().getName() : "None"));
        }
    }
}
