import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {

    /**
     * The main game class that orchestrates gameplay, manages players,
     * and tracks the game state.
     */

    private final int matchLength;
    private final Player player1, player2;
    private Player currentPlayer;
    private final Board board;
    private Dice dice = new Dice();
    private ArrayList<Integer> diceValues = new ArrayList<>();
    private String winner;
    private boolean stillPlaying = true;
    private Constants constants;
    private List<String> commandQueue = new ArrayList<>();
    private boolean testMode = false;

    /**
     * Creates a new Game instance with the specified players
     * 
     * @param player1 the first player
     * @param player2 the second player
     */

    public Game(Player player1, Player player2, int matchLength) {
        this.player1 = player1;
        this.player2 = player2;
        this.matchLength = matchLength;
        board = new Board();
        constants = new Constants();
    }

    /**
     * Starts the game, displaying player details and initializing gameplay.
     */
    public void start() {
        System.out.println();
        System.out.print("Player 1: " + player1.getName() + " (" + player1.getSymbol() + ")" + "\t\t\t\t"
                + "Player 2: " + player2.getName() + " (" + player2.getSymbol() + ")" + "\n");
        System.out.println();
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
        System.out
                .println("The Current Player is: " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ")");
    }

    /**
     * Determines which player goes first by rolling a single die for each player.
     *
     * @return The player who rolled the highest number.
     */

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

    /**
     * Displays a list of available user commands.
     */

    public void printCommands() {
        System.out.println("=========================================================");
        System.out.println("Possible commands to input");
        System.out.println("1.Roll");
        System.out.println("2.Double");
        System.out.println("3.Hint");
        System.out.println("4.Pip");
        System.out.println("5.Test");
        System.out.println("6.Quit");
        System.out.println("7.Dice");
        System.out.println("=========================================================");
        System.out.println();
    }

    public void playGame() {
        currentPlayer = whoGoesFirst();
        start();
        board.display(currentPlayer);

        while (stillPlaying) {
            System.out.println("Current Player: " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ")");
            System.out.print("User Input: ");
            String userInput = getUserInput();

            if (userInput.equalsIgnoreCase("quit")) {
                System.out.println("Quitting Game Now:");
                stillPlaying = false;
                break;
            }

            processUserCommand(userInput);

            // Check if the game is over
            if (isGameOver()) {
                System.out.printf("Game Over! %s wins this game.\n", winner);

                // Calculate result type and update scores
                determineResultType();

                // Check if the match is over
                if (player1.getScore() >= matchLength) {
                    System.out.printf("Match Over! %s wins the match with %d points!\n", player1.getName(),
                            player1.getScore());
                    stillPlaying = false;
                    break;
                } else if (player2.getScore() >= matchLength) {
                    System.out.printf("Match Over! %s wins the match with %d points!\n", player2.getName(),
                            player2.getScore());
                    stillPlaying = false;
                    break;
                }

                // Start a new game
                System.out.println("Starting a new game...");
                board.reset(); // Reset the board for the next game
                currentPlayer = whoGoesFirst(); // Decide who starts the new game
                board.display(currentPlayer);
            }
        }
    }

    /**
     * Processes the user's command and takes the appropriate action.
     *
     * @param command The command entered by the user.
     */

    private void processUserCommand(String command) {
        switch (command.toLowerCase()) {
            case "roll":
                handleRoll();
                break;

            case "moves":
                handleMoreLegalMovesCommand();
                break;

            case "pip":
                board.displayTotalPipCounts(player1, player2);
                break;

            case "test":
                handleTestCommand();
                break;

            case "hint":
                printCommands();
                break;

            case "dice":
                // need to handle dice case here
                handleCustomDiceCommand();
                break;

            case "ending":
                // simulateGameEnding(); // Call the new test mode method
                break;

            default:
                System.out.println("Invalid input, please type commands available.");
                break;
        }
    }

    private void handleTestCommand() {
        System.out.println("Input filename (must be of name.txt format):");
        processTestFile(getUserInput());
    }

    private void handleMoreLegalMovesCommand() {
        List<MoveOption> hints = board.getListOfLegalMoves(currentPlayer, diceValues);
        if (hints.isEmpty()) {
            System.out.println("No legal moves available.");
        } else {
            System.out.println("Here are some legal moves:");
            printLegalMovesWithCodes(hints);
        }
    }

    private void handleCustomDiceCommand() {
        System.out.println("Enter your dice values (e.g., '4,3' or '6,6' for doubles):");
        String input = getUserInput();

        try {
            // Split the input and ensure proper format
            String[] parts = input.split(",");
            if (parts.length != 2) {
                throw new IllegalArgumentException("You must enter exactly two dice values separated by a comma.");
            }

            // Parse the two dice values
            int die1 = Integer.parseInt(parts[0].trim());
            int die2 = Integer.parseInt(parts[1].trim());

            // Validate dice values (must be between 1 and 6)
            if (die1 < 1 || die1 > 6 || die2 < 1 || die2 > 6) {
                throw new IllegalArgumentException("Dice values must be between 1 and 6.");
            }

            // Set manual dice mode and roll the dice
            dice.enableManualMode();
            dice.setManualDice(List.of(die1, die2));
            dice.roll(); // This will use the manually set dice values
            dice.disableManualMode();

            // Output the dice results
            System.out.println("Roll Result: " + dice.getDiceResults());
            diceValues = new ArrayList<>(dice.getMoves());

            // Get legal moves from the board
            List<MoveOption> legalMoves = board.getListOfLegalMoves(currentPlayer, diceValues);

            // Display the board and handle moves
            board.display(currentPlayer);

            while (!diceValues.isEmpty()) {
                if (legalMoves.isEmpty()) {
                    System.out.println("No more legal moves. Switching turn.");
                    break;
                }

                if (legalMoves.size() == 1) {
                    System.out.println("Only one move available. Executing automatically:");
                    playMove(legalMoves.get(0), currentPlayer, diceValues);
                    continue;
                }

                System.out.println("Select your move by entering the corresponding letter (e.g., A, B, C):");
                printLegalMovesWithCodes(legalMoves);

                String userInput = getUserInput().trim().toUpperCase();
                MoveOption chosenMove = parseUserMoveByCode(userInput, legalMoves);

                if (chosenMove == null) {
                    System.out.println("Invalid selection. Please try again.");
                    continue;
                }

                playMove(chosenMove, currentPlayer, diceValues);

                // Update the board and recalculate legal moves
                board.display(currentPlayer);
                legalMoves = board.getListOfLegalMoves(currentPlayer, diceValues);
            }
        } catch (NumberFormatException e) {
            System.out.println("Error: Dice values must be numbers between 1 and 6.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
        if (diceValues.isEmpty()) {
            switchPlayer();
        }
    }

    private void handleRoll() {
        // For testing specific dice rolls
        // dice.enableManualMode();
        // dice.setManualDice(Arrays.asList(3, 3)); // Set dice to doubles [1, 8]
        dice.roll(); // This will use the manual dice values
        // dice.disableManualMode();
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

            if (legalMoves.size() == 1) { // Automatically execute the only move if there's one
                System.out.println("Only one move available. Executing automatically:");
                playMove(legalMoves.get(0), currentPlayer, diceValues);
                continue;
            }

            System.out.println("Select your move by entering the corresponding letter (e.g., A, B, C):");
            printLegalMovesWithCodes(legalMoves);

            String userInput = getUserInput().trim().toUpperCase();
            MoveOption chosenMove = parseUserMoveByCode(userInput, legalMoves);

            if (chosenMove == null) {
                System.out.println("Invalid selection. Please try again.");
                continue;
            }

            playMove(chosenMove, currentPlayer, diceValues);

            // Update the board and recalculate legal moves
            board.display(currentPlayer);
            legalMoves = board.getListOfLegalMoves(currentPlayer, diceValues);
        }
        if (diceValues.isEmpty()) {
            switchPlayer();
        }
    }

    private MoveOption parseUserMoveByCode(String userInput, List<MoveOption> legalMoves) {
        int index = userInput.charAt(0) - 'A'; // Convert 'A', 'B', 'C' to 0, 1, 2...
        if (index >= 0 && index < legalMoves.size()) {
            return legalMoves.get(index);
        }
        return null; // Invalid input
    }

    private void determineResultType() {
        Player winnerPlayer = winner.equals(player1.getName()) ? player1 : player2;
        Player loserPlayer = winnerPlayer == player1 ? player2 : player1;

        int loserBearOff = board.getBearOffCount(loserPlayer.getSymbol());
        boolean hasCheckerOnBar = !board.getBar(loserPlayer.getSymbol()).isEmpty();
        boolean hasCheckerInWinnerHome = board.hasCheckerInHomeArea(loserPlayer, winnerPlayer);

        if (loserBearOff == 0) {
            if (hasCheckerOnBar || hasCheckerInWinnerHome) {
                System.out.println("The game ended in a Backgammon");
                winnerPlayer.addScore(3);
            } else {
                System.out.println("The game ended in a Gammon"); //
                winnerPlayer.addScore(2);
            }
        } else {
            System.out.println("The game ended in a Single");
            winnerPlayer.addScore(1);
        }

        board.displayScore(currentPlayer, player1.getScore(), player2.getScore());
    }

    // Process commands from a file
    private void processTestFile(String filename) {
        if (filename == null || filename.isEmpty()) {
            System.out.println("Invalid filename provided. Please provide a valid file.");
            return;
        }

        try (Scanner fileReader = new Scanner(new File(filename))) {
            System.out.println("Loading test commands from: " + filename);
            while (fileReader.hasNextLine()) {
                commandQueue.add(fileReader.nextLine().trim());
            }
            testMode = true;
            System.out.println("Successfully loaded commands. Entering test mode.");
        } catch (FileNotFoundException e) {
            System.out.println("File: " + filename + " not found.");
            return;
        }
        processTestCommands(); // Process all commands in test mode
    }

    private void processTestCommands() {
        while (testMode ? !commandQueue.isEmpty() : stillPlaying) {
            String command = commandQueue.remove(0);
            processUserCommand(command);
        }

        if (testMode && commandQueue.isEmpty()) {
            System.out.println("No more commands available. Exiting.");
            testMode = false;
        }
    }

    private String getUserInput() {
        return new Scanner(System.in).nextLine().trim();
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

        String[] parts = input.toLowerCase().split("to");
        if (parts.length != 2) {
            return null;
        }

        try {
            int displayedStart = Integer.parseInt(parts[0].trim());
            int actualStart = currentPlayer.getSymbol().equals("O") ? (25 - displayedStart) : displayedStart;

            int actualEnd;
            if (parts[1].trim().equalsIgnoreCase("bear off")) {
                actualEnd = 25; // Special end position for bear-off
            } else {
                int displayedEnd = Integer.parseInt(parts[1].trim());
                actualEnd = currentPlayer.getSymbol().equals("O") ? (25 - displayedEnd) : displayedEnd;
            }

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

        int dispStart = displayedIndex(currentPlayer, chosenMove.getStartPos());
        String dispEnd = (chosenMove.getEndPos() == 25) ? "bear off"
                : String.valueOf(displayedIndex(currentPlayer, chosenMove.getEndPos()));
        System.out.println("You selected: " + dispStart + " to " + dispEnd);
        System.out.println("Checker moved successfully.");
    }

    /**
     * Print legal moves from the player's perspective.
     */
    public void printLegalMovesWithCodes(List<MoveOption> legalMoves) {
        char code = 'A'; // Start with 'A'
        for (MoveOption move : legalMoves) {
            int dispStart = displayedIndex(currentPlayer, move.getStartPos());
            String dispEnd = (move.getEndPos() == 25) ? "bear off"
                    : String.valueOf(displayedIndex(currentPlayer, move.getEndPos()));
            System.out.println(code + ") " + dispStart + " to " + dispEnd + " using " + move.getDiceUsed());
            code++;
        }
    }

    public boolean isGameOver() {
        if (board.getBearOffCount(constants.X) == 15) {
            winner = player1.getName();
            return true;
        } else if (board.getBearOffCount(constants.O) == 15) {
            winner = player2.getName();
            return true;
        }
        return false;
    }

    // public static Player switchPlayer(Player currentPlayer, Player player1,
    // Player player2) {
    // // If current player is player1, switch to player2, otherwise switch to
    // player1
    // if (currentPlayer == player1) {
    // System.out.println("The Current Player is Player: " + player2.getName() + "
    // (" + player2.getSymbol() + ")");
    // return player2;
    // } else {
    // System.out.println("The Current Player is Player: " + player1.getName() + "
    // (" + player1.getSymbol() + ")");
    // return player1;
    // }
    // }
}
