public class Game {

    private Player player1, player2;
    private Board board;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        board = new Board();
    }

    public void start(){
        System.out.println("Welcome to Backgammon");
        System.out.print("Player 1: " + player1.getName() + "\t\t\t\t" + "Player 2: " + player2.getName() + "\n");
        board.display();
    }
    public Player whoGoesFirst(){
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
                   return player1;
               }
               else {
                   System.out.println(player2.getName() + " goes first");
                   return player2;
               }
            }

            System.out.println("Roll Again");
        }

    }

    public static Player switchPlayer(Player currentPlayer, Player player1, Player player2) {
        // If current player is player1, switch to player2, otherwise switch to player1
        if (currentPlayer == player1) {
            System.out.println("The Current Player is Player: " + player2.getName());
            return player2;
        } else {
            System.out.println("The Current Player is Player: " + player1.getName());
            return player1;
        }

    }
}





