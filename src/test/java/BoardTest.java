import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class BoardTest {
    private Board board;
    private Player playerX;
    private Player playerO;

    @Before
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
}
