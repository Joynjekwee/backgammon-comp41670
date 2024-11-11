public class Player{

    private String name;
    private Dice dice;

    public Player(String name){
        this.name = name;
        dice = new Dice();
    }

    public int rollDieToStart(){
        dice.rollSingleDie();
        return dice.getDie1();
    }

    public void rollDice(){
        dice.roll();
    }
    public String getDiceResults(){
        return dice.getDiceResults();
    }

    public int getDie1() {
        return dice.getDie1();
    }

    public int getDie2() {
        return dice.getDie2();
    }

    public String getName(){
        return name;
    }



}

