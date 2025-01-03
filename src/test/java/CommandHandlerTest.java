import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.*;

public class CommandHandlerTest {
    private CommandHandler commandHandler;
    private Game game;
    private Player player1, player2;
    private Board board;
    private Dice dice;
    private Constants constants;

    @BeforeEach
    void setUp() {
        Player player1 = new Player("Alice", "X");
        Player player2 = new Player("Bob", "O");
        Board board = new Board();
        Dice dice = new Dice();
        Constants constants = new Constants();

        game = new Game(player1, player2, 5, dice, board, constants);

        commandHandler = new CommandHandler();

        // Register commands
        commandHandler.registerCommand("roll", new DiceCommand(game));
        commandHandler.registerCommand("hint", new HintCommand(game));
        commandHandler.registerCommand("moves", new MovesCommand(game));
        commandHandler.registerCommand("pip", new PipCommand(game));

    }

    @Test
    void testRegisterCommand() {
        Command testCommand = new TestCommand(game);

        commandHandler.registerCommand("test", testCommand);

        assertEquals(testCommand, commandHandler.getCommandMap().get("test"), "The registered command" +
                "should be added to the command map.");
    }

    @Test
    void testHandleInvalidCommand() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        commandHandler.handleCommand("invalidCommand");

        String output = outputStream.toString();
        assertTrue(output.contains("Invalid command! Please try again"), "Error message should be printed for invalid commands.");
    }

}
