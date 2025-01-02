
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {
    private Board board;
    private Player playerX;
    private Player playerO;

    @BeforeEach
    public void setUp() {
        board = new Board();
        playerO = new Player("Teju", "X");
        playerX = new Player("Bob", "O");
    }

    @Test
    public void testInitialisation() {
        assertEquals(5, board.getCheckersAt(13).size());
    }

    @Test
    public void testMoveChecker() {
        board.moveChecker(1, 5);
        assertEquals(1, board.getCheckersAt(5).size());
        assertEquals(1, board.getCheckersAt(1).size());
        assertEquals("O", board.getCheckersAt(5).get(0).getSymbol());
    }


    @Test
    public void testBearOffChecker() {
        board.bearOffChecker("O");
        assertEquals(1, board.getBearOffCount("O"));
        board.bearOffChecker("X");
        assertEquals(1, board.getBearOffCount("X"));
    }

    @Test
    public void testMoveCheckerToBar() {
        board.moveCheckerToBar("X", 19);
        assertEquals(1, board.getBar("X").size());
        assertEquals(4, board.getCheckersAt(19).size());
    }

    @Test
    public void testMoveCheckerToBoard() {
        board.moveCheckerToBar("X", 19);
        board.moveCheckerToBoard("X", 23);
        assertEquals(1, board.getCheckersAt(23).size());
        assertTrue(board.getBar("X").isEmpty());
    }

    @Test
    public void testGetCurrentPlayerCheckers() {
        // Reset the board to a known state
        board.reset();

        // Debug: Print board state for Player X
        System.out.println("Debug: Board state after reset (Player X positions):");
        for (int i = 1; i <= 24; i++) {
            List<Checker> checkersAtPosition = board.getCheckersAt(i);
            if (!checkersAtPosition.isEmpty() && checkersAtPosition.get(0).getSymbol().equals("X")) {
                System.out.println("Position " + i + ": " + checkersAtPosition);
            }
        }

        // Get the current player's checkers
        ArrayList<Checker> checkersX = board.getCurrentPlayerCheckers(playerX);

        // Debug: Print retrieved checkers
        System.out.println("Debug: Retrieved checkers for Player X: " + checkersX);

        // Assert the expected number of checkers
        assertEquals(15, checkersX.size(), "Player X should have 15 checkers.");
    }


    @Test
    public void testAreThereCheckersOnTheBar() {
        // Reset the board to ensure a clean state
        board.reset();

        // Add a checker directly to the bar for Player X
        Checker checkerX = new Checker("X", 0); // Checker for Player X
        Checker checker0 = new Checker("0", 0); // Checker for Player X
        board.getBar("X").add(checkerX); // Add the checker to the bar
        board.getBar("O").add(checker0); // Add the checker to the bar

        // Debugging output to check the state of the bar
        System.out.println("Bar for Player X: " + board.getBar("X"));
        System.out.println("Bar for Player O: " + board.getBar("O"));

        // Verify that Player X has a checker on the bar
        assertTrue(board.areThereCheckersOnTheBar(playerX), "Player X should have a checker on the bar.");
    }


    @Test
    public void testHasCheckerInHomeArea() {
        // Reset the board
        board.reset();

        // Explicitly set up Player X's checkers in their home area (positions 1–6)
        for (int i = 1; i <= 6; i++) {
            board.getCheckersAt(i).clear();
            board.getCheckersAt(i).add(new Checker("X", i));
        }

        // Explicitly set up Player O's checkers outside Player X's home area
        for (int i = 19; i <= 24; i++) {
            board.getCheckersAt(i).clear();
            board.getCheckersAt(i).add(new Checker("O", i));
        }

        // Debugging output
        System.out.println("Board State for Player X's Home Area:");
        for (int i = 1; i <= 6; i++) {
            System.out.println("Position " + i + ": " + board.getCheckersAt(i));
        }
        System.out.println("Board State for Player O's Checkers:");
        for (int i = 19; i <= 24; i++) {
            System.out.println("Position " + i + ": " + board.getCheckersAt(i));
        }

        // Run assertions
        assertTrue(board.hasCheckerInHomeArea(playerX, playerX), "Player X should have checkers in their home area.");
        assertFalse(board.hasCheckerInHomeArea(playerX, playerO), "Player O should not have checkers in Player X's home area.");
    }



    @Test
    public void testDisplayScore() {
        // Test displaying the score (no assertions since it's visual output)
        board.displayScore(playerX, 5, 3);
    }

    @Test
    public void testGetBoardState() {
        Map<Integer, ArrayList<Checker>> boardState = board.getBoardState();
        assertNotNull(boardState);
        assertEquals(24, boardState.size());
    }

    @Test
    public void testReset() {
        board.reset();
        assertEquals(5, board.getCheckersAt(13).size());
        assertEquals(0, board.getBar("X").size());
        assertEquals(0, board.getBearOffCount("X"));
    }

}
