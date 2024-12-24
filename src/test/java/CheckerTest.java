import org.junit.*;
import static org.junit.Assert.*;

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

