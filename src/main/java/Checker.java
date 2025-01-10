/**
 * Checker class representing the checkers moving on the board.
 * Checker contains its respective symbol and position on board.
 */

public class Checker {
    private String symbol;
    private int position;


   public Checker(String symbol) {
        this.symbol = symbol;
    }

    public Checker(String symbol, int position) {
        this.symbol = symbol;
        this.position = position;
    }
    public String getPlayer() { return symbol;}

    public int getPosition() { return position;}

    public  String getSymbol() {return symbol;}

    //setter
    public void setPosition(int position) {
        this.position = position;
    }
}
