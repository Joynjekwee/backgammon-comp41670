import java.util.Arrays;
import java.util.List;

public class Dice {

    private int die1;
    private int die2;
    private final int NUMOFDICE = 2;


    public int rollSingleDie(){
        die1 = (int)(Math.random()*6+1);
        return die1;
    }

    public void roll(){
        die1 = (int)(Math.random()*6+1);
        die2 = (int)(Math.random()*6+1);
    }

    public int getDie1(){
        return die1;
    }

    public int getDie2(){
        return die2;
    }


    // Check if the roll is a double (both dice show the same value)
    public boolean isDouble() {
        return die1 == die2;
    }

    // Returns the moves allowed by the dice roll
    public List<Integer> getMoves() {
        if (isDouble()) {
            // If doubles, return four moves of the same value
            return Arrays.asList(die1, die1, die1, die1);
        } else {
            // If not doubles, return the two dice values as the moves
            return Arrays.asList(die1, die2);
        }
    }


    public String getDiceResults(){
        return "Dice 1: " + die1 + ", Dice 2: " + die2 + (isDouble() ? "(Doubles!)" : " ");
    }
}
