import java.util.*;

/**
 * Represents the Backgammon board, which manages the state of all positions
 * and checkers during the game.
 */

public class Board {
    private Map<Integer, ArrayList<Checker>> board;
    private Map<String, Integer> bearOff;
    private Bar bar;
    private BearOffArea bearOffArea;
    private PipCalculator pipCalculator;
    private int countXOnBar = 0;
    private int countOOnBar = 0;
    private Constants c;
    private BoardDisplay boardDisplay;
    // NOTE ENCAPSULATE BAR AND BEAROFF INTO OWN CLASS

    /**
     * Initializes an empty board with all positions set up.
     */

    public Board() {
        board = new HashMap<>();
        pipCalculator = new PipCalculator(board);
        boardDisplay = new BoardDisplay(this);
        bar = new Bar();
        bearOffArea = new BearOffArea();
        bearOff = new HashMap<>();
        c = new Constants();
        initialize();
    }

    // Initializes the board with some example checkers
    private void initialize() {
        for (int i = 1; i <= 24; i++) {
            board.put(i, new ArrayList<>());
        }
        bearOff.put(c.X, 0);
        bearOff.put(c.O, 0);
        setupTestBearOffBoard();
        // setupInitialBoard();
    }

    public void reset() {
        // Clear the board and set up initial positions
        board.clear();
        setupInitialBoard();
        System.out.println("Board reset for a new game.");
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
            addChecker(24, new Checker(c.X, 24));
            addChecker(1, new Checker(c.O, 1));
        }

        for (int i = 0; i < 5; i++) {
            addChecker(13, new Checker(c.X, 13));
            addChecker(12, new Checker(c.O, 12));
            addChecker(6, new Checker(c.X, 6));
            addChecker(19, new Checker(c.O, 19));
        }

