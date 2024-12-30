import java.util.Scanner;

public class Main {

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

        // match length
        int matchLength = 0;
        while (matchLength <= 0) {
            try {
                System.out.print("Enter match length (points to win): ");
                matchLength = Integer.parseInt(in.nextLine().trim());
                if (matchLength <= 0) {
                    System.out.println("Match length must be positive. Enter the correct length!");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number!");
            }
        }

        Player p1 = new Player(userName1, player1Symbol);
        Player p2 = new Player(userName2, player2Symbol);
        Game game = new Game(p1, p2, matchLength);
        game.printCommands();
        game.playGame();

    }
}
