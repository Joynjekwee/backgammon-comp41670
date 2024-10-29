public class Player{

    private String name;
    private Dice dice;

    public Player(String name){
        this.name = name;
        dice = new Dice();
    }

    public int rollDieToStart(){
        dice.rollSingleDie();
        return dice.getDie(0);
    }

    public void roll(){
        dice.rollDice();
    }

    public String getName(){
        return name;
    }

}
