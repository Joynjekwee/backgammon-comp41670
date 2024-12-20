import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Game {

    /**
     * The main game class that orchestrates gameplay, manages players,
     * and tracks the game state.
     */

    private final Player player1, player2;
    private Player currentPlayer;
    private final Board board;
    private Scanner in = new Scanner(System.in);
    private Dice dice = new Dice();
    private ArrayList<Integer> diceValues = new ArrayList<>();
    private String winner;
    private boolean stillPlaying = true;

    /**
     * Creates a new Game instance with the specified players
     * @param player1 the first player
     * @param player2 the second player
     */

    public Game(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
        board = new Board();

    }

    /**
     * Starts the game, displaying player details and initializing gameplay.
     */
    public void start(){
        System.out.println();
        System.out.print("Player 1: " + player1.getName() + " (" + player1.getSymbol() + ")" + "\t\t\t\t"
                + "Player 2: " + player2.getName() + " (" + player2.getSymbol() + ")" + "\n");
        System.out.println();

    }

    /**
     * Determines which player goes first by rolling a single die for each player.
     *
     * @return The player who rolled the highest number.
     */

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

    /**
     * Displays a list of available user commands.
     */

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
            System.out.print("Enter your command: ");
            String userInput = in.nextLine();

            if(userInput.equalsIgnoreCase("quit")) {
                stillPlaying = false; // Exit the loop on quit
                break;
            }

            processUserCommand(userInput);

            if(isGameOver()){
                System.out.printf("Game Over! Congratulations, %s won!\n", winner);
                printEndOfGameScore();
                stillPlaying = false;
            }
        }
    }

    /**
     * Processes the user's command and takes the appropriate action.
     *
     * @param userInput The command entered by the user.
     */

    private void processUserCommand(String userInput){
        switch (userInput.toLowerCase()) {
            case "roll":
                handleRollCommand();
                break;

            case "quit":
                System.out.println("Quitting Game Now:");
                printEndOfGameScore();
                stillPlaying = false;
                break;

            case "hint":
                handleHintCommand();
                break;

            case "pip":
                board.displayTotalPipCounts(player1, player2);
                break;

            case "test":
                handleTestCommand();
                break;

            default:
                System.out.println("Invalid input, please type commands available.");
                break;

        }
    }

    private void handleTestCommand() {
        System.out.println("Input filename:");
        String filename = in.nextLine();
        processTestFile(filename);
    }

    private void handleHintCommand() {
        ArrayList<String> hints = board.getListOfLegalMoves(currentPlayer, diceValues);
        if (hints.isEmpty()) {
            System.out.println("No legal moves available.");
        } else {
            System.out.println("Here are some legal moves:");
            printLegalMoves(hints);
        }
    }

    private void handleRollCommand() {
        dice.roll();
        String dieResults = dice.getDiceResults();
        System.out.println("Roll Result: " + dieResults);
        diceValues = dice.getMoves();

        ArrayList<String> legalMoves = board.getListOfLegalMoves(currentPlayer, diceValues);
        if (legalMoves.isEmpty()) {
            System.out.println("No legal moves available. Switching turn.");
        } else {
            handlePlayerMove(legalMoves);
        }

        currentPlayer = switchPlayer(currentPlayer,player1,player2);
        board.display(currentPlayer);
    }

    private void handlePlayerMove(ArrayList<String> legalMoves) {
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





