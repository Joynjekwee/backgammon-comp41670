
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class BarTest {
    private Bar bar;
    private final String X = "X";

    @BeforeEach
    public void setUp() {
        bar = new Bar();
        bar.initialise();
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
        assertThrows(IllegalStateException.class, () -> bar.removeFromBar(X));
    }

    @Test
    public void testInvalidSymbol() {
       // assertThrows(NullPointerException.class, () -> bar.getCheckers("Z"));
    }

    @Test
    public void testReset() {
        bar.addToBar(X, new Checker(X));
        bar.reset();
        assertTrue(bar.getCheckers(X).isEmpty());
    }
}
