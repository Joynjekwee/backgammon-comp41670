import java.util.*;
import java.util.Map;

/**
 * Represents the Backgammon board, which manages the state of all positions
 * and checkers during the game.
 */

public class Board {
    private Map<Integer, ArrayList<Checker>> board;
    private Map<String, ArrayList<Checker>> bar;
    private Map<String, Integer> bearOff;
    private ArrayList<Checker> barX = new ArrayList<>();
    private ArrayList<Checker> barO = new ArrayList<>();
    private ArrayList<Checker> bearoffAreaPlayer1 = new ArrayList<>();
    private ArrayList<Checker> bearoffAreaPlayer2 = new ArrayList<>();

    private PipCalculator pipCalculator;
    private int countXOnBar = 0;
    private int countOOnBar = 0;

    // NOTE ENCAPSULATE BAR AND BEAROFF INTO OWN CLASS

    /**
     * Initializes an empty board with all positions set up.
     */

    public Board() {
        board = new HashMap<>();
        bar = new HashMap<>();
        bearOff = new HashMap<>();
        pipCalculator = new PipCalculator(board);
        initialize();
    }

    // Initializes the board with some example checkers
    private void initialize() {
        for (int i = 1; i <= 24; i++) {
            board.put(i, new ArrayList<>());
        }
        bar.put("X", new ArrayList<>());
        bar.put("O", new ArrayList<>());
        bearOff.put("X", 0);
        bearOff.put("O", 0);
        setupInitialBoard();
    }

    /**
     * Adds a checker to a specific position on the board.
     *
     * @param checker the checker to add
     */

    private void addChecker(int position, Checker checker) {
        if (!board.containsKey(position)) {
            throw new IllegalArgumentException("Invalid position: " + position);
        }
        board.get(position).add(checker);
    }

    private void setupInitialBoard() {
        // Each loop is a counter for how many checkers should be added to each point on
        // the board
        // Each point on the board is represented by an ArrayList
        for (int i = 0; i < 2; i++) {
            addChecker(24, new Checker("X", 24));
            addChecker(1, new Checker("O", 1));
        }

        for (int i = 0; i < 5; i++) {
            addChecker(13, new Checker("X", 13));
            addChecker(12, new Checker("O", 12));
            addChecker(6, new Checker("X", 6));
            addChecker(19, new Checker("O", 19));
        }

        for (int i = 0; i < 3; i++) {
            addChecker(8, new Checker("X", 8));
            addChecker(17, new Checker("O", 17));
        }
    }

    public Map<Integer, ArrayList<Checker>> getBoardState() {
        return board; // Return the internal board representation
    }

    public int getBearOffCount(String symbol) {
        return bearOff.getOrDefault(symbol, 0);
    }

    public ArrayList<Checker> getCheckersAt(int position) {
        return board.getOrDefault(position, new ArrayList<>());
    }

    public ArrayList<Checker> getBearoffAreaPlayer1() {
        return bearoffAreaPlayer1;
    }

    public ArrayList<Checker> getBearoffAreaPlayer2() {
        return bearoffAreaPlayer2;
    }

    public boolean isMoveValid(int start, int end, Player player) {
        if (end < 1 || end > 24) {
            return false;
        }

        if (start != 0) {
            ArrayList<Checker> startCheckers = board1.getOrDefault(start, new ArrayList<>());
            if (startCheckers.isEmpty() || !startCheckers.get(0).getSymbol().equals(player.getSymbol())) {
                return false;
            }
        } else {
            ArrayList<Checker> barCheckers = bar.get(player.getSymbol());
            if (barCheckers == null || barCheckers.isEmpty()) {
                return false;
            }
        }

        ArrayList<Checker> endCheckers = board1.getOrDefault(end, new ArrayList<>());
        String opponentSymbol = player.getSymbol().equals("X") ? "O" : "X";
        int opponentCheckerCount = 0;
        for (Checker c : endCheckers) {
            if (c.getSymbol().equals(opponentSymbol)) {
                opponentCheckerCount++;
            }
        }

        return opponentCheckerCount <= 1;
    }

    public void moveChecker(int start, int end) {

        if (!board.containsKey(start) || !board.containsKey(end)) {
            throw new IllegalArgumentException("Invalid board position");
        }
        if (board.get(start).isEmpty()) {
            throw new IllegalStateException("No checkers at starting position.");
        }
        Checker check = board.get(start).remove(0);
        board.get(end).add(check);
        System.out.println("Moved checker from position " + (start) + " to " + (end));
    }

    public void moveCheckerToBar(String symbol, int position) {
        if (!board.containsKey(position) || board.get(position).isEmpty()) {
            System.out.println("Invalid move: No checkers at the specified position.");
            return;
        }

        Checker checker = board.get(position).remove(0);
        bar.get(symbol).add(checker);
    }

    public void moveCheckerToBoard(ArrayList<Checker> bar, int end) {
        Checker checker = bar.removeFirst();
        board.get(end).add(checker);
        System.out.println("Moved checker from bar " + " to " + (end));
    }

