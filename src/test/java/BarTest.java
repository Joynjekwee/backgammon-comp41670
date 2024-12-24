import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BarTest {
    private Bar bar;
    private final String X = "X";

    @Before
    public void setUp() {
        bar = new Bar();
    }

    @Test
    public void testAddToBar() {
        Checker checker = new Checker(X, 1);
        bar.addToBar(X, checker);
        List<Checker> checkers = bar.getCheckers(X);

        assertEquals(1, checkers.size());
        assertEquals(checker, checkers.get(0));
    }

    @Test
    public void testRemoveFromBar() {
        Checker checker = new Checker(X, 1);
        bar.addToBar(X, checker);
        Checker removedChecker = bar.removeFromBar(X);
        assertEquals(checker, removedChecker);
        assertTrue(bar.getCheckers(X).isEmpty());
    }

    @Test
    public void testRemoveFromEmptyBarThrowsException() {
        assertThrows(IllegalStateException.class, () -> bar.removeFromBar("X"));
    }
}
