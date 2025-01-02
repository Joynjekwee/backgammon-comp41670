
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    private Player player;

    @BeforeEach
    public void setUp() {
        player = new Player("Alice", "X");
    }

    @Test
    public void testInitialState() {
        assertEquals("Alice", player.getName());
        assertEquals("X", player.getSymbol());
        assertEquals(0, player.getScore());
        assertFalse(player.canDouble());
    }

    @Test
    public void testAddScore() {
        player.addScore(10);
        assertEquals(10, player.getScore());
    }

    @Test
    public void testSetCanDouble() {
        player.setCanDouble();
        assertTrue(player.canDouble());
    }

    @Test
    public void testResetCanDouble() {
        player.setCanDouble();
        player.resetCanDouble();
        assertFalse(player.canDouble());
    }
}