    public void executeMove(int start, int end, Player player) {
        ArrayList<Checker> barX = bar.get("X");
        ArrayList<Checker> barO = bar.get("O");

        ArrayList<Checker> endCheckers = board1.get(end);
        ArrayList<Checker> startCheckers = (start == 0 || !board1.containsKey(start)) ? null : board1.get(start);

        if (start == 0) {
            if (!barX.isEmpty() && barX.get(0).getSymbol().equals(player.getSymbol())) {
                moveCheckerToBoard(barX, end);
                return;
            }
            if (!barO.isEmpty() && barO.get(0).getSymbol().equals(player.getSymbol())) {
                moveCheckerToBoard(barO, end);
                return;
            }
        }

        // Hitting opponent's checker
        if (endCheckers != null && endCheckers.size() == 1 && startCheckers != null && !startCheckers.isEmpty() &&
                endCheckers.get(0).getPlayer() != startCheckers.get(0).getPlayer()) {
            Checker hitChecker = endCheckers.get(0);
            if (hitChecker.getSymbol().equals("X"))
                bar.get("X").add(hitChecker);
            else if (hitChecker.getSymbol().equals("O")) {
                bar.get("O").add(hitChecker);
            }
            endCheckers.remove(hitChecker);
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
        if (pointIndex >= 1 && pointIndex <= 24) {
            board1.get(pointIndex).add(checker);
        } else {
            System.out.println("Invalid point index");
        }

        // board1.get(pointIndex+1).add(checker);
    }

    private void setupInitialBoard() {
        // Each loop is a counter for how many checkers should be added to each point on
        // the board
        // Each point on the board is represented by an ArrayList
        for (int i = 0; i < 2; i++) {
            addChecker(1, new Checker("X", 1));
            addChecker(24, new Checker("O", 24));
        }

        for (int i = 0; i < 5; i++) {
            addChecker(12, new Checker("X", 12));
            addChecker(13, new Checker("O", 13));
            addChecker(19, new Checker("X", 19));
            addChecker(6, new Checker("O", 6));
        }

        for (int i = 0; i < 3; i++) {
            addChecker(17, new Checker("X", 17));
            addChecker(8, new Checker("O", 8));
        }
    }

    // To find maximum number of checkers currently in a point to know the number of
    // rows needed
    public int findCurrentMaxRows() {
        int maxRows = 0;
        for (ArrayList<Checker> point : board1.values()) {
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

    public ArrayList<Checker> getCurrentPlayerCheckers(Player player) {
        ArrayList<Checker> checkers = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Checker>> entry : board1.entrySet()) {
            ArrayList<Checker> point = entry.getValue();
            if (!point.isEmpty() && point.get(0).getSymbol().equals(player.getSymbol())) {
                checkers.add(point.get(0));
            }
        }
        return checkers;
    }

    public boolean areThereCheckersOnTheBar(Player player) {
        return !bar.getOrDefault(player.getSymbol(), new ArrayList<>()).isEmpty();
    }

    public List<MoveOption> getListOfLegalMoves(Player player, List<Integer> diceValues) {
        if (areThereCheckersOnTheBar(player)) {
            return canWeMoveCheckersToBoard(diceValues, player);
        }

        Set<MoveOption> moves = new HashSet<>();
        for (Checker checker : getCurrentPlayerCheckers(player)) {
            moves.addAll(canWeMakeAMove(checker.getPosition(), diceValues, player));
        }
        return new ArrayList<>(moves);
    }

    private boolean isDoubles(List<Integer> diceValues) {
        if (diceValues.size() != 4)
            return false;
        int first = diceValues.get(0);
        for (int d : diceValues) {
            if (d != first)
                return false;
        }
        return true;
    }

    private List<Integer> generateDiceUsedForSum(int d, int sum) {
        // For doubles: if sum=9 and d=3, we have three dice [3,3,3]
        List<Integer> used = new ArrayList<>();
        int count = sum / d;
        for (int i = 0; i < count; i++) {
            used.add(d);
        }
        return used;
    }

    public List<MoveOption> canWeMakeAMove(int start, List<Integer> diceValues, Player player) {
        Set<MoveOption> potentialMoves = new HashSet<>();
        String playerSymbol = player.getSymbol();
        int direction = playerSymbol.equals("X") ? +1 : -1;

        if (isDoubles(diceValues)) {
            int d = diceValues.get(0);
            // doubles: consider 1d, 2d, 3d, 4d
            int[] sums = { d, d * 2, d * 3, d * 4 };
            for (int sum : sums) {
                int newPos = start + (direction * sum);
                if (isMoveValid(start, newPos, player)) {
                    potentialMoves.add(new MoveOption(start, newPos, generateDiceUsedForSum(d, sum)));
                }
            }
        } else {
            // Not doubles: normal scenario
            // Single die moves
            for (int die : diceValues) {
                int newPos = start + (direction * die);
                if (isMoveValid(start, newPos, player)) {
                    potentialMoves.add(new MoveOption(start, newPos, List.of(die)));
                }
            }

            // Two dice moves (if we have at least 2 dice)
            if (diceValues.size() >= 2) {
                for (int i = 0; i < diceValues.size(); i++) {
                    for (int j = i + 1; j < diceValues.size(); j++) {
                        int sum = diceValues.get(i) + diceValues.get(j);
                        int newPos = start + (direction * sum);
                        if (isMoveValid(start, newPos, player)) {
                            List<Integer> diceUsed = Arrays.asList(diceValues.get(i), diceValues.get(j));
                            // Sort for consistency in equals/hashCode if needed
                            potentialMoves.add(new MoveOption(start, newPos, diceUsed));
                        }
                    }
                }
            }
        }

        return new ArrayList<>(potentialMoves);
    }

    public List<MoveOption> canWeMoveCheckersToBoard(List<Integer> diceValues, Player player) {
        Set<MoveOption> movesFromBar = new HashSet<>();
        String playerSymbol = player.getSymbol();
        int direction = playerSymbol.equals("X") ? +1 : -1;

        if (isDoubles(diceValues)) {
            int d = diceValues.get(0);
            int[] sums = { d, d * 2, d * 3, d * 4 };
            for (int sum : sums) {
                int newPos = (direction > 0) ? sum : (25 - sum);
                if (isMoveValid(0, newPos, player)) {
                    movesFromBar.add(new MoveOption(0, newPos, generateDiceUsedForSum(d, sum)));
                }
            }
        } else {
            // Single die moves
            for (int die : diceValues) {
                int newPos = (direction > 0) ? die : (25 - die);
                if (isMoveValid(0, newPos, player)) {
                    movesFromBar.add(new MoveOption(0, newPos, List.of(die)));
                }
            }

            // Two dice moves if non-doubles and at least 2 dice
            if (diceValues.size() >= 2) {
                for (int i = 0; i < diceValues.size(); i++) {
                    for (int j = i + 1; j < diceValues.size(); j++) {
                        int sum = diceValues.get(i) + diceValues.get(j);
                        int newPos = (direction > 0) ? sum : (25 - sum);
                        if (isMoveValid(0, newPos, player)) {
                            List<Integer> diceUsed = Arrays.asList(diceValues.get(i), diceValues.get(j));
                            movesFromBar.add(new MoveOption(0, newPos, diceUsed));
                        }
                    }
                }
            }
        }

        return new ArrayList<>(movesFromBar);
    }

    public boolean canWeBearOff(Player player) {
        Map<Integer, ArrayList<Checker>> boardPositions = this.getBoardState();
        String playerSymbol = player.getSymbol();
        int start, end;

        if (playerSymbol.equals("X")) {
            start = 1;
            end = 6;
        } else if (playerSymbol.equals("O")) {
            start = 19;
            end = 24;
        } else {
            return false;
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
        return true;
    }

    public void printCheckerOnBar() {
        if (!bar.get("X").isEmpty() && bar.get("X").size() > countXOnBar) {
            countXOnBar += 1;
            System.out.print(bar.get("X").get(0).getSymbol() + "     ");
        } else if (!bar.get("O").isEmpty() && bar.get("O").size() > countOOnBar) {
            countOOnBar += 1;
            System.out.print(bar.get("O").get(0).getSymbol() + "     ");
        } else {
            System.out.print("BAR   ");
        }
    }

    public void display(Player player) {
        countXOnBar = 0;
        countOOnBar = 0;

        // Display pip numbers for the top row
        pipCalculator.displayTopPipNumbers(player);
        int maxRowsNew = findCurrentMaxRows();

        // Display upper part of the board (points 13 to 24)
        for (int row = 0; row <= maxRowsNew - 1; row++) {
            System.out.print(" ");
            for (int pointIndex = 13; pointIndex <= 18; pointIndex++) {
                printChecker(board.get(pointIndex), row);
            }
            printCheckerOnBar();
            for (int pointIndex = 19; pointIndex <= 24; pointIndex++) {
                printChecker(board.get(pointIndex), row);
            }
            System.out.println();
        }

        System.out.println();

        // Display lower part of the board (points 12 to 1, reverse order)
        for (int row = maxRowsNew - 1; row >= 0; row--) {
            System.out.print(" ");
            for (int pointIndex = 12; pointIndex >= 7; pointIndex--) {
                printChecker(board.get(pointIndex), row);
            }
            printCheckerOnBar();
            for (int pointIndex = 6; pointIndex >= 1; pointIndex--) {
                printChecker(board.get(pointIndex), row);
            }
            System.out.println();
        }

        // Display pip numbers for the bottom row
        pipCalculator.displayBottomPipNumbers(player);
        System.out.println("\n");
    }

    public void displayTotalPipCounts(Player playerX, Player player0) {
        pipCalculator.displayTotalPipCounts(playerX, player0);
    }
}
