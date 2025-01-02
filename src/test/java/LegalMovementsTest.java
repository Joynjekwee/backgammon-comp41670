
import java.util.*;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class LegalMovementsTest {

    private LegalMovements legalMovements;
    private Board board; // Updated to use the Board class
    private Bar bar;
    private BearOffArea bearOffArea;
    private Constants constants;
    private Player playerX;
    private Player playerO;

    @BeforeEach
    public void setUp() {
        // Initialize dependencies
        bar = new Bar();
        bearOffArea = new BearOffArea();
        constants = new Constants();

        // Initialize players
        playerX = new Player("PlayerX", constants.X);
        playerO = new Player("PlayerO", constants.O);

        // Initialize Board
        board = new Board();

        // Initialize LegalMovements with Board and dependencies
        legalMovements = new LegalMovements(board, bar, bearOffArea, constants);

        // Initialize bar and bear-off area
        bar.initialise();
        bearOffArea.initialise();
    }


    @Test
    public void testIsMoveValid_ValidMove() {
        // Set up the board
        Checker checker = new Checker(constants.X, 6);
        board.getBoardState().get(6).add(checker);

        // Check if the move is valid
        boolean result = legalMovements.isMoveValid(6, 8, playerX);
        assertTrue(result);
    }

    @Test
    public void testIsMoveValid_InvalidEndPosition() {
        // Set up the board
        Checker checker = new Checker(constants.X, 6);
        board.getBoardState().get(6).add(checker);

        // Check if the move to an invalid position is invalid
        boolean result = legalMovements.isMoveValid(6, 25, playerX);
        assertFalse(result);
    }

    @Test
    public void testIsMoveValid_InvalidStartPosition() {
        // No checker at the start position
        boolean result = legalMovements.isMoveValid(6, 8, playerX);
        assertFalse(result);
    }

    @Test
    public void testCanWeBearOff_Eligible() {
        // Set up the board
        for (int i = 1; i <= 6; i++) {
            board.getBoardState().get(i).add(new Checker(constants.X, i));
        }

        // Set dice values
        List<Integer> diceValues = Arrays.asList(1, 2);

        // Check if the player is eligible to bear off
        boolean result = legalMovements.canWeBearOff(playerX,diceValues);
        assertTrue(result);
    }

    @Test
    public void testCanWeBearOff_NotEligible() {
        // Set up the board with checkers outside the home area
        board.getBoardState().get(7).add(new Checker(constants.X, 7));
        // Set dice values
        List<Integer> diceValues = Arrays.asList(1, 2);

        // Check if the player is not eligible to bear off
        boolean result = legalMovements.canWeBearOff(playerX, diceValues);
        assertFalse(result);
    }

    @Test
    public void testCanWeMakeAMove_SingleDie() {
        // Set up the board
        Checker checker = new Checker(constants.X, 6);
        board.getBoardState().get(6).add(checker); // Add checker to position 6
        System.out.println("Checker at position 6: " + board.getBoardState().get(6));

        // Set dice values
        List<Integer> diceValues = Collections.singletonList(2); // Die value: 2
        System.out.println("Dice values: " + diceValues);

        // Call the method
        List<MoveOption> moves = legalMovements.canWeMakeAMove(6, diceValues, playerX);
        System.out.println("Generated moves: " + moves);

        // Assertions
        assertEquals(1, moves.size());
        assertEquals(4, moves.get(0).getEndPos()); // For player X, expected end position is 4
    }

    @Test
    public void testCanWeMakeAMove_Doubles() {
        // Set up the board
        Checker checker = new Checker(constants.X, 6);
        board.getBoardState().get(6).add(checker);

        // Set dice values for doubles
        List<Integer> diceValues = Arrays.asList(3, 3, 3, 3);

        // Call the method
        List<MoveOption> moves = legalMovements.canWeMakeAMove(6, diceValues, playerX);

        // Debugging output
        System.out.println("Generated moves for doubles: " + moves);

        // Expected moves
        List<MoveOption> expectedMoves = Arrays.asList(
                new MoveOption(6, 3, List.of(3)),           // Move 1: Use one die
                new MoveOption(6, 25, List.of(3, 3)),        // Move 2: Use two dice
                new MoveOption(6, 25, List.of(3, 3, 3)),    // Move 3: Use three dice
                new MoveOption(6, 25, List.of(3, 3, 3, 3))  // Move 4: Use all four dice
        );

        // Assert the expected number of unique moves
        assertEquals(expectedMoves.size(), moves.size(), "Expected 4 unique moves for doubles");

        // Assert that the generated moves match the expected moves
        for (MoveOption expectedMove : expectedMoves) {
            assertTrue(moves.contains(expectedMove), "Expected move missing: " + expectedMove);
        }
    }



    @Test
    public void testCanWeMoveCheckersToBoard_ValidMove() {
        // Set up the bar
        Checker checker = new Checker(constants.X, 0);
        bar.addToBar(constants.X, checker);

        // Set up dice values
        List<Integer> diceValues = Arrays.asList(5, 5); // Doubles

        // Call the method
        List<MoveOption> moves = legalMovements.canWeMoveCheckersToBoard(diceValues, playerX);

        // Log generated moves for debugging
        System.out.println("Generated moves: " + moves);

        // Expected moves
        List<MoveOption> expectedMoves = Arrays.asList(
                new MoveOption(0, 15, Arrays.asList(5, 5)), // Combined move using both dice
                new MoveOption(0, 20, Arrays.asList(5))     // Single move using one dice
        );

        // Ensure duplicates are removed
        Set<MoveOption> uniqueMoves = new HashSet<>(moves);
        assertEquals(uniqueMoves.size(), moves.size(), "Expected unique moves only");

        // Assert the expected number of unique moves
        assertEquals(expectedMoves.size(), moves.size(), "Expected unique move count");

        // Assert that the generated moves match the expected moves
        for (MoveOption expectedMove : expectedMoves) {
            assertTrue(moves.contains(expectedMove), "Expected move missing: " + expectedMove);
        }
    }



    @Test
    public void testCanWeMoveCheckersToBoard_InvalidMove() {
        // Set up the bar
        Checker checker = new Checker(constants.X, 0);
        bar.addToBar(constants.X, checker);

        // Check if the player cannot move a checker to an invalid position
        List<Integer> diceValues = Collections.singletonList(25);
        List<MoveOption> moves = legalMovements.canWeMoveCheckersToBoard(diceValues, playerX);

        assertTrue(moves.isEmpty());
    }
    @Test
    public void testGetListOfLegalMoves_WithValidMoves() {
        // Set up the board
        Checker checker1 = new Checker(constants.X, 1);
        Checker checker2 = new Checker(constants.X, 2);
        Checker checker4 = new Checker(constants.X, 4);
        Checker checker6 = new Checker(constants.X, 6);

        board.getBoardState().get(1).add(checker1);
        board.getBoardState().get(2).add(checker2);
        board.getBoardState().get(4).add(checker4);
        board.getBoardState().get(6).add(checker6);

        // Set dice values
        List<Integer> diceValues = Arrays.asList(1, 2);

        // Call the method
        List<MoveOption> actualMoves = legalMovements.getListOfLegalMoves(playerX, diceValues);

        // Manually add the expected moves
        List<MoveOption> expectedMoves = new ArrayList<>();
        expectedMoves.add(new MoveOption(2, 1, List.of(1)));
        expectedMoves.add(new MoveOption(4, 3, List.of(1)));
        expectedMoves.add(new MoveOption(6, 5, List.of(1)));
        expectedMoves.add(new MoveOption(4, 2, List.of(2)));
        expectedMoves.add(new MoveOption(6, 4, List.of(2)));
        expectedMoves.add(new MoveOption(4, 1, List.of(1, 2)));
        expectedMoves.add(new MoveOption(6, 3, List.of(1, 2)));
        expectedMoves.add(new MoveOption(1, 25, List.of(1))); // Bear-off move
        expectedMoves.add(new MoveOption(1, 25, List.of(2))); // Bear-off move
        expectedMoves.add(new MoveOption(2, 25, List.of(2))); // Bear-off move

        // Print the expected and actual moves for debugging
        System.out.println("Expected moves: " + expectedMoves);
        System.out.println("Actual moves: " + actualMoves);

        // Assert the expected number of moves
        assertEquals(expectedMoves.size(), actualMoves.size(), "Expected 10 legal moves (including duplicates)");

        // Assert that all expected moves are in the actual moves
        for (MoveOption expectedMove : expectedMoves) {
            assertTrue(actualMoves.contains(expectedMove), "Missing expected move: " + expectedMove);
        }
    }





    @Test
    public void testGetListOfLegalMoves_NoRegularMovesAvailable() {
        // Clear and set up the board
        for (int i = 1; i <= 24; i++) {
            board.getBoardState().get(i).clear(); // Ensure no checkers are left from other tests
        }

        // Place one checker for PlayerX at position 1
        board.getBoardState().get(1).add(new Checker(constants.X, 1));

        // Block positions 4 and 5 with opponent checkers
        board.getBoardState().get(4).add(new Checker(constants.O, 4));
        board.getBoardState().get(5).add(new Checker(constants.O, 5));

        // Use dice values that would result in blocked moves
        List<Integer> diceValues = Arrays.asList(3, 4);

        // Call getListOfLegalMoves
        List<MoveOption> moves = legalMovements.getListOfLegalMoves(playerX, diceValues);

        // Log the generated moves for debugging
        System.out.println("Generated moves: " + moves);

        // Verify that the only moves generated are bear-off moves
        for (MoveOption move : moves) {
            assertEquals(25, move.getEndPos(), "Unexpected non-bear-off move generated");
        }

        // Assert that moves exist only if bear-off is valid
        boolean canBearOff = board.canWeBearOff(playerX, diceValues);
        if (canBearOff) {
            assertFalse(moves.isEmpty(), "Expected only bear-off moves, but none were generated");
        } else {
            assertTrue(moves.isEmpty(), "Expected no legal moves, but some were returned");
        }
    }


}
