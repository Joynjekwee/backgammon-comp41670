
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;



public class DoublingCubeTest {

    private DoublingCube doublingCube;
    private Player player;

    @BeforeEach
    public void setUp() {
        doublingCube = new DoublingCube();
        player = new Player("Alice", "X");
    }

    @Test
    public void testInitialState() {
        assertEquals(1, doublingCube.getStake());
        assertNull(doublingCube.getOwner());
        assertFalse(doublingCube.isDoublingOffered());
    }

    @Test
    public void testOfferDoubling() {
        doublingCube.offerDoubling(player);
        assertTrue(doublingCube.isDoublingOffered());
        assertEquals(player, doublingCube.getOwner());
    }


    @Test
    public void testAcceptDoubling() {
        Player anotherPlayer = new Player("Bob", "O");
        doublingCube.offerDoubling(player);
        doublingCube.acceptDoubling(anotherPlayer);
        assertEquals(2, doublingCube.getStake());
        assertFalse(doublingCube.isDoublingOffered());
    }


    @Test
    public void testRefuseDoubling() {
        doublingCube.offerDoubling(player);
        doublingCube.refuseDoubling();
        assertFalse(doublingCube.isDoublingOffered());
    }
}
