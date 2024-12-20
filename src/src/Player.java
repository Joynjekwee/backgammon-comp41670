public class Player{

    private String name;
    private int score;
    private String symbol;

    public Player(String name, String symbol) {
        this.name = name;
        this.score = 0;
        this.symbol = symbol;
    }

    public int getScore() {return score;}
    public String getName(){
        return name;
    }
    public String getSymbol(){return symbol;}
    public void addScore(int increment) {
        this.score += increment;
    }



}

