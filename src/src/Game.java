public class Game {

    private Player player1, player2;
    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    public void whoGoesFirst(){
        System.out.println("Each player will roll to see who goes first.");
        int player1rolled;
        int player2rolled;
        while (true) {
            player1rolled = player1.rollDieToStart();
            player2rolled = player2.rollDieToStart();
            System.out.println(player1.getName() + " rolled " + player1rolled);
            System.out.println(player2.getName() + " rolled " + player2rolled);
            if (player1rolled != player2rolled) {
               if (player1rolled > player2rolled) {
                   System.out.println(player1.getName() + " goes first");
               }
               else {
                   System.out.println(player2.getName() + " goes first");
               }
                break;
            }
        }
    }
}
