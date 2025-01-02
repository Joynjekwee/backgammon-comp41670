
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class MoveOptionTest {
    private MoveOption moveOption1;
    private MoveOption moveOption2;
    private MoveOption moveOption3;

    @BeforeEach
    public void setUp() {
        moveOption1 = new MoveOption(1, 5, Arrays.asList(3, 2));
        moveOption2 = new MoveOption(1, 5, Arrays.asList(2, 3)); // Same as moveOption1 but dice order differs
        moveOption3 = new MoveOption(2, 6, Arrays.asList(4, 1));
    }

    @Test
    public void testConstructorAndGetters() {
        MoveOption moveOption = new MoveOption(1, 10, Arrays.asList(4, 2));
        assertEquals(1, moveOption.getStartPos());
        assertEquals(10, moveOption.getEndPos());
        assertEquals(Arrays.asList(4, 2), moveOption.getDiceUsed());
    }

    @Test
    public void testEquals_SameObject() {
        assertTrue(moveOption1.equals(moveOption1)); // An object should equal itself
    }

    @Test
    public void testEquals_DifferentObjectsSameValues() {
        assertTrue(moveOption1.equals(moveOption2)); // Equal values but different instances
    }

    @Test
    public void testEquals_DifferentStartPos() {
        MoveOption differentStart = new MoveOption(2, 5, Arrays.asList(3, 2));
        assertFalse(moveOption1.equals(differentStart));
    }

    @Test
    public void testEquals_DifferentEndPos() {
        MoveOption differentEnd = new MoveOption(1, 6, Arrays.asList(3, 2));
        assertFalse(moveOption1.equals(differentEnd));
    }

    @Test
    public void testEquals_DifferentDiceUsed() {
        MoveOption differentDice = new MoveOption(1, 5, Arrays.asList(1, 3));
        assertFalse(moveOption1.equals(differentDice));
    }

    @Test
    public void testEquals_Null() {
        assertFalse(moveOption1.equals(null)); // An object should not equal null
    }

    @Test
    public void testEquals_DifferentClass() {
        assertFalse(moveOption1.equals("String")); // An object should not equal an object of another class
    }

    @Test
    public void testHashCode_EqualObjects() {
        assertEquals(moveOption1.hashCode(), moveOption2.hashCode()); // Equal objects must have the same hash code
    }

    @Test
    public void testHashCode_DifferentObjects() {
        assertNotEquals(moveOption1.hashCode(), moveOption3.hashCode()); // Different objects should have different hash codes
    }

    @Test
    public void testToString() {
        String expected = "Start: 1, End: 5, Dice: [3, 2]";
        assertEquals(expected, moveOption1.toString());
    }

    @Test
    public void testDiceUsedSortingInEquals() {
        MoveOption unsortedDice = new MoveOption(1, 5, Arrays.asList(3, 2));
        MoveOption sortedDice = new MoveOption(1, 5, Arrays.asList(2, 3));
        assertTrue(unsortedDice.equals(sortedDice)); // Dice order should not matter
    }
}
