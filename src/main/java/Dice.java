import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Dice {

    /*
     * private int die1;
     * private int die2;
     * private final int NUMOFDICE = 2;
     * 
     * 
     * public int rollSingleDie(){
     * die1 = (int)(Math.random()*6+1);
     * return die1;
     * }
     * 
     * public void roll(){
     * die1 = (int)(Math.random()*6+1);
     * die2 = (int)(Math.random()*6+1);
     * }
     * 
     * public int getDie1(){
     * return die1;
     * }
     * 
     * public int getDie2(){
     * return die2;
     * }
     * 
     * 
     * // Check if the roll is a double (both dice show the same value)
     * public boolean isDouble() {
     * return die1 == die2;
     * }
     * 
     * // Returns the moves allowed by the dice roll
     * public ArrayList<Integer> getMoves() {
     * if (isDouble()) {
     * // If doubles, return four moves of the same value
     * return new ArrayList<Integer>(Arrays.asList(die1, die1, die1, die1));
     * } else {
     * // If not doubles, return the two dice values as the moves
     * return new ArrayList<Integer>(Arrays.asList(die1, die2));
     * }
     * }
     * 
     * 
     * public String getDiceResults(){
     * return "Dice 1: " + die1 + ", Dice 2: " + die2 + (isDouble() ? "(Doubles!)" :
     * " ");
     * }
     */

    private List<Integer> diceValues;
    private Random rand;
    private boolean manual = false; // Flag to indicate manual mode
    private List<Integer> manualDice = new ArrayList<>(); // List to store manual dice

    public Dice() {
        diceValues = new ArrayList<>();
        rand = new Random();
    }

    /**
     * Enable manual mode.
     */
    public void enableManualMode() {
        manual = true;
    }

    /**
     * Disable manual mode.
     */
    public void disableManualMode() {
        manual = false;
        manualDice.clear();
    }

    /**
     * Set manual dice values.
     */
    public void setManualDice(List<Integer> dice) {
        if (manual && dice.size() == 2) { // Ensure exactly two dice are set
            manualDice = new ArrayList<>(dice);
        }
    }

    /**
     * Rolls the dice. If doubles are rolled, prepares four dice values.
     */
    public void roll() {
        diceValues.clear();
        int die1, die2;
        if (manual) {
            if (manualDice.size() < 2) {
                throw new IllegalStateException("Not enough dice values set for manual mode.");
            }
            die1 = manualDice.get(0);
            die2 = manualDice.get(1);
        } else {
            die1 = rollSingleDie();
            die2 = rollSingleDie();
        }

        if (die1 == die2) {
            // Doubles: add four copies
            for (int i = 0; i < 4; i++) {
                diceValues.add(die1);
            }
        } else {
            // Non-doubles: add two dice
            diceValues.add(die1);
            diceValues.add(die2);
        }
    }

    public int rollSingleDie() {
        return rand.nextInt(6) + 1; // Returns 1-6
    }

    /**
     * Returns whether the current roll was doubles.
     */
    public boolean isDoubles() {
        return diceValues.size() == 4 && diceValues.get(0).equals(diceValues.get(1)) &&
                diceValues.get(1).equals(diceValues.get(2)) && diceValues.get(2).equals(diceValues.get(3));
    }

    /**
     * Returns the first die value (used for doubles).
     */
    public int getFirstDie() {
        if (!diceValues.isEmpty()) {
            return diceValues.get(0);
        }
        return 0; // Or throw an exception
    }

    /**
     * Returns the list of current dice values.
     */
    public List<Integer> getMoves() {
        return new ArrayList<>(diceValues);
    }

    /**
     * Returns a string representation of the dice.
     */
    public String getDiceResults() {
        if (isDoubles()) {
            return "Dice: " + diceValues.get(0) + " (Doubles!)";
        } else {
            return "Dice 1: " + diceValues.get(0) + ", Dice 2: " + diceValues.get(1);
        }
    }

    // // Helper Test method
    // public void setDiceValues(int die1, int die2) {
    // this.die1 = die1;
    // this.die2 = die2;
    // }

}
