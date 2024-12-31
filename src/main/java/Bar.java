import java.util.*;

public class Bar {
    private final Map<String, ArrayList<Checker>> bar;

    public Bar() {
        bar = new HashMap<>();
    }

    public void addToBar(String symbol, Checker checker) {
        bar.get(symbol).add(checker);
    }

    public void initialise() {
        bar.put("X", new ArrayList<>());
        bar.put("O", new ArrayList<>());
    }
    public Checker removeFromBar(String symbol) {
        if (bar.get(symbol).isEmpty()) {
            throw new IllegalStateException("No checkers on the bar for symbol: " + symbol);
        }
        return bar.get(symbol).remove(0);
    }

    public List<Checker> getCheckers(String symbol) {
        return bar.get(symbol);
    }
    public void reset() { bar.clear(); }
}
