import java.util.*;

/**
 * Represents the bear-off area in Backgammon where checkers are moved
 * when they have been successfully borne off the board.
 */
public class BearOffArea {
    private final Map<String, Integer> bearOff;

    public BearOffArea() {
        bearOff = new HashMap<>();
        initialise(); // Ensure the map is initialized during construction
    }

    public void initialise() {
        // Initialize the bear-off counts for both players
        bearOff.put("X", 0);
        bearOff.put("O", 0);
    }

    public void reset() {
        // Clear and reinitialize the map
        bearOff.clear();
        initialise();
    }

    public void addChecker(String symbol) {
        // Safely increment the bear-off count for the given symbol
        bearOff.put(symbol, bearOff.getOrDefault(symbol, 0) + 1);
    }

    public int getBearOffCount(String symbol) {
        // Safely retrieve the bear-off count or return 0 if the symbol does not exist
        return bearOff.getOrDefault(symbol, 0);
    }
}
