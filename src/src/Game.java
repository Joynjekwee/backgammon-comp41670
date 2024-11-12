import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private Player player1, player2;
    private Board board;
    private Scanner in = new Scanner(System.in);
    private Dice dice = new Dice();
    private List<Integer> listOfDie = new ArrayList<>();

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        board = new Board();
    }

    public void start(){
        System.out.println();
        System.out.print("Player 1: " + player1.getName() + "\t\t\t\t" + "Player 2: " + player2.getName() + "\n");
        System.out.println();
        board.display();
    }


    public Player whoGoesFirst(){
        System.out.println("Each player will roll to see who goes first.");
        int player1rolled;
        int player2rolled;
        while (true) {
            player1rolled = dice.rollSingleDie();
            player2rolled = dice.rollSingleDie();
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

    public void printCommands(){
        System.out.println("=========================================================");
        System.out.println("Possible commands to input");
        System.out.println("1.Roll");
        System.out.println("2.Quit");
        System.out.println("=========================================================");
        System.out.println();
    }
    public void playGame(){
        start();
        Player currentPlayer = whoGoesFirst();

        boolean stillPlaying = true;

        while (stillPlaying) {
            printCommands();
            System.out.print("User Input: ");
            String userInput = in.nextLine();

            switch (userInput) {
                case "roll":
                    dice.roll();
                    String dieResults = dice.getDiceResults();
                    System.out.println("Roll Result: " + dieResults);
                    listOfDie = dice.getMoves();
                    //System.out.println("Moves: " + dice.getMoves());
                    currentPlayer = switchPlayer(currentPlayer,player1,player2);
                    break;

                case "quit":
                    System.out.println("Quitting Game Now:");
                    stillPlaying = false;
                    break;

                default:
                    System.out.println("Invalid input, please type 'roll' or 'quit'.");
                    break;

            }
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





