
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class MoveExecutionTest {

    private Map<Integer, ArrayList<Checker>> board;
    private Bar bar;
    private BearOffArea bearOffArea;
    private MoveExecution moveExecution;
    private Constants constants;
    private Player playerX;
    private Player playerO;

    @BeforeEach
    public void setUp() {
        // Initialize dependencies
        board = new HashMap<>();
        bar = new Bar();
        bearOffArea = new BearOffArea();
        constants = new Constants();

        // Initialize players
        playerX = new Player("PlayerX", constants.X);
        playerO = new Player("PlayerO", constants.O);

        // Initialize board positions
        for (int i = 1; i <= 24; i++) {
            board.put(i, new ArrayList<>());
        }

        // Initialize MoveExecution
        moveExecution = new MoveExecution(board, bar, bearOffArea);
    }

    @Test
    public void testMoveCheckerNormally() {
        // Set up the board
        Checker checker = new Checker(constants.X, 6);
        board.get(6).add(checker);

        // Execute the move
        moveExecution.executeMove(6, 8, playerX, constants);

        // Assertions
        assertTrue(board.get(6).isEmpty());
        assertEquals(1, board.get(8).size());
        assertEquals(constants.X, board.get(8).get(0).getSymbol());
    }

    @Test
    public void testMoveCheckerFromBar() {
        // Set up the bar
        Checker checker = new Checker(constants.X, 0);
        bar.addToBar(constants.X, checker);

        // Execute the move
        moveExecution.moveFromBar(constants.X, 6);

        // Assertions
        assertTrue(bar.getCheckers(constants.X).isEmpty());
        assertEquals(1, board.get(6).size());
        assertEquals(constants.X, board.get(6).get(0).getSymbol());
    }

    @Test
    public void testBearOffChecker() {
        // Set up the board
        Checker checker = new Checker(constants.X, 6);
        board.get(6).add(checker);

        // Execute the bear-off
        moveExecution.executeBearOff(6, playerX);

        // Assertions
        assertTrue(board.get(6).isEmpty());
        assertEquals(1, bearOffArea.getBearOffCount(constants.X));
    }

    @Test
    public void testHitOpponentChecker() {
        // Set up the board
        Checker opponentChecker = new Checker(constants.O, 8);
        board.get(8).add(opponentChecker);

        Checker playerChecker = new Checker(constants.X, 6);
        board.get(6).add(playerChecker);

        // Execute the move (hitting the opponent)
        moveExecution.executeMove(6, 8, playerX, constants);

        // Assertions
        assertEquals(1, board.get(8).size());
        assertEquals(constants.X, board.get(8).get(0).getSymbol());
        assertEquals(1, bar.getCheckers(constants.O).size());
    }

    @Test
    public void testMoveFromEmptyBarFails() {
        // Expect an IllegalStateException when trying to move a checker from an empty bar
        assertThrows(IllegalStateException.class, () -> {
            moveExecution.moveFromBar(constants.X, 6);
        });
    }

    @Test
    public void testInvalidBearOffFails() {
        assertThrows(IllegalStateException.class, () -> {
            moveExecution.executeBearOff(6, playerX);
        });
    }
}

