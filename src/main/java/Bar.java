import java.util.*;

public class Bar {
    private final Map<String, ArrayList<Checker>> bar;

    public Bar() {
        bar = new HashMap<>();
        initialise(); // Ensure the bar is pre-populated with empty lists for "X" and "O"
    }

    public void addToBar(String symbol, Checker checker) {
        // Ensure the ArrayList for the given symbol is initialized before adding
        bar.computeIfAbsent(symbol, k -> new ArrayList<>());
        bar.get(symbol).add(checker);
    }

    public void initialise() {
        // Initialize the bar with empty ArrayLists for "X" and "O"
        bar.put("X", new ArrayList<>());
        bar.put("O", new ArrayList<>());
    }

    public Checker removeFromBar(String symbol) {
        if (!bar.containsKey(symbol) || bar.get(symbol).isEmpty()) {
            throw new IllegalStateException("No checkers on the bar for symbol: " + symbol);
        }
        return bar.get(symbol).remove(0);
    }

    public List<Checker> getCheckers(String symbol) {
        // Safely return the list of checkers or an empty list if the symbol does not exist
        return bar.getOrDefault(symbol, new ArrayList<>());
    }

    public void reset() {
        // Clear the bar and reinitialize it
        bar.clear();
        initialise();
    }
}
