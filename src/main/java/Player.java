public class Player{

    private String name;
    private int score; //FOR MATCHES
    private String symbol;
    private boolean canDouble = false;

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
        this.score += increment;//
    }

    public boolean canDouble(){return canDouble;}

    public void setCanDouble(){canDouble = true;}

    public  void resetCanDouble(){canDouble = false;}
}
