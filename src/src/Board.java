import java.util.ArrayList;

public class Board {
    private ArrayList<ArrayList<Checker>> board;
    private ArrayList<Checker> barX = new ArrayList<>();
    private ArrayList<Checker> barO = new ArrayList<>();
    private ArrayList<Checker> bearoffAreaPlayer1 = new ArrayList<>();
    private ArrayList<Checker> bearoffAreaPlayer2 = new ArrayList<>();
   // private Validation validate = new Validation(board);
    private int countXOnBar = 0;
    private int countOOnBar = 0;

    public Board() {
        initialize();
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
        Checker checker = board.get(start).remove(0);
        board.get(end).add(checker);
        System.out.println("Moved checker from position " + (start + 1) + " to " + (end + 1));
    }

    public void moveCheckerToBoard(ArrayList<Checker> bar,int end) {
        Checker checker = bar.remove(0);
        board.get(end).add(checker);
        System.out.println("Moved checker from bar " + " to " + (end + 1));
    }
    public void executeMove(int start, int end, Player player) {

        if(start == 0) {
            ArrayList<Checker> endCheckers = board.get(end);
           if(!barO.isEmpty()) {
               if(barO.getFirst().getSymbol().equals(player.getSymbol())) {
                   moveCheckerToBoard(barO,end);
                   return;
               }
           }

           if(!barX.isEmpty()) {
            if(barX.getFirst().getSymbol().equals(player.getSymbol())) {
                moveCheckerToBoard(barX, end);
                return;
            }
           }

        }
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

    public ArrayList<String> getListOfLegalMoves(Player player, ArrayList<Integer> diceValues) {
        ArrayList<String> legalMoves = new ArrayList<>();
        ArrayList<Integer> usedDice = new ArrayList<>(); // To track used dice

        for (Checker checker : getCurrentPlayerCheckers(player)) {
            int startPoint = checker.getPosition();

            // Iterate through dice for single-die moves
            for (int die : diceValues) {
                if (!usedDice.contains(die)) { // Skip dice already used
                    legalMoves.addAll(canWeMakeAMove(startPoint, die, player));
                }
            }

            // Handle combined-dice moves
            if (diceValues.size() == 2) {
                int combinedDie = diceValues.get(0) + diceValues.get(1);
                if (!usedDice.contains(combinedDie)) { // Skip combined dice if used
                    legalMoves.addAll(canWeMakeAMove(startPoint, combinedDie, player));
                }
            }
        }
        return legalMoves;
    }





    public ArrayList<String> canWeMakeAMove(int start, int dieValue, Player player) {
        ArrayList<String> potentialLegalMoves = new ArrayList<>();
        int newPos = start + dieValue;

        if (newPos >= 0 && newPos < board.size()) { // Check if within board bounds
            ArrayList<Checker> checkersAtNewPos = board.get(newPos);
            String playerSymbol = player.getSymbol();
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
                String moveDescription = "Initial position: " + (start + 1) + ", Final position: " + (newPos + 1);
                potentialLegalMoves.add(moveDescription);
            }
        }

        return potentialLegalMoves; // Return potential moves (empty if none found)
    }

    // Check if there are checkers on the bar for a specific player
    public boolean areThereCheckersOnTheBar(Player player) {
        if (player.getSymbol().equals("X")) {
            return !getBarX().isEmpty();
        } else if (player.getSymbol().equals("O")) {
            return !getBarO().isEmpty();
        }
        return false;
    }



