public class Checker {
    private String player;
    private int count;

    public Checker(String player, int count) {
        this.player = player;
        this.count = count;
    }

    public String getPlayer() {
        return player;
    }


    public int getCount() {
        return count;
    }

    public  String getSymbol() {return player;}

    public String toString() {
        if (count > 0) {
            return player;
        } else {
            return "|";  // Empty point if there are no checkers
        }
    }

}