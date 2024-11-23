import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    private Player player1, player2,currentPlayer;
    private Board board;
    private Scanner in = new Scanner(System.in);
    private Dice dice = new Dice();
    private ArrayList<Integer> diceValues = new ArrayList<>();
    private String winner;

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        board = new Board();
    }

    public void start(){
        System.out.println();
        System.out.print("Player 1: " + player1.getName() + " (" + player1.getSymbol() + ")" + "\t\t\t\t"
                + "Player 2: " + player2.getName() + " (" + player2.getSymbol() + ")" + "\n");
        System.out.println();

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
                   System.out.println();
                   System.out.println();
                   return player1;
               }
               else {
                   System.out.println(player2.getName() + " goes first");
                   System.out.println();
                   System.out.println();
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
        System.out.println("3.Hint");
        System.out.println("4.Pip");
        System.out.println("=========================================================");
        System.out.println();
    }

    public void playGame(){
        start();
        Player currentPlayer = whoGoesFirst();
        board.display(currentPlayer);

        boolean stillPlaying = true;

        while (stillPlaying) {

            System.out.print("User Input: ");
            String userInput = in.nextLine();

            switch (userInput.toLowerCase()) {
                case "roll":
                    dice.roll();
                    String dieResults = dice.getDiceResults();
                    System.out.println("Roll Result: " + dieResults);
                    diceValues = dice.getMoves();
                    ArrayList<String> legalMoves = board.getListOfLegalMoves(currentPlayer, diceValues);
                    printLegalMoves(legalMoves);
                    int choice = playerSelectsMoveToPlay(legalMoves);
                    playMove(choice, legalMoves);
                    //System.out.println("Moves: " + dice.getMoves());
                    currentPlayer = switchPlayer(currentPlayer,player1,player2);
                    board.display(currentPlayer);
                    break;

                case "quit":
                    System.out.println("Quitting Game Now:");
                    printEndOfGameScore();
                    stillPlaying = false;
                    break;

                case "hint":
                    printCommands();
                    break;

                case "pip":
                    board.displayTotalPipCounts(player1, player2);
                    break;
                default:
                    System.out.println("Invalid input, please type commands available.");
                    break;

            }

            if(isGameOver()){
                System.out.println("Game Over!");
                System.out.println("Congratulations! " + winner + " won!");
                printEndOfGameScore();
                stillPlaying = false;
            }



        }
    }


    public int playerSelectsMoveToPlay(ArrayList<String> legalMoves) {

        System.out.println();
        int moveSelected;
        while (true) {
            System.out.println("Please select a move to play (input number): ");
             moveSelected = in.nextInt();
            //if(!legalMoves.contains(moveSelected)) <- this is wrong ???
            if(!(moveSelected >= 1 && moveSelected <= legalMoves.size()))
                System.out.println("Invalid move!");
            else
                break;
        }

        return moveSelected;


    }

    public void playMove(int choice, ArrayList<String> legalMoves) {

        int startPoint;
        int endPoint;

        String move = legalMoves.get(choice-1);
       if(move.equals("-1")){
           return;
       }
        String[] parts = move.split(", ");
       if(parts[0].contains("Bar")) {
           startPoint = 0;
           endPoint = Integer.parseInt(parts[1].replaceAll("[^0-9]", "").trim()) - 1;
       //    board.executeMove(startPoint,endPoint, currentPlayer);
       } else {
           startPoint = Integer.parseInt(parts[0].replaceAll("[^0-9]", "").trim()) - 1;
           endPoint = Integer.parseInt(parts[1].replaceAll("[^0-9]", "").trim()) - 1;
       }


        board.executeMove(startPoint,endPoint, currentPlayer);


    }
    public void printLegalMoves(ArrayList<String> legalMoves){
        for(int i = 0; i < legalMoves.size(); i++){
            System.out.println((i+1) + ". " + legalMoves.get(i));
        }
    }
    public void printEndOfGameScore(){
        System.out.println(player1.getName() + " score: " + player1.getScore());
        System.out.println(player2.getName() + " score: " + player2.getScore());
    }

    public boolean isGameOver(){
        if(board.getBearoffAreaPlayer1().size() == 15){
            winner = player1.getName();
            return true;
        }
        else if (board.getBearoffAreaPlayer2().size() == 15) {
            winner = player2.getName();
            return true;
        }
        return false;
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





