import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Board {
    private Map<Integer, ArrayList<Checker>> board1;
    private Map<String, ArrayList<Checker>> bar;
    private Map<String, Integer> bearOff;
    private ArrayList<ArrayList<Checker>> board;
    private ArrayList<Checker> barX = new ArrayList<>();
    private ArrayList<Checker> barO = new ArrayList<>();
    private ArrayList<Checker> bearoffAreaPlayer1 = new ArrayList<>();
    private ArrayList<Checker> bearoffAreaPlayer2 = new ArrayList<>();

    private PipCalculator pipCalculator;
   // private Validation validate = new Validation(board);
    private int countXOnBar = 0;
    private int countOOnBar = 0;

    public Board() {
        board1 = new HashMap<>();
        bar = new HashMap<>();
        bearOff = new HashMap<>();
        pipCalculator = new PipCalculator(board1);
        initialize();
    }


    public Map<Integer, ArrayList<Checker>> getBoardState() {
        return board1; // Return the internal board representation
    }

    public List<Checker> getBar(String symbol) {
        return bar.getOrDefault(symbol, new ArrayList<>());
    }

    public int getBearOffCount(String symbol) {
        return bearOff.getOrDefault(symbol, 0);
    }

    public List<Checker> getCheckersAt(int position) {
        return board1.getOrDefault(position, new ArrayList<>());
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

    public ArrayList<Checker> getBearoffAreaPlayer1() {
        return bearoffAreaPlayer1;
    }

    public ArrayList<Checker> getBearoffAreaPlayer2() {
        return bearoffAreaPlayer2;
    }

    public void moveChecker(int start, int end) {

        if (board1.get(start).isEmpty()) {
            System.out.println("Invalid move: No checkers at the starting position.");
            return;
        }
        Checker check = board1.get(start).remove(0);
        board1.get(end).add(check);
      //  Checker checker = board.get(start).remove(0);
      //  board.get(end).add(checker);
        System.out.println("Moved checker from position " + (start) + " to " + (end));
    }

    public void moveCheckerToBar(String symbol, int position) {
        if (!board1.containsKey(position) || board1.get(position).isEmpty()) {
            System.out.println("Invalid move: No checkers at the specified position.");
            return;
        }

        Checker checker = board1.get(position).remove(0);
        bar.get(symbol).add(checker);

        System.out.println("Checker moved to bar: " + symbol);
    }

    public void moveCheckerToBoard(ArrayList<Checker>bar,int end) {
        Checker checker = bar.removeFirst();
        board1.get(end).add(checker);
        System.out.println("Moved checker from bar " + " to " + (end));
    }

    public void executeMove(int start, int end, Player player) {

        ArrayList<Checker> barX = bar.get("X");
        ArrayList<Checker> barO = bar.get("O");

        ArrayList<Checker> endCheckers = board1.get(end);
        ArrayList<Checker> startCheckers = board1.get(start);
        if(start == 1) {
           if(!barX.isEmpty()) {
               if(barX.getFirst().getSymbol().equals(player.getSymbol())) {
                   moveCheckerToBoard(barX,end);
                   return;
               }
           }

            if(!barO.isEmpty()) {
                if(barO.getFirst().getSymbol().equals(player.getSymbol())) {
                    moveCheckerToBoard(barO,end);
                    return;
                }
            }

        }


        if(endCheckers.size() == 1 && endCheckers.get(0).getPlayer() != startCheckers.get(0).getPlayer()) {
            Checker hitChecker = endCheckers.get(0);
            if(hitChecker.getSymbol() == "X")
                bar.get("X").add(hitChecker);
            else if (hitChecker.getSymbol() == "O") {
                bar.get("O").add(hitChecker);
            }
            board1.get(end).remove(hitChecker);
        }

        moveChecker(start, end);
    }


    // Initializes the board with some example checkers
    private void initialize() {
        board = new ArrayList<>();
        for (int i = 1; i <= 24; i++) {
            board.add(new ArrayList<>());
            board1.put(i, new ArrayList<>());
        }

        bar.put("X", new ArrayList<>());
        bar.put("O", new ArrayList<>());
        bearOff.put("X", 0);
        bearOff.put("O", 0);
        setupInitialBoard();
    }

    private void addChecker(int pointIndex, Checker checker) {
        if(pointIndex >= 1 && pointIndex <= 24) {
            board1.get(pointIndex).add(checker);
        } else {
            System.out.println("Invalid point index");
        }

       // board1.get(pointIndex+1).add(checker);
    }
    private void setupInitialBoard() {
        // Each loop is a counter for how many checkers should be added to each point on the board
        // Each point on the board is represented by an ArrayList
        for (int i = 0; i < 2; i++) {
            addChecker(1, new Checker("X",1));
            addChecker(24, new Checker("O",24));
        }

        for (int i = 0; i < 5; i++) {
            addChecker(12, new Checker("X",12));
            addChecker(13, new Checker("O",13));
            addChecker(19, new Checker("X",19));
            addChecker(6, new Checker("O",6));
        }

        for (int i = 0; i < 3; i++) {
            addChecker(17, new Checker("X",17));
            addChecker(8, new Checker("O",8));
        }
    }


// To find maximum number of checkers currently in a point to know the number of rows needed
    public int findCurrentMaxRows() {
        int maxRows = 0;
        for (ArrayList<Checker> point : board1.values()) {
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
        for (ArrayList<Checker> point : board1.values()) {
            if (point.size() != 0 && point.get(0).getSymbol().equals(player.getSymbol())) {
                checkers.add(point.getFirst());
            }

        }
        return checkers;
    }

    public ArrayList<String> getListOfLegalMoves(Player player, ArrayList<Integer> diceValues){
        ArrayList<String> legalMoves = new ArrayList<>();
        ArrayList<String> legalmovesboard = new ArrayList<>();
        int die1 = diceValues.get(0);
        int die2 = diceValues.get(1);
        for(Checker checker : getCurrentPlayerCheckers(player)){
            int startPoint =  checker.getPosition();
            if(areThereCheckersOnTheBar(player)) {
                if(canWeMoveCheckersToBoard(die1,die2,player).isEmpty()) {
                    legalmovesboard.add("-1");
                    return  legalmovesboard;
                }

                legalmovesboard.addAll(canWeMoveCheckersToBoard(die1,die2,player));
                return legalmovesboard;
            } else {
                legalMoves.addAll(canWeMakeAMove(startPoint, die1, die2, player));
            }

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
            if (newPos >= 1 && newPos <= 24) { // Check if within board bounds
                ArrayList<Checker> checkersAtNewPos = board1.get(newPos);
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
                    String moveDescription = "Initial position: " + (start ) + ", Final position: " + (newPos );
                    potentialLegalMoves.add(moveDescription);
                }
            }
        }


        return potentialLegalMoves; // Return potential moves (empty if none found)
    }

    // Check if there are checkers on the bar for a specific player
    public boolean areThereCheckersOnTheBar(Player player) {
        return !bar.getOrDefault(player.getSymbol(), new ArrayList<>()).isEmpty();
//        if (player.getSymbol().equals("X")) {
//            return !getBarX().isEmpty();
//        } else if (player.getSymbol().equals("O")) {
//            return !getBarO().isEmpty();
//        }
//        return false;
    }



    public ArrayList<String> canWeMoveCheckersToBoard(int die1, int die2, Player player) {
        ArrayList<String> potentialLegalMoves = new ArrayList<>();

               int[] newPositions = {
                       die1,
                       die2,
                       die1 + die2
               };

               //ArrayList<ArrayList<Checker>> boardst  = board.getBoard();
               String playerSymbol = player.getSymbol();

               for (int newPos : newPositions) {
                   if (newPos >= 0 && newPos < board1.size()) { // Check if within board bounds
                       ArrayList<Checker> checkersAtNewPos = board1.get(newPos);
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
                           String moveDescription = "Initial position: Bar" + ", Final position: " + (newPos);
                           potentialLegalMoves.add(moveDescription);
                       }
                   }
               }
               return potentialLegalMoves; // Return potential moves (empty if none found)
    }


    public boolean canWeBearOff(Player player) {
        Map<Integer,ArrayList<Checker>> boardPositions = this.getBoardState();
        String playerSymbol = player.getSymbol();
        int start, end;

        if (playerSymbol.equals("X")) {
            start = 1;
            end = 6;
        } else if (playerSymbol.equals("O")) {
            start = 19;
            end = 24;
        } else {
            return false; // Invalid symbol
        }

        // Check all points outside the home base
        for (int i = 0; i < boardPositions.size(); i++) {
            if (i < start || i > end) {
                for (Checker checker : boardPositions.get(i)) {
                    if (checker.getSymbol().equals(playerSymbol)) {
                        return false; // Found a checker outside the home base
                    }
                }
            }
        }
        return true; // All checkers are in the home base
    }


    public void printCheckerOnBar() {
        if(!bar.get("X").isEmpty() && bar.get("X").size() > countXOnBar) {
            countXOnBar += 1;
            System.out.print(bar.get("X").getFirst().getSymbol() + "     ");
        } else if (!bar.get("O").isEmpty() && bar.get("O").size() > countOOnBar) {
            countOOnBar += 1;
            System.out.print(bar.get("O").getFirst().getSymbol() + "     ");
        }
        else {
            System.out.print("BAR   ");
        }
    }

    public void display(Player player) {

        countXOnBar = 0;
        countOOnBar = 0;

        pipCalculator.displayTopPipNumbers(player);
        int maxRowsNew = findCurrentMaxRows(); // Find max rows in board

        // Display the upper part of the board (points 12 to 23)
        for (int row = 0; row <= maxRowsNew-1; row++) {
            System.out.print(" "); // Just for alignment
            for (int pointIndex = 13; pointIndex <= 18; pointIndex++) {
                printChecker(board1.get(pointIndex), row);
            }
            printCheckerOnBar();
            for (int pointIndex = 19; pointIndex <= 24; pointIndex++) {
                printChecker(board1.get(pointIndex), row);
            }
            System.out.println();
        }

        System.out.println();

        // Display the lower part of the board (points 0 to 11 in reverse order)
        for (int row = maxRowsNew - 1; row >= 0; row--) {
            System.out.print(" ");
            for (int pointIndex = 12; pointIndex >= 7; pointIndex--) {
                printChecker(board1.get(pointIndex), row);
            }
            printCheckerOnBar();
            for (int pointIndex = 6; pointIndex >= 1; pointIndex--) {
                printChecker(board1.get(pointIndex), row);
            }
            System.out.println();
        }


        pipCalculator.displayBottomPipNumbers(player);
        System.out.println("\n"); // New line after full board
    }




    public void displayTotalPipCounts(Player playerX, Player player0) {
        pipCalculator.displayTotalPipCounts(playerX, player0);

    }
}
