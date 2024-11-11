import java.util.ArrayList;
import java.util.List;

public class Board {
    private Checker[] board;
    private List<Checker> barX = new ArrayList<>();
    private List<Checker> barO = new ArrayList<>();
    private List<Checker> offX = new ArrayList<>();
    private List<Checker> offO = new ArrayList<>();

    public Board() {
        board = new Checker[24];
        initialize();
    }

    // Initializes the board with some example checkers
    private void initialize() {
        for (int i = 0; i < 24; i++) {
            board[i] = null;  // Null means no checkers on this point
        }

        // Initialise checker placement for X checkers
        board[0] = new Checker("X", 2);   // 2 X checkers on point 1
        board[11] = new Checker("X", 5);  // 5 X checkers on point 12
        board[16] = new Checker("X", 3);  // 3 X checkers on point 17
        board[18] = new Checker("X", 5);  // 5 X checkers on point 19

        // Initialise checker placement for O checkers
        board[23] = new Checker("O", 2);  // 2 O checkers on point 24
        board[12] = new Checker("O", 5);  // 5 O checkers on point 13
        board[7] = new Checker("O", 3);   // 3 O checkers on point 8
        board[5] = new Checker("O", 5);   // 5 O checkers on point 6
    }

    public void display() {
        System.out.println(" 13--14--15--16--17--18  BAR   19--20--21--22--23--24  OFF");

        int maxRows = findMaxRows(board);
        for (int j = 0; j <= maxRows; j++) {
            System.out.print(" ");
            for (int i = 12; i < 18; i++) {
                System.out.print(getCheckerDisplay(board[i], j) + "   ");
            }
            System.out.print("BAR   ");
            for (int i = 18; i < 24; i++) {
                System.out.print(getCheckerDisplay(board[i], j) + "   ");
            }
            System.out.println();
        }

        System.out.println();

        for (int j = maxRows; j >= 0; j--) {
            System.out.print(" ");
            for (int i = 11; i >= 6; i--) {
                System.out.print(getCheckerDisplay(board[i], j) + "   ");
            }
            System.out.print("BAR   ");
            for (int i = 5; i >= 0; i--) {
                System.out.print(getCheckerDisplay(board[i], j) + "   ");
            }
            System.out.println();
        }

        System.out.println(" 12--11--10--09--08--07  BAR   06--05--04--03--02--01  OFF");
        System.out.println("\n"); // New line after full board

    }

    private int findMaxRows(Checker[] board) {
        int maxRows = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != null && board[i].getCount() > maxRows)
                maxRows = board[i].getCount();
        }
        return maxRows;
    }

    private String getCheckerDisplay(Checker checker, int row) {
        if (checker == null || row >= checker.getCount()) {
            return "|";
        }
        return checker.getSymbol();
    }

    public void displayPipCounts(String playerNameX, String playerNameO) {
        List<String> scoresX = new ArrayList<>();
        List<String> scoresO = new ArrayList<>();
        int totalPipScoreX = 0;
        int totalPipScoreO = 0;

        for (int i = 0; i < board.length; i++) {
            if (board[i] != null) {
                int coneNumber = 24 - i; // Calculate cone number from index
                int pipScore = coneNumber * board[i].getCount(); // Calculate the pip score

                String scoreDetail = "Cone " + coneNumber + " (" + board[i].getCount() + " checkers) -> " + pipScore;

                if (board[i].getPlayer().equals("X")) {
                    scoresX.add(scoreDetail);
                    totalPipScoreX += pipScore; // Accumulate total score for X
                } else if (board[i].getPlayer().equals("O")) {
                    scoresO.add(scoreDetail);
                    totalPipScoreO += pipScore; // Accumulate total score for O
                }
            }
        }

        // Display scores for X
        System.out.println("\nPip scores for " + playerNameX + ":");
        scoresX.forEach(System.out::println);
        System.out.println("Total pip score for " + playerNameX + ": " + totalPipScoreX);

        // Display scores for O
        System.out.println("\nPip scores for " + playerNameO + ":");
        scoresO.forEach(System.out::println);
        System.out.println("Total pip score for " + playerNameO + ": " + totalPipScoreO);
    }
    public void displayTotalPipCounts(String playerNameX, String playerNameO) {
        int totalPipScoreX = 0;
        int totalPipScoreO = 0;

        for (int i = 0; i < board.length; i++) {
            if (board[i] != null) {
                int pipScore = (25 - (i + 1)) * board[i].getCount();
                if (board[i].getPlayer().equals("X")) {
                    totalPipScoreX += pipScore;
                } else if (board[i].getPlayer().equals("O")) {
                    totalPipScoreO += pipScore;
                }
            }
        }

        System.out.println("Total pip score for " + playerNameX + ": " + totalPipScoreX);
        System.out.println("Total pip score for " + playerNameO + ": " + totalPipScoreO);
    }

}


