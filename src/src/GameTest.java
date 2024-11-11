import java.util.Scanner;

public class GameTest {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        System.out.println("User Name player 1: ");
        String userName1 = in.nextLine();
        System.out.println("User Name player 2: ");
        String userName2 = in.nextLine();

        Player p1 = new Player(userName1);
        Player p2 = new Player(userName2);
        Game game = new Game(p1, p2);

        game.start();
        Player currentPlayer = game.whoGoesFirst();

        boolean stillPlaying = true;

        while (stillPlaying) {
            System.out.println("User Input: ");
            String userInput = in.nextLine();

            switch (userInput) {
                case "roll":
                    currentPlayer.rollDice();
                    String dieResults = currentPlayer.getDiceResults();
                    System.out.println("Roll Result: " + dieResults);
                    currentPlayer = Game.switchPlayer(currentPlayer, p1, p2);
                    break;

                case "pip":

                    game.displayPipCounts();
                    break;

                case "quit":
                    System.out.println("Quitting Game Now.");
                    stillPlaying = false;
                    break;

                default:
                    System.out.println("Invalid input, please type 'roll', 'pip', or 'quit'.");
                    break;

            }




        }
    }


}
