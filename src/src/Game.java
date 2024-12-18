import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    private Player player1, player2,currentPlayer;
    private Board board;
    private Scanner in = new Scanner(System.in);
    private Dice dice = new Dice();
    private ArrayList<Integer> diceValues = new ArrayList<>();
    private String winner;
    private boolean stillPlaying = true;


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
                   return player1;
               }
               else {
                   System.out.println(player2.getName() + " goes first");
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
        System.out.println("5.Test");
        System.out.println("=========================================================");
        System.out.println();
    }

    public void playGame(){
        currentPlayer = whoGoesFirst();
        start();
        board.display(currentPlayer);



        while (stillPlaying) {
            System.out.println("Current Player: " + currentPlayer.getName() + "("  + currentPlayer.getSymbol() + ")" );
            System.out.print( "User Input: ");
            String userInput = in.nextLine();

            if(userInput.equalsIgnoreCase("quit")) {
                stillPlaying = false; // Exit the loop on quit
            }

            processUserCommand(userInput);

            if(isGameOver()){
                System.out.println("Game Over!");
                System.out.println("Congratulations! " + winner + " won!");
                printEndOfGameScore();
                stillPlaying = false;
            }
        }
    }

    private void processUserCommand(String userInput){
        switch (userInput.toLowerCase()) {
            case "roll":
                dice.roll();
                String dieResults = dice.getDiceResults();
                System.out.println("Roll Result: " + dieResults);
                diceValues = dice.getMoves();
                ArrayList<String> legalMoves = board.getListOfLegalMoves(currentPlayer, diceValues);

                if (legalMoves.isEmpty()) {
                    System.out.println("No legal moves available. Switching turn.");
                } else {
                    //   printLegalMoves(legalMoves);

                    // Ask the player to input their move
                    boolean validMove = false;
                    while (!validMove) {
                        System.out.print("Enter your move as: pos1 to pos2 (or type 'hint' to see suggestions): ");
                        String moveInput = in.nextLine();

                        if (moveInput.equalsIgnoreCase("hint")) {
                            System.out.println("Here are some legal moves:");
                            printLegalMoves(legalMoves);
                        } else {
                            validMove = validateAndExecuteMove(moveInput, currentPlayer);
                            if (!validMove) {
                                System.out.println("Invalid move. Try again.");
                            }
                        }
                    }
                }

                currentPlayer = switchPlayer(currentPlayer,player1,player2);
                board.display(currentPlayer);
                break;

            case "quit":
                System.out.println("Quitting Game Now:");
                printEndOfGameScore();
                stillPlaying = false;
                break;

            case "hint":
                ArrayList<String> hints = board.getListOfLegalMoves(currentPlayer, diceValues);
                if (hints.isEmpty()) {
                    System.out.println("No legal moves available.");
                } else {
                    System.out.println("Here are some legal moves:");
                    printLegalMoves(hints);
                }
                break;

            case "pip":
                board.displayTotalPipCounts(player1, player2);
                break;

            case "test":
                System.out.println("Input filename:");
                String filename = in.nextLine();
                processTestFile(filename);
            default:
                System.out.println("Invalid input, please type commands available.");
                break;

        }
    }

    private void processTestFile(String filename) {
        try {
            File myObj = new File(filename);
            Scanner myReader = new Scanner(myObj);

            while (myReader.hasNextLine()) {
                String command = myReader.nextLine();
                //System.out.println(command);

                processUserCommand(command);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
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

    private boolean validateAndExecuteMove(String moveInput, Player currentPlayer) {
        try {
            String[] parts = moveInput.split("to");
            int start = Integer.parseInt(parts[0].trim()) ;
            int end = Integer.parseInt(parts[1].trim()) ;

            ArrayList<String> legalMoves = board.getListOfLegalMoves(currentPlayer, diceValues);
            for (String move : legalMoves) {
                if (move.contains("Initial position: " + (start)) &&
                        move.contains("Final position: " + (end))) {
                    board.executeMove(start, end, currentPlayer);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println("Invalid format. Use the format '13 to 18'.");
        }
        return false;
    }

    public void playMove(int choice, ArrayList<String> legalMoves) {
        int startPoint;
        int endPoint;

       String move = legalMoves.get(choice-1);
       if(move.equals("-1")) {
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

    public void printLegalMoves(ArrayList<String> legalMoves) {
        for(int i = 0; i < legalMoves.size(); i++) {
            System.out.println((i+1) + ". " + legalMoves.get(i));
        }
    }

    public void printEndOfGameScore() {
        System.out.println(player1.getName() + " score: " + player1.getScore());
        System.out.println(player2.getName() + " score: " + player2.getScore());
    }

    public boolean isGameOver() {
        if(board.getBearoffAreaPlayer1().size() == 15) {
            winner = player1.getName();
            return true;
        } else if (board.getBearoffAreaPlayer2().size() == 15) {
            winner = player2.getName();
            return true;
        }
        return false;
    }

    public static Player switchPlayer(Player currentPlayer, Player player1, Player player2) {

        // If current player is player1, switch to player2, otherwise switch to player1
        if (currentPlayer == player1) {
            System.out.println("The Current Player is Player: " + player2.getName() + " (" + player2.getSymbol() + ")");
            return player2;
        } else {
            System.out.println("The Current Player is Player: " + player1.getName()+ " (" + player1.getSymbol() + ")");
            return player1;
        }
    }
}





