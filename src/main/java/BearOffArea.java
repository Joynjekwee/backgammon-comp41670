import java.util.*;

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

