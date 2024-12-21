import java.util.*;

/**
 * Represents the bear-off area in Backgammon where checkers are moved
 * when they have been successfully borne off the board.
 */

public class BearOffArea {
    private final Map<String, Integer> bearOff;

    public BearOffArea() {
        bearOff = new HashMap<>();
        bearOff.put("X", 0);
        bearOff.put("O", 0);
    }


    public void addChecker(String symbol) {
        bearOff.put(symbol, bearOff.get(symbol) + 1);
    }

    public int getBearOffCount(String symbol) {
        return bearOff.getOrDefault(symbol, 0);
    }
}

