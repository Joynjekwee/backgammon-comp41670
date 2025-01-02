
import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class CheckerTest {

    @Test
    public void testSymbolAndPositionInitialization() {
        Checker checker = new Checker("X", 1);
        assertEquals("X", checker.getSymbol());
        assertEquals(1, checker.getPosition());
    }

    @Test
    public void testGetPlayer() {
        Checker checker = new Checker("O");
        assertEquals("O", checker.getPlayer());
        assertNotEquals("X",checker.getPlayer());
    }
}

