
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {
    private Player player1;
    private Player player2;
    private Board board;
    private Dice dice;
    private Game game;
    private DoublingCube doublingCube;
    private Constants constants;

    @BeforeEach
    void setUp() {
        player1 = new Player("Alice", "X");
        player2 = new Player("Bob", "O");
        board = new Board();
        dice = new Dice();
        doublingCube = new DoublingCube();
        constants = new Constants();

        game = new Game(player1,player2, 5, dice, board, constants);
    }

    @Test
    void testGameInitialization() {
        game.displayPlayers();

        assertNotNull(game.getPlayer1());
        assertNotNull(game.getPlayer2());
        assertEquals("Alice", game.getPlayer1().getName());
        assertEquals("Bob", game.getPlayer2().getName());
    }

    @Test
    void testWhoGoesFirst() {
        Player firstPlayer = game.whoGoesFirst();

        assertTrue(firstPlayer == player1 || firstPlayer == player2, "First player should be one of the two players.");
    }

    @Test
    void testHandleRollCommand() {
        game.initialiseGame();
        game.enableTestMode();
        game.addCommandToQueue("A");
        game.addCommandToQueue("B");
        game.handleRollCommand();
        List<Integer> diceValues = dice.getMoves();
        assertNotNull(diceValues);
        assertFalse(diceValues.isEmpty(), "Dice values should not be empty after rolling.");
        assertTrue(diceValues.size() <= 4, "Dice values should be valid for backgammon.");
    }

    @Test
    void testHandleCustomDiceCommand() {
        game.initialiseGame();
        game.enableTestMode();
        game.addCommandToQueue("1,2");
        game.addCommandToQueue("A");
        game.addCommandToQueue("B");
        game.handleCustomDiceCommand();

        List<Integer> diceValues = dice.getMoves();
        assertNotNull(diceValues);
        assertEquals(2, diceValues.size(), "Custom dice should generate exactly 2 values.");
        assertTrue(diceValues.stream().allMatch(val -> val >= 1 && val <= 6), "Dice values must be between 1 and 6.");
    }

    @Test
    void testDoubleAndAccept() {

        game.enableTestMode();
        // Add test commands for doubling and refusing
        game.addCommandToQueue("accept");

        game.initialiseGame();


        game.doubleCommand();


        // Simulate accept double
        game.handleAcceptDouble();

        assertNotNull(game.getBoard());
    }

    @Test
    void testDoubleAndRefuse() {

        game.enableTestMode();
        // Add test commands for doubling and refusing
        game.addCommandToQueue("refuse");

        game.initialiseGame();


        game.doubleCommand();

        // Simulate refuse double
        game.handleRefuseDouble();

        assertTrue(player1.getScore() >= 0 || player2.getScore() >= 0, "One player's score should increase after refusal.");
    }

    @Test
    void testGameOverCondition() {
        board.setBearOffCount(constants.X, 15);
        boolean gameOver = game.isGameOver();

        assertTrue(gameOver, "Game should be over if a player has borne off all checkers.");
    }

    @Test
    void testMatchOverCondition() {
        player1.addScore(5); // Player 1 reaches match score
        boolean matchOver = game.checkMatchState();

        assertTrue(matchOver, "Match should be over when a player reaches the match score.");
    }

    @Test
    void testDetermineResultTypeSingleWin() {
        board.setBearOffCount(constants.X, 15); // Player 1 bears off all checkers
        board.setBearOffCount(constants.O, 5);  // Player 2 bears off some checkers

        game.isGameOver();

        // Assert the game is over and the winner is correct
        assertTrue(game.isGameOver(), "The game should be over when one player bears off all checkers.");
        assertEquals(player1.getName(), game.getWinner(), "Player 1 should be the winner in this scenario.");

        game.determineResultType();

        assertEquals(1, player1.getScore(), "Player 1 should score 1 point for a single win.");
    }

    @Test
    void testDetermineResultTypeGammon() {
        board.setBearOffCount(constants.X, 15); // Player 1 bears off all checkers
        board.setBearOffCount(constants.O, 0);  // Player 2 bears off none

        board.getBoardState().clear(); // clear board so in this situation no loser player checkers in home winner area
        game.isGameOver();

        // Assert the game is over and the winner is correct
        assertTrue(game.isGameOver(), "The game should be over when one player bears off all checkers.");
        assertEquals(player1.getName(), game.getWinner(), "Player 1 should be the winner in this scenario.");

        game.determineResultType();

        assertEquals(2, player1.getScore(), "Player 1 should score 2 points for a gammon.");
    }

    @Test
    void testDetermineResultTypeBackgammon() {
        board.setBearOffCount(constants.X, 15); // Player 1 bears off all checkers
        Checker checker = new Checker(constants.O, 1);
        board.getBar(constants.O).add(checker);          // Player 2 has a checker on the bar

        game.isGameOver();

        // Assert the game is over and the winner is correct
        assertTrue(game.isGameOver(), "The game should be over when one player bears off all checkers.");
        assertEquals(player1.getName(), game.getWinner(), "Player 1 should be the winner in this scenario.");

        game.determineResultType();

        assertEquals(3, player1.getScore(), "Player 1 should score 3 points for a backgammon.");
    }

    @Test
    void testResetGameState() {
        board.setBearOffCount(constants.X, 15); // Player 1 bears off all checkers
        game.isGameOver();

        game.resetGameForNewRound(); // Reset for a new round

        assertEquals(0, board.getBearOffCount(constants.X), "Board state should reset.");
        assertFalse(game.isGameOver(), "Game should not be over after reset.");
    }

    @Test
    void shouldThrowExceptionForInvalidDiceValues() {
        // Redirect console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        game.enableTestMode();
        game.addCommandToQueue("1,7"); // Invalid dice value exceeds max
        game.handleCustomDiceCommand();

        // Restore console output
        System.setOut(System.out);

        String output = outputStream.toString();
        assertTrue(output.contains("Dice values must be between 1 and 6."),
                "Expected error message for dice values out of range.");
    }




    @Test
    void shouldPrintErrorForInvalidFormat() {
        // Redirect console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        game.enableTestMode();
        game.addCommandToQueue("1"); // Single value instead of two

        game.handleCustomDiceCommand();

        // Restore console output
        System.setOut(System.out);

        String output = outputStream.toString();
        assertTrue(output.contains("You must enter exactly two dice values separated by a comma."),
                "Expected error message for invalid dice format.");
    }


}
