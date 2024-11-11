import java.util.ArrayList;
import java.util.List;

public class Board {
    private ArrayList<ArrayList<Checker>> board;
    private List<Checker> barX = new ArrayList<>();
    private List<Checker> barO = new ArrayList<>();
    private List<Checker> offX = new ArrayList<>();
    private List<Checker> offO = new ArrayList<>();

    public Board() {
        initialize();
    }

    // Initializes the board with some example checkers
    private void initialize() {
        board = new ArrayList<>();
        for (int i = 0; i < 24; i++) {
            board.add(new ArrayList<>());
        }
        setupInitialBoard();
        // Initialise checker placement for X checkers
        // Array 0-indexed so positions from 0-23 instead of 1-24
    }

    private void addChecker(int pointIndex, Checker checker) {
        if(pointIndex >= 0 && pointIndex < board.size()) {
            board.get(pointIndex).add(checker);
        } else {
            System.out.println("Invalid point index");
        }
    }
    private void setupInitialBoard() {
        // Each loop is a counter for how many checkers should be added to each point on the board
        // Each point on the board is represented by an ArrayList
        for (int i = 0; i < 2; i++) {
            addChecker(0, new Checker("X"));
            addChecker(23, new Checker("O"));
        }

        for (int i = 0; i < 5; i++) {
            addChecker(11, new Checker("X"));
            addChecker(12, new Checker("O"));
            addChecker(18, new Checker("X"));
            addChecker(5, new Checker("O"));
        }

        for (int i = 0; i < 3; i++) {
            addChecker(16, new Checker("X"));
            addChecker(7, new Checker("O"));
        }
    }


// To find maximum number of checkers currently in a point to know the number of rows needed
    public int findCurrentMaxRows() {
        int maxRows = 0;
        for (ArrayList<Checker> point : board) {
            if (point != null && point.size() > maxRows) {
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
            System.out.print("|   "); // Empty spot if row exceeds the number of checkers at this point
        }
    }

    public void display() {
        System.out.println(" 13--14--15--16--17--18  BAR   19--20--21--22--23--24  OFF");

        int maxRowsNew = findCurrentMaxRows(); // Find max rows in board

        // Display the upper part of the board (points 12 to 23)
        for (int row = 0; row < maxRowsNew; row++) {
            System.out.print(" "); // Just for alignment
            for (int pointIndex = 12; pointIndex < 18; pointIndex++) {
                printChecker(board.get(pointIndex), row);
            }
            System.out.print("BAR   ");
            for (int pointIndex = 18; pointIndex < 24; pointIndex++) {
                printChecker(board.get(pointIndex), row);
            }
            System.out.println();
        }

        System.out.println();

        // Display the lower part of the board (points 0 to 11 in reverse order)
        for (int row = maxRowsNew - 1; row >= 0; row--) {
            System.out.print(" ");
            for (int pointIndex = 11; pointIndex >= 6; pointIndex--) {
                printChecker(board.get(pointIndex), row);
            }
            System.out.print("BAR   ");
            for (int pointIndex = 5; pointIndex >= 0; pointIndex--) {
                printChecker(board.get(pointIndex), row);
            }
            System.out.println();
        }

        System.out.println(" 12--11--10--09--08--07  BAR   06--05--04--03--02--01  OFF");
        System.out.println("\n"); // New line after full board
    }
}
