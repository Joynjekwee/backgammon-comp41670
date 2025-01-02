import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        boolean playAgain = true;
        System.out.println("Welcome to Backgammon");
        System.out.println();

        while (playAgain) {
            // Get player names
            System.out.print("Enter name for Player 1: ");
            String playerName1 = in.nextLine();
            System.out.print("Enter name for Player 2: ");
            String playerName2 = in.nextLine();

            String player1Symbol = "X";
            String player2Symbol = "O";

            // Get match length
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

            // Create a new game
            Player p1 = new Player(playerName1, player1Symbol);
            Player p2 = new Player(playerName2, player2Symbol);

            // Inject dependencies into the Game Class
            Dice dice = new Dice();
            Board board = new Board();
            Constants constants = new Constants();

            Game game = new Game(p1, p2, matchLength, dice, board, constants);
            game.printCommands();
            game.play();

            // Ask if the players want to start a new match
            System.out.print("Do you want to start a new match? (yes/no): ");
            String response = in.nextLine();
            playAgain = response.equalsIgnoreCase("yes");
        }

        System.out.println("Thank you for playing!");

    }
}
