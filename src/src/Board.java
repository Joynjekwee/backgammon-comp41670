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

    public int getPipCount(Player player) {
        return pipCalculator.getPipCount(player);
    }

    public void display(Player player, int player1Score, int player2Score) {
        // Display the match score at the top
        System.out.println("Match Score: Player1(X) = " + player1Score + " | Player2(O) = " + player2Score);
        System.out.println(); // Line break for clarity
    }

    public void moveChecker(int start, int end) {

        if (board1.get(start).isEmpty()) {
            System.out.println("Invalid move: No checkers at the starting position.");
            return;
        }
        Checker check = board1.get(start).remove(0);
        board1.get(end).add(check);
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

    public void moveCheckerToBoard(ArrayList<Checker> barList,int end) {
        if(barList.isEmpty()){
            System.out.println("No checkers on the bar to move.");
            return;
        }
        Checker checker = barList.remove(0);
        board1.get(end).add(checker);
        System.out.println("Moved checker from bar to " + (end));
    }

    public void executeMove(int start, int end, Player player) {

        ArrayList<Checker> barX = bar.get("X");
        ArrayList<Checker> barO = bar.get("O");

        ArrayList<Checker> endCheckers = board1.get(end);
        ArrayList<Checker> startCheckers = board1.get(start);
        if(start == 1) {
            if(barX != null && !barX.isEmpty()) {
                if(barX.get(0).getSymbol().equals(player.getSymbol())) {
                    moveCheckerToBoard(barX,end);
                    return;
                }
            }

            if(barO != null && !barO.isEmpty()) {
                if(barO.get(0).getSymbol().equals(player.getSymbol())) {
                    moveCheckerToBoard(barO,end);
                    return;
                }
            }

        }
        // Check if move is bearing off
        boolean isBearingOff = false;
        if (player.getSymbol().equals("X") && end > 24) {
            isBearingOff = true;
        } else if (player.getSymbol().equals("O") && end < 1) {
            isBearingOff = true;
        }

        if(isBearingOff) {
            // Check if bearing off is allowed
            if(canWeBearOff(player)) {
                Checker checker = board1.get(start).remove(0);
                if(player.getSymbol().equals("X")) {
                    bearoffAreaPlayer1.add(checker);
                } else {
                    bearoffAreaPlayer2.add(checker);
                }
                System.out.println("Checker borne off by " + player.getName());
                return;
            } else {
                System.out.println("Cannot bear off yet. All checkers must be in home board.");
                return;
            }
        }



        if(endCheckers.size() == 1 && endCheckers.get(0).getPlayer() != startCheckers.get(0).getPlayer()) {
            Checker hitChecker = endCheckers.get(0);
            if(hitChecker.getSymbol().equals("X"))
                bar.get("X").add(hitChecker);
            else if (hitChecker.getSymbol().equals("O")) {
                bar.get("O").add(hitChecker);
            }
            board1.get(end).remove(hitChecker);
        }

        moveChecker(start, end);
    }


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
    }

    private void setupInitialBoard() {
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
            System.out.print("|   ");
        }
    }

    public ArrayList<Checker> getCurrentPlayerCheckers(Player player) {
        ArrayList<Checker> checkers = new ArrayList<>();
        for (ArrayList<Checker> point : board1.values()) {
            if (!point.isEmpty() && point.get(0).getSymbol().equals(player.getSymbol())) {
                checkers.add(point.get(0));
            }
        }
        return checkers;
    }

    public ArrayList<String> getListOfLegalMoves(Player player, ArrayList<Integer> diceValues){
        ArrayList<String> legalMoves = new ArrayList<>();
        ArrayList<String> legalmovesboard = new ArrayList<>();
        int die1 = diceValues.isEmpty()?0:diceValues.get(0);
        int die2 = diceValues.size()>1?diceValues.get(1):0;
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
    }


    public ArrayList<String> canWeMakeAMove(int start, int die1, int die2, Player player) {

        ArrayList<String> potentialLegalMoves = new ArrayList<>();
        int[] newPositions = {
                start + die1,
                start + die2,
                start + die1 + die2
        };

        String playerSymbol = player.getSymbol();

        for (int newPos : newPositions) {
            if (newPos >= 1 && newPos <= 24) {
                ArrayList<Checker> checkersAtNewPos = board1.get(newPos);
                String opponentSymbol = playerSymbol.equals("X") ? "O" : "X";

                int opponentCheckerCount = 0;
                boolean isOwnedByPlayer = true;

                for (Checker checker : checkersAtNewPos) {
                    if (checker.getPlayer().equals(opponentSymbol)) {
                        opponentCheckerCount++;
                        isOwnedByPlayer = false;
                    }
                }

                if (checkersAtNewPos.isEmpty() || opponentCheckerCount < 2 || isOwnedByPlayer) {
                    String moveDescription = "Initial position: " + (start ) + ", Final position: " + (newPos );
                    potentialLegalMoves.add(moveDescription);
                }
            }
        }

        return potentialLegalMoves;
    }

    public boolean areThereCheckersOnTheBar(Player player) {
        return !bar.getOrDefault(player.getSymbol(), new ArrayList<>()).isEmpty();
    }

    public ArrayList<String> canWeMoveCheckersToBoard(int die1, int die2, Player player) {
        ArrayList<String> potentialLegalMoves = new ArrayList<>();

        int[] newPositions = {
                die1,
                die2,
                die1 + die2
        };

        String playerSymbol = player.getSymbol();

        for (int newPos : newPositions) {
            if (newPos >= 1 && newPos <= 24) {
                ArrayList<Checker> checkersAtNewPos = board1.get(newPos);
                String opponentSymbol = playerSymbol.equals("X") ? "O" : "X";

                int opponentCheckerCount = 0;
                boolean isOwnedByPlayer = true;

                for (Checker checker : checkersAtNewPos) {
                    if (checker.getPlayer().equals(opponentSymbol)) {
                        opponentCheckerCount++;
                        isOwnedByPlayer = false;
                    }
                }

                if (checkersAtNewPos.isEmpty() || opponentCheckerCount < 2 || isOwnedByPlayer) {
                    String moveDescription = "Initial position: Bar, Final position: " + (newPos);
                    potentialLegalMoves.add(moveDescription);
                }
            }
        }
        return potentialLegalMoves;
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

        for (int i = 1; i <= 24; i++) {
            if (i < start || i > end) {
                for (Checker checker : boardPositions.get(i)) {
                    if (checker.getSymbol().equals(playerSymbol)) {
                        return false;
                    }
                }
            }
        }
        return true; // All checkers are in the home base
    }

    public boolean hasCheckerInHomeArea(Player loserPlayer, Player winnerPlayer) {
        int homeStart, homeEnd;

        if (winnerPlayer.getSymbol().equals("X")) {
            homeStart = 1;
            homeEnd = 6;
        } else {
            homeStart = 19;
            homeEnd = 24;
        }

        for (int i = homeStart; i <= homeEnd; i++) {
            List<Checker> checkersAtPosition = getCheckersAt(i);
            for (Checker checker : checkersAtPosition) {
                if (checker.getSymbol().equals(loserPlayer.getSymbol())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void printCheckerOnBar() {
        boolean printed = false;
        if(!bar.get("X").isEmpty() && bar.get("X").size() > countXOnBar) {
            System.out.print(bar.get("X").get(0).getSymbol() + "     ");
            printed = true;
        } else if (!bar.get("O").isEmpty() && bar.get("O").size() > countOOnBar) {
            System.out.print(bar.get("O").get(0).getSymbol() + "     ");
            printed = true;
        }

        if(!printed) {
            System.out.print("BAR   ");
        }
    }

    public void display(Player player) {

        countXOnBar = 0;
        countOOnBar = 0;

        pipCalculator.displayTopPipNumbers(player);
        int maxRowsNew = findCurrentMaxRows(); // Find max rows in board

        // Display the upper part of the board (points 13 to 24))
        for (int row = 0; row <= maxRowsNew-1; row++) {
            System.out.print(" ");
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

        // Display the lower part of the board (points 12 down to 1)
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
