import java.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BearOffAreaTest {
    private BearOffArea bearOffArea;
    private final String X = "X";
    private final String O = "O";

    @Before
    public void setUp() {
        bearOffArea = new BearOffArea();
        bearOffArea.initialise();
    }



    @Test
   public void testAddChecker() {
        bearOffArea.addChecker(X);
        assertEquals(1, bearOffArea.getBearOffCount(X));
    }

    @Test
    public void testGetBearOffCount() {
        bearOffArea.addChecker(X);
        bearOffArea.addChecker(X);
        assertEquals(2, bearOffArea.getBearOffCount(X));
        assertEquals(0, bearOffArea.getBearOffCount(O));
    }

    @Test
    public void testReset() {
        bearOffArea.addChecker("X");
        bearOffArea.reset();
        assertEquals(0, bearOffArea.getBearOffCount("X"));
    }
}
