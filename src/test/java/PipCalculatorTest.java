import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PipCalculatorTest {

    private PipCalculator pipCalculator;
    private Map<Integer, ArrayList<Checker>> board;
    private Player playerX;

    @Before
    public void setUp() {
        board = new HashMap<>();
        for (int i = 1; i <= 24; i++) {
            board.put(i, new ArrayList<>());
        }
        playerX = new Player("Alice", "X");
        pipCalculator = new PipCalculator(board);
    }

    @Test
    public void testCalculateTotalPipCount() {
        board.get(1).add(new Checker("X"));
        board.get(2).add(new Checker("X"));
        assertEquals(3, pipCalculator.calculateTotalPipCount(playerX));
    }

    @Test
    public void testGetPipCount() {
        board.get(1).add(new Checker("X"));
        assertEquals(1, pipCalculator.getPipCount(playerX));
    }

    @Test
    public void testEmptyBoard() {
        assertEquals(0, pipCalculator.calculateTotalPipCount(playerX));
    }

}
