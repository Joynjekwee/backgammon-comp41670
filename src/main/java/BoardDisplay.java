import java.util.*;

/**
 * Handles the rendering and display of the Backgammon board,
 * including bar, bear-off areas, and active board positions.
 */
public class BoardDisplay {
    private final Board board;

    public BoardDisplay(Board board) {
        this.board = board;
    }


    public void display(Player player) {

       // countXOnBar = 0;
      //  countOOnBar = 0;

        displayPipCounts(player, true); // Bottom pip numbers (13-24)

        int maxRowsNew = findCurrentMaxRows(); // Find max rows in board

        // Display the upper part of the board (points 12 to 23)
        for (int row = 0; row <= maxRowsNew-1; row++) {
            System.out.print(" "); // Just for alignment
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

        // Display the lower part of the board (points 0 to 11 in reverse order)
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


        displayPipCounts(player, false); // Bottom pip numbers (13-24)
        System.out.println("\n"); // New line after full board
    }

    public void printCheckerOnBar() {
      /*  String symbol = player.getSymbol();
        List<Checker> barCheckers = board.getBar(symbol);
        if(!bar.get("X").isEmpty() && bar.get("X").size() > countXOnBar) {
            countXOnBar += 1;
            System.out.print(bar.get("X").getFirst().getSymbol() + "     ");
        } else if (!bar.get("O").isEmpty() && bar.get("O").size() > countOOnBar) {
            countOOnBar += 1;
            System.out.print(bar.get("O").getFirst().getSymbol() + "     ");
        }
        else {
            System.out.print("BAR   ");
        }*/
    }

    public void printChecker(ArrayList<Checker> point, int row) {
        if (row < point.size()) {
            Checker checker = point.get(row);
            if (checker != null) {
                System.out.print(checker.getPlayer() + "   ");
            }
        } else {
            System.out.print("|   "); // Empty spot if row exceeds the number of checkers at this point
        }
    }

    // To find maximum number of checkers currently in a point to know the number of rows needed
    public int findCurrentMaxRows() {
        int maxRows = 0;
        for (ArrayList<Checker> point : board.getBoardState().values()) {
            if (point != null && point.size() > maxRows) {
                maxRows = point.size();
            }
        }
        return maxRows;
    }


    private void displayPipCounts(Player player, boolean isTop) {
        PipCalculator pipCalculator = new PipCalculator(board.getBoardState());
        if (isTop) {
            pipCalculator.displayTopPipNumbers(player);
        } else {
            pipCalculator.displayBottomPipNumbers(player);
        }
    }

    public void displayTotalPipCounts(Player playerX, Player player0) {
        PipCalculator pipCalculator = new PipCalculator(board.getBoardState());
        pipCalculator.displayTotalPipCounts(playerX, player0);

    }

}
