import java.util.Scanner;

public class GameTest {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Welcome to Backgammon");
        System.out.println();
        System.out.print("User Name player 1: ");
        String userName1 = in.nextLine();
        System.out.print("User Name player 2: ");
        String userName2 = in.nextLine();

        String player1Symbol = "X";
        String player2Symbol = "O";
        Player p1 = new Player(userName1, player1Symbol);
        Player p2 = new Player(userName2, player2Symbol);
        Game game = new Game(p1, p2);

        game.playGame();

    }
}
