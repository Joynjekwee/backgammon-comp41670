import java.util.ArrayList;
import java.util.List;

public class Board {
    private ArrayList<ArrayList<Checker>> board;
    private ArrayList<Checker> barX = new ArrayList<>();
    private ArrayList<Checker> barO = new ArrayList<>();
    private ArrayList<Checker> bearoffAreaPlayer1 = new ArrayList<>();
    private ArrayList<Checker> bearoffAreaPlayer2 = new ArrayList<>();
    private Validation validate = new Validation(board);
    private int countXOnBar = 0;
    private int countOOnBar = 0;

    public Board() {
        initialize();
    }

    public ArrayList<Checker> getBearoffAreaPlayer1() {
        return bearoffAreaPlayer1;
    }

    public ArrayList<Checker> getBearoffAreaPlayer2() {
        return bearoffAreaPlayer2;
    }

    public void moveChecker(int start, int end) {
        Checker checker = board.get(start).remove(0);
        board.get(end).add(checker);
        System.out.println("Moved checker from position " + (start + 1) + " to " + (end + 1));
    }

    public void executeMove(int start, int end) {
        ArrayList<Checker> endCheckers = board.get(end);
        ArrayList<Checker> startCheckers = board.get(start);

        if(endCheckers.size() == 1 && endCheckers.get(0).getPlayer() != startCheckers.get(0).getPlayer()) {
            Checker hitChecker = endCheckers.get(0);
            if(hitChecker.getSymbol() == "X")
                barX.add(hitChecker);
            else if (hitChecker.getSymbol() == "O") {
                barO.add(hitChecker);
            }
            board.get(end).remove(hitChecker);
        }

        moveChecker(start, end);
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
            addChecker(0, new Checker("X",0));
            addChecker(23, new Checker("O",23));
        }

        for (int i = 0; i < 5; i++) {
            addChecker(11, new Checker("X",11));
            addChecker(12, new Checker("O",12));
            addChecker(18, new Checker("X",18));
            addChecker(5, new Checker("O",5));
        }

        for (int i = 0; i < 3; i++) {
            addChecker(16, new Checker("X",16));
            addChecker(7, new Checker("O",7));
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
//LOOK AT THIS
    public ArrayList<Checker> getCurrentPlayerCheckers(Player player) {

        ArrayList<Checker> checkers = new ArrayList<>();

        for (ArrayList<Checker> point : board) {
            if (point.size() != 0 && point.get(0).getSymbol().equals(player.getSymbol())) {
                checkers.add(point.getFirst());
            }

        }
        return checkers;
    }

    public ArrayList<String> getListOfLegalMoves(Player player, ArrayList<Integer> diceValues){
        ArrayList<String> legalMoves = new ArrayList<>();
        int die1 = diceValues.get(0);
        int die2 = diceValues.get(1);
        for(Checker checker : getCurrentPlayerCheckers(player)){
            int startPoint =  checker.getPosition();
           // legalMoves = canWeMakeAMove(startPoint,die1,die2,player);
            //added to test
            legalMoves.addAll(canWeMakeAMove(startPoint, die1, die2, player));

        }

        return legalMoves;
        // valid.
    }




    public ArrayList<String> canWeMakeAMove(int start, int die1, int die2, Player player) {

        ArrayList<String> potentialLegalMoves = new ArrayList<>();
        int[] newPositions = {
                start + die1,
                start + die2,
                start + die1 + die2
        };

        //ArrayList<ArrayList<Checker>> boardst  = board.getBoard();
        String playerSymbol = player.getSymbol();

        for (int newPos : newPositions) {
            if (newPos >= 0 && newPos < board.size()) { // Check if within board bounds
                ArrayList<Checker> checkersAtNewPos = board.get(newPos);
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

                if (checkersAtNewPos.isEmpty() || opponentCheckerCount < 2 || isOwnedByPlayer) {
                    // 1 to represent board positions correctly
                    String moveDescription = "Initial position: " + (start + 1) + ", Final position: " + (newPos + 1);
                    potentialLegalMoves.add(moveDescription);
                }
            }
        }


        return potentialLegalMoves; // Return potential moves (empty if none found)
    }

    public void printCheckerOnBar() {
        if(barX.size() != 0 && barX.size() > countXOnBar) {
            countXOnBar += 1;
            System.out.print(barX.get(0).getSymbol() + "     ");
        } else if (barO.size() != 0 && barO.size() > countOOnBar) {
            countOOnBar += 1;
            System.out.print(barO.get(0).getSymbol() + "     ");
        }
        else {
            System.out.print("BAR   ");
        }
    }
    public void display() {

        countXOnBar = 0;
        countOOnBar = 0;

        System.out.println(" 13--14--15--16--17--18  BAR   19--20--21--22--23--24  OFF");

        int maxRowsNew = findCurrentMaxRows(); // Find max rows in board

        // Display the upper part of the board (points 12 to 23)
        for (int row = 0; row < maxRowsNew; row++) {
            System.out.print(" "); // Just for alignment
            for (int pointIndex = 12; pointIndex < 18; pointIndex++) {
                printChecker(board.get(pointIndex), row);
            }
            printCheckerOnBar();
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
            printCheckerOnBar();
            for (int pointIndex = 5; pointIndex >= 0; pointIndex--) {
                printChecker(board.get(pointIndex), row);
            }
            System.out.println();
        }

        System.out.println(" 12--11--10--09--08--07  BAR   06--05--04--03--02--01  OFF");
        System.out.println("\n"); // New line after full board
    }

    public ArrayList<Checker> getBarX() {
        return barX;
    }

    public ArrayList<Checker> getBarO(){
        return barO;
    }

    public ArrayList<ArrayList<Checker>> getBoard() {
        return board;
    }


    public void displayTotalPipCounts(Player playerX, Player player0) {
        int totalPipCountX = 0;
        int totalPipCountO = 0;

        for(int i = 0; i < board.size(); i++) {
            int point = board.get(i).size();
            if(point != 0) {
                int pipScore = (25 - (i + 1) * point);
                if(board.get(i).get(0).getSymbol().equals("X")) {
                    totalPipCountX += pipScore;
                } else if (board.get(i).get(0).getSymbol().equals("O")) {
                    totalPipCountO += pipScore;
                }
            }
        }

        System.out.println("Total pip score for " + playerX.getName() + ": " + totalPipCountX);
        System.out.println("Total pip score for " + player0.getName() + ": " + totalPipCountO);
    }
}