    public ArrayList<String> canWeMoveCheckersToBoard(ArrayList<Integer> dieValues, Player player) {
        ArrayList<String> potentialLegalMoves = new ArrayList<>();
        String playerSymbol = player.getSymbol();

        for (int dieValue : dieValues) {
            int newPos = dieValue - 1; // Convert to zero-based index if necessary

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
                    String moveDescription = "Initial position: Bar" + ", Final position: " + (newPos + 1);
                    potentialLegalMoves.add(moveDescription);
                }
            }
        }

        return potentialLegalMoves; // Return potential moves (empty if none found)
    }


    /*public boolean canWeBearOff(Board board, Player player) {

        if (player.getSymbol().equals("X")) {

        }

        if (player.getSymbol().equals("O")) {

        }

        return false;

    }*/

    public boolean canWeBearOff(Player player) {
        ArrayList<ArrayList<Checker>> boardPositions = this.getBoard();
        String playerSymbol = player.getSymbol();
        int start, end;

        if (playerSymbol.equals("X")) {
            start = 0;
            end = 5;
        } else if (playerSymbol.equals("O")) {
            start = 18;
            end = 23;
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

    private int getPipNumber(int position, Player player) {
        if(player.getSymbol().equals("X")) {
            return position + 1;
        } else {
            return 24 - position;
        }
    }

    public void displayTopPipNumbers(Player player) {
        System.out.print(" ");
        int pipNumber;
        int count = 0;
        for (int i = 12; i < 18; i++) {
             pipNumber = getPipNumber(i, player);
            count += 1;
            if(pipNumber < 10)
                System.out.print("0" + pipNumber);
            else
                System.out.print(pipNumber);
            if(count < 6) {
                System.out.print("--");
            }
        }
        System.out.print("  BAR   ");
        count = 0;
        for (int i = 18; i < 24; i++) {
             pipNumber = getPipNumber(i, player);
            count += 1;
            //System.out.print(pipNumber);
            if(pipNumber < 10)
                System.out.print("0" + pipNumber);
            else
                System.out.print(pipNumber);
            if(count < 6) {
                System.out.print("--");
            }
        }
        System.out.print("  OFF");
        System.out.println();
    }

    public void displayBottomPipNumbers(Player player) {
        System.out.print(" ");
        int count = 0;
        int pipNumber;
        for (int i = 11; i >= 6; i--) {
             pipNumber = getPipNumber(i, player);
            count += 1;
            if(pipNumber < 10)
                System.out.print("0" + pipNumber);
            else
                System.out.print(pipNumber);
            if(count < 6) {
                System.out.print("--");
            }
        }
        System.out.print("  BAR   ");
        count = 0;
        for (int i = 5; i >= 0; i--) {
             pipNumber = getPipNumber(i, player);
            count += 1;
           // System.out.print(pipNumber);
            if(pipNumber < 10)
                System.out.print("0" + pipNumber);
            else
                System.out.print(pipNumber);
            if(count < 6) {
                System.out.print("--");
            }
        }
        System.out.print("  OFF");
        System.out.println();
    }

    public void display(Player player) {

        countXOnBar = 0;
        countOOnBar = 0;

       // System.out.println(" 13--14--15--16--17--18  BAR   19--20--21--22--23--24  OFF");
        displayTopPipNumbers(player);
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

        //System.out.println(" 12--11--10--09--08--07  BAR   06--05--04--03--02--01  OFF");
        displayBottomPipNumbers( player);
        System.out.println("\n"); // New line after full board
    }




    public void displayTotalPipCounts(Player playerX, Player player0) {
        int totalPipCountX = 0;
        int totalPipCountO = 0;

        for(int i = 0; i < board.size(); i++) {
            int point = board.get(i).size();
            if(point != 0) {
                int pipScoreX = (24 - i) * point; // Player X's perspective (24 to 1)
                int pipScoreO = (i + 1) * point;   // Player O's perspective (1 to 24)

                if(board.get(i).get(0).getSymbol().equals("X")) {
                    totalPipCountX += pipScoreX;
                } else if (board.get(i).get(0).getSymbol().equals("O")) {
                    totalPipCountO += pipScoreO;
                }
            }
        }

        System.out.println("Total pip score for " + playerX.getName() + ": " + totalPipCountX);
        System.out.println("Total pip score for " + player0.getName() + ": " + totalPipCountO);
    }
}
