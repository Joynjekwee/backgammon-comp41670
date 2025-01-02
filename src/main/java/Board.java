import java.util.*;

/**
 * Represents the Backgammon board, which manages the state of all positions
 * and checkers during the game.
 */

public class Board {
    private Map<Integer, ArrayList<Checker>> board;
    private Bar bar;
    private BearOffArea bearOffArea;
    private MoveExecution moveExecution;
    private LegalMovements legalMovements;
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
        moveExecution = new MoveExecution(board, bar, bearOffArea); // Initialize MoveExecution
        c = new Constants();
        legalMovements = new LegalMovements(this, bar, bearOffArea, c); // Initialize LegalMovements instance
        initialize();
    }

    // Initializes the board with some example checkers
    private void initialize() {
        for (int i = 1; i <= 24; i++) {
            board.put(i, new ArrayList<>());
        }
        bar.initialise();
        bearOffArea.initialise();
        setupTestBearOffBoard();
        // setupInitialBoard();
    }

    public void reset() {
        // Clear the board and set up initial positions
        board.clear();
        bar.reset();
        bearOffArea.reset();
        initialize();
        System.out.println("Board reset for a new game.");
    }

    /**
     * Adds a checker to a specific position on the board.
     *
     * @param checker the checker to add
     */

    protected void addChecker(int position, Checker checker) {
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
        System.out.println();
        System.out.println("Match Score: Player1(X) = " + player1Score + " | Player2(O) = " + player2Score);

    }

    public boolean isMoveValid(int start, int end, Player player) {
        return legalMovements.isMoveValid(start, end, player);
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
        moveExecution.executeMove(start, end, player, c);
    }

    public ArrayList<Checker> getCurrentPlayerCheckers(Player player) {
        ArrayList<Checker> checkers = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Checker>> entry : board.entrySet()) {
            ArrayList<Checker> point = entry.getValue();
            for (Checker checker : point) { // Loop through all checkers at this position
                if (checker.getSymbol().equals(player.getSymbol())) {
                    checkers.add(checker);
                }
            }
        }
        return checkers;
    }

    public boolean areThereCheckersOnTheBar(Player player) {
        return !bar.getCheckers(player.getSymbol()).isEmpty();
    }

    public List<MoveOption> getListOfLegalMoves(Player player, List<Integer> diceValues) {
        return legalMovements.getListOfLegalMoves(player, diceValues);
    }

    public List<MoveOption> canWeMakeAMove(int start, List<Integer> diceValues, Player player) {
        return legalMovements.canWeMakeAMove(start, diceValues, player);
    }

    public List<MoveOption> canWeMoveCheckersToBoard(List<Integer> diceValues, Player player) {
        return legalMovements.canWeMoveCheckersToBoard(diceValues, player);
    }

    public boolean canWeBearOff(Player player, List<Integer> diceValues) {
        return legalMovements.canWeBearOff(player, diceValues);
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
        System.out.println("Checking home area for winnerPlayer: " + winnerPlayer.getSymbol());
        System.out.println("Home area range: " + homeStart + " to " + homeEnd);

        for (int i = homeStart; i <= homeEnd; i++) {
            List<Checker> checkersAtPosition = getCheckersAt(i);
            System.out.println("Position " + i + " contains: " + checkersAtPosition);
            for (Checker checker : checkersAtPosition) {
                if (checker.getSymbol().equals(loserPlayer.getSymbol())) {
                    System.out.println(
                            "Found a checker for loserPlayer: " + loserPlayer.getSymbol() + " at position " + i);
                    return true;
                }
            }
        }
        return false;
    }

    public void display(Player player, DoublingCube doublingCube) {
        boardDisplay.display(player, doublingCube);
    }

    public void displayTotalPipCounts(Player playerX, Player player0) {
        pipCalculator.displayTotalPipCounts(playerX, player0);
    }
}
