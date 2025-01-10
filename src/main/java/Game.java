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

    private final int matchLength; // Length of match in points
    private final Player player1, player2;
    private Player currentPlayer, doublingPlayer; // Tracks current and doubling player
    private final Board board;
    private Dice dice;
    private DoublingCube doublingCube;
    private ArrayList<Integer> diceValues;
    private String winner; // Stores winners name
    private boolean gameOver = false, matchOver = false, testMode = false, doublingOffered = false;
    private Constants constants; // Game constants
    private List<String> commandQueue; // Queue of test commands
    private int currentStake = 1;
    private CommandHandler commandHandler; // Handles user commands
    private static final String ROLL = "roll";
    private static final String DICE = "dice";
    private static final String DOUBLE = "double";
    private static final String HINT = "hint";
    private static final String TEST = "test";
    private static final String PIP = "pip";
    private static final String ACCEPT = "accept";
    private static final String REFUSE = "refuse";
    private static final String QUIT = "quit";
    private static final int BEAR_OFF_TARGET = 15;

    /**
     * Constructs a new Game instance with the specified players, board, and
     * settings.
     *
     * @param player1     the first player
     * @param player2     the second player
     * @param matchLength the match length in points
     * @param dice        the dice object
     * @param board       the game board
     * @param constants   the game constants
     */

    public Game(Player player1, Player player2, int matchLength, Dice dice, Board board, Constants constants) {
        this.player1 = player1;
        this.player2 = player2;
        this.matchLength = matchLength;
        this.board = board;
        this.constants = constants;
        this.doublingCube = new DoublingCube();
        this.dice = dice;
        this.diceValues = new ArrayList<>();
        this.commandQueue = new ArrayList<>();
        this.commandHandler = new CommandHandler();

        registerCommands();
    }

    /**
     * Registers all available commands for the game.
     */
    protected void registerCommands() {
        commandHandler.registerCommand(constants.ROLL, new RollCommand(this));
        commandHandler.registerCommand(constants.QUIT, new QuitCommand(this));
        commandHandler.registerCommand(constants.HINT, new HintCommand(this));
        commandHandler.registerCommand(constants.DOUBLE, new DoubleCommand(this));
        commandHandler.registerCommand(constants.ACCEPT, new AcceptDoubleCommand(this));
        commandHandler.registerCommand(constants.REFUSE, new RefuseDoubleCommand(this));
        commandHandler.registerCommand(constants.PIP, new PipCommand(this));
        commandHandler.registerCommand(constants.DICE, new DiceCommand(this));
        commandHandler.registerCommand(constants.TEST, new TestCommand(this));
    }

    // Getters and Setters

    public Board getBoard() {
        return board;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public void setIfMatchOver(boolean matchOver) {

        this.matchOver = matchOver;
    }

    public void setGameOver(boolean b) {
        gameOver = b;
    }

    public String getWinner() {
        return winner;
    }

    /**
     * Starts the game, displaying player details and initializing gameplay.
     */
    protected void displayPlayers() {
        System.out.println();
        System.out.print("Player 1: " + player1.getName() + " (" + player1.getSymbol() + ")" + "\t\t\t\t"
                + "Player 2: " + player2.getName() + " (" + player2.getSymbol() + ")" + "\n");
        System.out.println();
    }

    /**
     * Starts main game loop
     */

    public void play() {
        while (!matchOver) {
            initialiseGame();
            while (!gameOver) {
                printSeperator();
                System.out
                        .println("Current Player: " + currentPlayer.getName() + " (" + currentPlayer.getSymbol() + ")");
                System.out.print("User Input: ");

                String userInput = getUserInput();
                System.out.println();

                processUserCommand(userInput);

                if (isGameOver()) {
                    System.out.println();
                    printSeperator();
                    System.out.printf("Game Over! %s wins this game.\n", winner);
                    // Calculate result type and update scores
                    determineResultType();
                    determineMatchAndGameState();
                    if (matchOver)
                        break;
                }
            }

        }
    }

    /**
     * Initialise new game round.
     */
    protected void initialiseGame() {
        currentPlayer = whoGoesFirst();
        displayPlayers();
        board.display(currentPlayer, doublingCube);
    }

    /**
     * Determines which player goes first by rolling a single die for each player.
     *
     * @return The player who rolled the highest number.
     */

    protected Player whoGoesFirst() {
        System.out.println("Rolling to determine the first player...");

        int player1roll = dice.rollSingleDie();
        int player2roll = dice.rollSingleDie();

        System.out.println(player1.getName() + " rolled " + player1roll);
        System.out.println(player2.getName() + " rolled " + player2roll);

        if (player1roll > player2roll) {
            System.out.println(player1.getName() + " goes first");
            System.out.println();
            printSeperator();
            return player1;
        } else if (player2roll > player1roll) {
            System.out.println(player2.getName() + " goes first");
            System.out.println();
            printSeperator();
            return player2;
        }
        System.out.println("It's a tie! Rolling again....");
        return whoGoesFirst();

    }

    // Seperator for clarity
    private void printSeperator() {
        System.out.println("=============================================================");
    }

    /**
     * Processes the user's command and takes the appropriate action.
     *
     * @param command The command entered by the user.
     */

    private void processUserCommand(String command) {

        commandHandler.handleCommand(command);
    }

    private void switchPlayer() {
        currentPlayer = (currentPlayer == player1) ? player2 : player1;
    }

    protected boolean isGameOver() {
        if (board.getBearOffCount(constants.X) == constants.MAX_CHECKERS) {
            winner = player1.getName();
            return true;
        } else if (board.getBearOffCount(constants.O) == constants.MAX_CHECKERS) {
            winner = player2.getName();
            return true;
        }
        return false;
    }

    public boolean checkMatchState() {
        // Check if the match is over
        if (player1.getScore() >= matchLength) {
            System.out.printf("Match Over! %s wins the match with %d points!\n", player1.getName(),
                    player1.getScore());
            matchOver = true;
            return true;
        } else if (player2.getScore() >= matchLength) {
            System.out.printf("Match Over! %s wins the match with %d points!\n", player2.getName(),
                    player2.getScore());
            matchOver = true;
            return true;
        }
        return false;
    }

    /**
     * Determines the match state and either resets or ends the match.
     */
    private void determineMatchAndGameState() {
        if (checkMatchState()) {
            gameOver = true;
        } else {
            resetGameForNewRound();
        }
    }

    protected void resetGameForNewRound() {
        System.out.println("Starting a new game...");
        doublingCube.reset();
        board.reset(); // Reset the board for the next game
        initialiseGame();
    }

    private String getUserInput() {
        if (testMode && !commandQueue.isEmpty()) {
            return commandQueue.removeFirst();
        }
        return new Scanner(System.in).nextLine().trim();
    }

    /**
     * Displays a list of available user commands.
     */
    public void printCommands() {
        printSeperator();
        System.out.println("Possible commands to input");
        System.out.println("1.Roll - to roll the dice");
        System.out.println("2.Double - to double the stakes");
        System.out.println("3.Hint - to see commands available to input");
        System.out.println("4.Pip - to show pip count");
        System.out.println("5.Test - to run a test file of commands");
        System.out.println("6.Dice - to manually select dice values");
        System.out.println("7.Quit - to end game");
        printSeperator();
        System.out.println();
    }

    public void handleTestCommand() {
        System.out.println("Input filename (must be of name.txt format):");
        processTestFile(getUserInput());
    }

    private static final int MIN_DICE_VALUE = 1;
    private static final int MAX_DICE_VALUE = 6;
    private static final int DICE_COUNT = 2;

    public void handleCustomDiceCommand() {
        System.out.println("Enter your dice values (e.g., '4,3' or '6,6' for doubles):");
        String input = getUserInput();
        try {
            // Split the input and ensure proper format
            String[] parts = input.split(",");
            if (parts.length != DICE_COUNT) {
                throw new IllegalArgumentException("You must enter exactly two dice values separated by a comma.");
            }

            // Parse the two dice values
            int die1 = Integer.parseInt(parts[0].trim());
            int die2 = Integer.parseInt(parts[1].trim());

            // Validate dice values (must be between 1 and 6)
            if (die1 < MIN_DICE_VALUE || die1 > MAX_DICE_VALUE || die2 < MIN_DICE_VALUE || die2 > MAX_DICE_VALUE) {
                throw new IllegalArgumentException("Dice values must be between 1 and 6.");
            }

            handleDiceRollManualMode(die1, die2);
            handleMoves(diceValues);
        } catch (NumberFormatException e) {
            System.out.println("Error: Dice values must be numbers between 1 and 6.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }

    public void handleRollCommand() {
        dice.roll(); // This will use the manual dice values
        String dieResults = dice.getDiceResults();
        System.out.println("Roll Result: " + dieResults);
        diceValues = new ArrayList<>(dice.getMoves());
        handleMoves(diceValues);
    }

    private void handleDiceRollManualMode(int die1, int die2) {
        dice.enableManualMode();
        dice.setManualDice(List.of(die1, die2));
        dice.roll();
        dice.disableManualMode();
        System.out.println("Roll Result: " + dice.getDiceResults());
        this.diceValues = new ArrayList<>(dice.getMoves());
    }

    private void handleMoves(List<Integer> diceValues) {
        List<MoveOption> legalMoves = board.getListOfLegalMoves(currentPlayer, diceValues);
        board.display(currentPlayer, doublingCube);
        while (!diceValues.isEmpty()) {
            if (legalMoves.isEmpty()) {
                System.out.println("No more legal moves. Switching turn.");
                return;
            }
            if (legalMoves.size() == 1) {
                System.out.println("Only one move available. Executing automatically:");
                playMove(legalMoves.get(0), currentPlayer, diceValues);
                continue;
            }

            System.out.println("Select your move by entering the corresponding letter (e.g., A, B, C):");
            printLegalMovesWithCodes(legalMoves);
            MoveOption chosenMove = parseUserMoveByCode(getUserInput().toUpperCase(), legalMoves);

            if (chosenMove != null) {
                playMove(chosenMove, currentPlayer, diceValues);
            } else {
                System.out.println("Invalid selection. Please try again.");
            }

            board.display(currentPlayer, doublingCube);
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

    /**
     * Determines what way the game ended in according to backgammon rules.
     */
    protected void determineResultType() {
        Player winnerPlayer = winner.equals(player1.getName()) ? player1 : player2;
        Player loserPlayer = winnerPlayer == player1 ? player2 : player1;

        int loserBearOff = board.getBearOffCount(loserPlayer.getSymbol());
        boolean hasCheckerOnBar = !board.getBar(loserPlayer.getSymbol()).isEmpty();
        boolean hasCheckerInWinnerHome = board.hasCheckerInHomeArea(loserPlayer, winnerPlayer);

        if (loserBearOff == 0) {
            if (hasCheckerOnBar || hasCheckerInWinnerHome) {
                System.out.println("The game ended in a Backgammon");
                winnerPlayer.addScore(3 * doublingCube.getStake());
            } else {
                System.out.println("The game ended in a Gammon"); //
                winnerPlayer.addScore(2 * doublingCube.getStake());
            }
        } else {
            System.out.println("The game ended in a Single");
            winnerPlayer.addScore(doublingCube.getStake());
        }

        board.displayScore(currentPlayer, player1.getScore(), player2.getScore());
    }

    public void enableTestMode() {
        testMode = true;
    }

    public void disableTestMode() {
        testMode = false;
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
                addCommandToQueue(fileReader.nextLine().trim());
            }
            enableTestMode();
            processTestCommands(); // Process all commands in test mode
        } catch (FileNotFoundException e) {
            System.out.println("File: " + filename + " not found.");
        }

    }

    public void addCommandToQueue(String command) {
            commandQueue.add(command);
    }

    private void processTestCommands() {
        while (!commandQueue.isEmpty()) {
            String command = commandQueue.removeFirst(); // Fetch next command
            System.out.println("Processing command: " + command); // Debugging output
            processUserCommand(command);
        }
        testMode = false; // Disable test mode after processing
    }

    /**
     * Helper to get displayed index based on current player's perspective.
     */
    private int displayedIndex(Player player, int actualIndex) {
        if (player.getSymbol().equals(constants.O)) {
            return 25 - actualIndex;
        }
        return actualIndex;
    }

    /**
     * Execute the given MoveOption and remove the used dice.
     * Print the executed move from the player's perspective.
     */
    private void playMove(MoveOption chosenMove, Player currentPlayer, List<Integer> diceValues) {
        board.executeMove(chosenMove.getStartPos(), chosenMove.getEndPos(), currentPlayer);

        for (Integer usedDie : chosenMove.getDiceUsed()) {
            diceValues.remove(usedDie);
        }

        int dispStart = displayedIndex(currentPlayer, chosenMove.getStartPos());
        String dispEnd = (chosenMove.getEndPos() == 25) ? "bear off"
                : String.valueOf(displayedIndex(currentPlayer, chosenMove.getEndPos()));
        System.out.println("You selected: " + dispStart + " to " + dispEnd);
        System.out.println("Checker moved successfully.");
        System.out.println();
    }

    /**
     * Print legal moves from the player's perspective.
     */
    private void printLegalMovesWithCodes(List<MoveOption> legalMoves) {
        char code = 'A'; // Start with 'A'
        for (MoveOption move : legalMoves) {
            int dispStart = displayedIndex(currentPlayer, move.getStartPos());
            String dispEnd = (move.getEndPos() == 25) ? "bear off"
                    : String.valueOf(displayedIndex(currentPlayer, move.getEndPos()));
            System.out.println(code + ") " + dispStart + " to " + dispEnd + " using " + move.getDiceUsed());
            code++;
        }
    }

    public void doubleCommand() {
        Player opponent = currentPlayer == player1 ? player2 : player1;
        doublingPlayer = currentPlayer;
        if (doublingCube.getOwner() == null) {
            doublingCube.offerDoubling(currentPlayer);
        }

        if (doublingCube.getOwner() != currentPlayer) {
            System.out.println("You don't own the doubling cube and so cannot double at this time.");
            return;
        }
        doublingCube.offerDoubling(currentPlayer);

        if (doublingCube.getOwner() == currentPlayer && doublingCube.isDoublingOffered()) {
            // doublingPlayer = currentPlayer;
            System.out.println("Player " + currentPlayer.getName() + " offers to double the stakes.");
            System.out.println(
                    "Player " + opponent.getName() + ", enter 'accept' to double stakes or 'refuse' to end game");

            String choice = getUserInput();
            processUserCommand(choice);

        } else {
            System.out.println("You cannot double at this time.");
        }
    }

    public void handleAcceptDouble() {
        Player opponent = doublingPlayer == player1 ? player2 : player1;
        doublingCube.acceptDoubling(opponent);
        System.out.println("Player " + opponent.getName() + " accepts the double. Stakes are now doubled!");
        doublingPlayer = opponent; // Reset doubling state
    }

    public void handleRefuseDouble() {
        Player opponent = doublingPlayer == player1 ? player2 : player1;
        doublingCube.refuseDoubling();
        System.out
                .println("Player " + opponent.getName() + " refuses to double the stakes. " + currentPlayer.getName() +
                        " wins the current stake of " + currentStake + "!");
        doublingPlayer.addScore(doublingCube.getStake());
        System.out.println("Current Scores: " + player1.getName() + ": " + player1.getScore() + " | " +
                player2.getName() + ": " + player2.getScore());

        determineMatchAndGameState();
    }
}
