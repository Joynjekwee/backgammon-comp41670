import java.util.*;

public class MoveOption {
    private int startPos;
    private int endPos;
    private List<Integer> diceUsed;

    public MoveOption(int startPos, int endPos, List<Integer> diceUsed) {
        this.startPos = startPos;
        this.endPos = endPos;
        this.diceUsed = new ArrayList<>(diceUsed);
    }

    public int getStartPos() { return startPos; }
    public int getEndPos() { return endPos; }
    public List<Integer> getDiceUsed() { return diceUsed; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MoveOption)) return false;
        MoveOption that = (MoveOption) o;
        if (startPos != that.startPos || endPos != that.endPos) return false;

        // Compare diceUsed as a sorted list
        List<Integer> a = new ArrayList<>(this.diceUsed);
        List<Integer> b = new ArrayList<>(that.diceUsed);
        Collections.sort(a);
        Collections.sort(b);
        return a.equals(b);
    }

    @Override
    public int hashCode() {
        List<Integer> sortedDice = new ArrayList<>(diceUsed);
        Collections.sort(sortedDice);
        return Objects.hash(startPos, endPos, sortedDice);
    }

    @Override
    public String toString() {
        return "Start: " + startPos + ", End: " + endPos + ", Dice: " + diceUsed;
    }
}
