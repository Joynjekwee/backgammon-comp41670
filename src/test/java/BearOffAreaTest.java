import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class BearOffAreaTest {
    private BearOffArea bearOffArea;
    private final String X = "X";
    private final String O = "O";

    @BeforeEach
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
