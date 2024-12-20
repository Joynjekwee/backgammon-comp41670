import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Game {

    private Player player1, player2, currentPlayer;
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

    public void start() {
        System.out.println();
        System.out.print("Player 1: " + player1.getName() + " (" + player1.getSymbol() + ")" + "\t\t\t\t"
                + "Player 2: " + player2.getName() + " (" + player2.getSymbol() + ")" + "\n");
        System.out.println();
    }

    public Player whoGoesFirst() {
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
                } else {
                    System.out.println(player2.getName() + " goes first");
                    System.out.println();
                    return player2;
                }
            }

            System.out.println("Roll Again");
        }
    }

    public void printCommands() {
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

    public void playGame() {
        currentPlayer = whoGoesFirst();
        start();
        board.display(currentPlayer);

        while (stillPlaying) {
            System.out.println("Current Player: " + currentPlayer.getName() + "(" + currentPlayer.getSymbol() + ")");
            System.out.print("User Input: ");
            String userInput = in.nextLine().trim();

            if (userInput.equalsIgnoreCase("quit")) {
                stillPlaying = false; // Exit the loop on quit
            } else {
                processUserCommand(userInput);
            }

            if (isGameOver()) {
                System.out.println("Game Over!");
                System.out.println("Congratulations! " + winner + " won!");
                printEndOfGameScore();
                stillPlaying = false;
            }
        }
    }

    private void processUserCommand(String userInput) {
        switch (userInput.toLowerCase()) {
            case "roll":
                // For testing specific dice rolls
                dice.enableManualMode();
                dice.setManualDice(Arrays.asList(1, 8)); // Set dice to doubles [1, 8]
                dice.roll(); // This will use the manual dice values
                //dice.disableManualMode();
                // dice.roll();
                String dieResults = dice.getDiceResults();
                System.out.println("Roll Result: " + dieResults);
               diceValues = new ArrayList<>(dice.getMoves());
               // diceValues = dice.getMoves();

                // Get legal moves from the board
                List<MoveOption> legalMoves = board.getListOfLegalMoves(currentPlayer, diceValues);

                // Display the board before making moves
                board.display(currentPlayer);

                // Loop until all dice are used or no moves remain
                while (!diceValues.isEmpty()) {
                    if (legalMoves.isEmpty()) {
                        System.out.println("No more legal moves. Switching turn.");
                        break;
                    }

                    System.out.println("Enter your move in the format 'start to end' (e.g., '5 to 12') or type 'hint' for suggestions:");
                    String moveInput = in.nextLine().trim();

                    if (moveInput.equalsIgnoreCase("hint")) {
                        printLegalMoves(legalMoves);
                        continue;
                    }

                    MoveOption chosenMove = parseUserMoveInput(moveInput, legalMoves);
                    if (chosenMove == null) {
                        System.out.println("Invalid move. Please try again.");
                        continue;
                    }

                    // Execute the chosen move
                    playMove(chosenMove, currentPlayer, diceValues);

                    // Display the board after the move
                    board.display(currentPlayer);

                    // Recalculate legal moves with remaining dice
                    legalMoves = board.getListOfLegalMoves(currentPlayer, diceValues);
                }

                currentPlayer = switchPlayer(currentPlayer, player1, player2);
                board.display(currentPlayer);
                break;

            case "quit":
                System.out.println("Quitting Game Now:");
                printEndOfGameScore();
                stillPlaying = false;
                break;

            case "hint":
                List<MoveOption> hints = board.getListOfLegalMoves(currentPlayer, diceValues);
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
                break;

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
                String command = myReader.nextLine().trim();
                processUserCommand(command);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    /**
     * Helper to get displayed index based on current player's perspective.
     */
    private int displayedIndex(Player player, int actualIndex) {
        if (player.getSymbol().equals("O")) {
            return 25 - actualIndex;
        }
        return actualIndex;
    }

    /**
     * Parses user input in the format "start to end" from the player's perspective.
     * Convert from player's displayed perspective back to actual board index.
     */
    private MoveOption parseUserMoveInput(String input, List<MoveOption> legalMoves) {
        String[] parts = input.split("to");
        if (parts.length != 2) {
            return null;
        }

        try {
            int displayedStart = Integer.parseInt(parts[0].trim());
            int displayedEnd = Integer.parseInt(parts[1].trim());

            // Convert displayed indices to actual indices
            int actualStart = (currentPlayer.getSymbol().equals("O")) ? (25 - displayedStart) : displayedStart;
            int actualEnd = (currentPlayer.getSymbol().equals("O")) ? (25 - displayedEnd) : displayedEnd;

            for (MoveOption move : legalMoves) {
                if (move.getStartPos() == actualStart && move.getEndPos() == actualEnd) {
                    return move;
                }
            }
        } catch (NumberFormatException e) {
            return null;
        }

        return null;
    }

    /**
     * Execute the given MoveOption and remove the used dice.
     * Print the executed move from the player's perspective.
     */
    public void playMove(MoveOption chosenMove, Player currentPlayer, List<Integer> diceValues) {
        board.executeMove(chosenMove.getStartPos(), chosenMove.getEndPos(), currentPlayer);
        for (Integer usedDie : chosenMove.getDiceUsed()) {
            diceValues.remove(usedDie);
        }

        // Print move result from player's perspective
        int dispStart = displayedIndex(currentPlayer, chosenMove.getStartPos());
        int dispEnd = displayedIndex(currentPlayer, chosenMove.getEndPos());
        System.out.println("Moved checker from position " + dispStart + " to " + dispEnd);
    }

    /**
     * Print legal moves from the player's perspective.
     */
    public void printLegalMoves(List<MoveOption> legalMoves) {
        for (int i = 0; i < legalMoves.size(); i++) {
            MoveOption move = legalMoves.get(i);
            // Convert actual start/end to displayed for current player
            int dispStart = displayedIndex(currentPlayer, move.getStartPos());
            int dispEnd = displayedIndex(currentPlayer, move.getEndPos());
            System.out.println((i + 1) + ". " + dispStart + " to " + dispEnd + " using " + move.getDiceUsed());
        }
    }

    public void printEndOfGameScore() {
        System.out.println(player1.getName() + " score: " + player1.getScore());
        System.out.println(player2.getName() + " score: " + player2.getScore());
    }

    public boolean isGameOver() {
        if (board.getBearoffAreaPlayer1().size() == 15) {
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
            System.out.println("The Current Player is Player: " + player1.getName() + " (" + player1.getSymbol() + ")");
            return player1;
        }
    }
}
