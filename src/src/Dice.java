public class Dice {

    private int die1;
    private int die2;
    private final int NUMOFDICE = 2;


    public void rollSingleDie(){
        die1 = (int)(Math.random()*6+1);
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

    public String getDiceResults(){
        return "Dice 1: " + die1 + ", Dice 2: " + die2;
    }
}