        for (int i = 0; i < 3; i++) {
            addChecker(8, new Checker(c.X, 8));
            addChecker(17, new Checker(c.O, 17));
        }
    }

    private void setupTestBearOffBoard() {
        for (int i = 0; i < 5; i++) {
            addChecker(24, new Checker(c.O, 24));
            addChecker(1, new Checker(c.X, 1));
            addChecker(23, new Checker(c.O, 23));
            addChecker(2, new Checker(c.X, 2));
            addChecker(21, new Checker(c.O, 21));
            addChecker(4, new Checker(c.X, 4));
        }
    }

    public Map<Integer, ArrayList<Checker>> getBoardState() {
        return board; // Return the internal board representation
    }

    public List<Checker> getBar(String symbol) {
        return bar.getCheckers(symbol);
    }

    public ArrayList<Checker> getCheckersAt(int position) {
        return board.getOrDefault(position, new ArrayList<>());
    }

    // Bear-off related methods
    public void bearOffChecker(String symbol) {
        // Increment bear-off count using BearOffArea
        bearOffArea.addChecker(symbol);

    }

    public int getBearOffCount(String symbol) {
        // Delegate bear-off count retrieval to BearOffArea
        return bearOffArea.getBearOffCount(symbol);
    }

    public int getPipCount(Player player) {
        return pipCalculator.getPipCount(player);
    }

    public void displayScore(Player player, int player1Score, int player2Score) {
        // Display the match score at the top
        System.out.println("Match Score: Player1(X) = " + player1Score + " | Player2(O) = " + player2Score);
        System.out.println(); // Line break for clarity
    }

    public boolean isMoveValid(int start, int end, Player player) {
        if (end < 1 || end > 24) {
            return false;
        }

        if (start != 0) {
            ArrayList<Checker> startCheckers = board.getOrDefault(start, new ArrayList<>());
            if (startCheckers.isEmpty() || !startCheckers.get(0).getSymbol().equals(player.getSymbol())) {
                return false;
            }
        } else {
            List<Checker> barCheckers = bar.getCheckers(player.getSymbol());
            // ArrayList<Checker> barCheckers = bar.get(player.getSymbol());
            if (barCheckers == null || barCheckers.isEmpty()) {
                return false;
            }
        }

        ArrayList<Checker> endCheckers = board.getOrDefault(end, new ArrayList<>());
        String opponentSymbol = player.getSymbol().equals(c.X) ? c.O : c.X;
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
    }

    public void moveCheckerToBar(String symbol, int position) {
        if (!board.containsKey(position) || board.get(position).isEmpty()) {
            System.out.println("Invalid move: No checkers at the specified position.");
            return;
        }

        Checker checker = board.get(position).remove(0);
        bar.addToBar(symbol, checker);
        // System.out.println("Checker moved to bar: " + symbol);
    }

    public void moveCheckerToBoard(String symbol, int end) {
        Checker checker = bar.removeFromBar(symbol);
        board.get(end).add(checker);
        System.out.println("Moved checker from bar " + " to " + (end));
    }

    public void executeMove(int start, int end, Player player) {
        List<Checker> barX = bar.getCheckers(c.X);
        List<Checker> barO = bar.getCheckers(c.O);

        ArrayList<Checker> endCheckers = board.get(end);
        ArrayList<Checker> startCheckers = (start == 0 || !board.containsKey(start)) ? null : board.get(start);

        if (start == 0) { // Moving from the bar
            try {
                moveCheckerToBoard(player.getSymbol(), end);
                return;
            } catch (IllegalStateException e) {
                System.out.println("No checkers on the bar to move");
                return;
            }
        }

        if (end == 25) { // Handle bear-off
            if (!board.get(start).isEmpty() && board.get(start).get(0).getSymbol().equals(player.getSymbol())) {
                Checker checker = board.get(start).remove(0);
                bearOffChecker(player.getSymbol()); // Increment the bear-off count

            } else {
                throw new IllegalStateException("No checkers at position ");
            }
            return;
        }

        // Hitting opponent's checker
        if (endCheckers != null && endCheckers.size() == 1 && startCheckers != null && !startCheckers.isEmpty() &&
                endCheckers.get(0).getPlayer() != startCheckers.get(0).getPlayer()) {
            Checker hitChecker = endCheckers.get(0);
            bar.addToBar(hitChecker.getSymbol(), hitChecker);
            endCheckers.remove(hitChecker);
        }

        moveChecker(start, end);
    }

    public ArrayList<Checker> getCurrentPlayerCheckers(Player player) {
        ArrayList<Checker> checkers = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Checker>> entry : board.entrySet()) {
            ArrayList<Checker> point = entry.getValue();
            if (!point.isEmpty() && point.get(0).getSymbol().equals(player.getSymbol())) {
                checkers.add(point.get(0));
            }
        }
        return checkers;
    }

    public boolean areThereCheckersOnTheBar(Player player) {
        return !bar.getCheckers(player.getSymbol()).isEmpty();
    }

    public List<MoveOption> getListOfLegalMoves(Player player, List<Integer> diceValues) {
        if (areThereCheckersOnTheBar(player)) {
            return canWeMoveCheckersToBoard(diceValues, player);
        }

        Set<MoveOption> moves = new HashSet<>();
        for (Checker checker : getCurrentPlayerCheckers(player)) {
            moves.addAll(canWeMakeAMove(checker.getPosition(), diceValues, player));
        }

        if (canWeBearOff(player)) { // Check if the player is eligible to bear off
            int homeStart = player.getSymbol().equals("X") ? 1 : 19;
            int homeEnd = player.getSymbol().equals("X") ? 6 : 24;

            for (Checker checker : getCurrentPlayerCheckers(player)) {
                int position = checker.getPosition();
                if (position >= homeStart && position <= homeEnd) {
                    for (int die : diceValues) {
                        if (position - die < homeStart || position + die > homeEnd) { // Bear-off condition
                            moves.add(new MoveOption(position, 25, List.of(die))); // Use 25 as the special endPos for
                                                                                   // bear-off
                        }
                    }
                }
            }
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
        int direction = playerSymbol.equals(c.X) ? -1 : +1;

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
        int direction = playerSymbol.equals(c.X) ? -1 : +1;

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

        if (playerSymbol.equals(c.X)) {
            start = 1;
            end = 6;
        } else if (playerSymbol.equals(c.O)) {
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

    public boolean hasCheckerInHomeArea(Player loserPlayer, Player winnerPlayer) {
        int homeStart, homeEnd;

        if (winnerPlayer.getSymbol().equals(c.X)) {
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

    public void display(Player player) {
        boardDisplay.display(player);
    }

    public void displayDoublingCube(DoublingCube doublingCube) {boardDisplay.displayDoublingStatus(doublingCube);}

    public void displayTotalPipCounts(Player playerX, Player player0) {
        pipCalculator.displayTotalPipCounts(playerX, player0);
    }
}
