import java.util.*;

public class LegalMovements {
    private final Board board;
    private final Bar bar;
    private final BearOffArea bearOffArea;
    private final Constants constants;

    /**
     * Constructor to initialize the LegalMovements class with necessary game components.
     *
     * @param board The game board representation.
     * @param bar The bar area to store checkers hit by opponents.
     * @param bearOffArea The area for bearing off checkers.
     * @param constants Constants defining the game's rules.
     */

    public LegalMovements(Board board, Bar bar, BearOffArea bearOffArea, Constants constants) {
        this.board = board;
        this.bar = bar;
        this.bearOffArea = bearOffArea;
        this.constants = constants;
    }
//Validates whether a move from start to end is allowed for a given player
    public boolean isMoveValid(int start, int end, Player player) {
        boolean withinBounds = isWithinBounds(end);
        boolean startValid = isStartValid(start, player);
        boolean endValid = isEndValid(end, player);

        return withinBounds && startValid && endValid;
    }


    private boolean isWithinBounds(int position) {
        return position >= 1 && position <= 24;
    }

    private boolean isStartValid(int start, Player player) {
        if (start == 0) { // Moving from the bar
            List<Checker> barCheckers = bar.getCheckers(player.getSymbol());
            return barCheckers != null && !barCheckers.isEmpty();
        }

        ArrayList<Checker> startCheckers = board.getCheckersAt(start);
        return !startCheckers.isEmpty() && startCheckers.get(0).getSymbol().equals(player.getSymbol());
    }

    private boolean isEndValid(int end, Player player) {
        ArrayList<Checker> endCheckers = board.getCheckersAt(end);
        String opponentSymbol = player.getSymbol().equals(constants.X) ? constants.O : constants.X;

        long opponentCheckerCount = endCheckers.stream()
                .filter(checker -> checker.getSymbol().equals(opponentSymbol))
                .count();

        return opponentCheckerCount <= 1;
    }
    /**
     * Generates possible moves to the board for checkers currently on the bar.
     *
     * @param diceValues The values of the dice rolled.
     * @param player The player making the moves.
     * @return A list of possible move options from the bar.
     */
    public List<MoveOption> canWeMoveCheckersToBoard(List<Integer> diceValues, Player player) {
        Set<MoveOption> movesFromBar = new HashSet<>();
        String playerSymbol = player.getSymbol();
        int direction = playerSymbol.equals(constants.X) ? -1 : +1;

        if (isDoubles(diceValues)) {
            int d = diceValues.get(0);
            int[] sums = { d, d * 2, d * 3, d * 4 };
            for (int sum : sums) {
                int newPos = (direction > 0) ? sum : (25 - sum);
                if (isMoveValid(0, newPos, player)) {
                    movesFromBar.add(new MoveOption(0, newPos, generateDiceUsedForSum(d, sum)));
                }
            }
        } else {
            for (int die : diceValues) {
                int newPos = (direction > 0) ? die : (25 - die);
                if (isMoveValid(0, newPos, player)) {
                    movesFromBar.add(new MoveOption(0, newPos, List.of(die)));
                }
            }

            if (diceValues.size() >= 2) {
                for (int i = 0; i < diceValues.size(); i++) {
                    for (int j = i + 1; j < diceValues.size(); j++) {
                        int sum = diceValues.get(i) + diceValues.get(j);
                        int newPos = (direction > 0) ? sum : (25 - sum);
                        if (isMoveValid(0, newPos, player)) {
                            List<Integer> diceUsed = Arrays.asList(diceValues.get(i), diceValues.get(j));
                            movesFromBar.add(new MoveOption(0, newPos, diceUsed));
                        }
                    }
                }
            }
        }

        return new ArrayList<>(movesFromBar);
    }
    /**
     * Checks if a player is eligible to bear off checkers.
     * @return True if the player can bear off; false otherwise.
     */
    public boolean canWeBearOff(Player player, List<Integer> diceValues) {
        String playerSymbol = player.getSymbol();
        int homeStart, homeEnd;

        if (playerSymbol.equals(constants.X)) {
            homeStart = 1;
            homeEnd = 6;
        } else if (playerSymbol.equals(constants.O)) {
            homeStart = 19;
            homeEnd = 24;
        } else {
            return false;
        }

        // Ensure all checkers are within the home area
        for (int i = 1; i <= 24; i++) {
            if (i < homeStart || i > homeEnd) {
                for (Checker checker : board.getCheckersAt(i)) {
                    if (checker.getSymbol().equals(playerSymbol)) {
                        return false; // Found a checker outside home area
                    }
                }
            }
        }

        // Check if any dice values allow a bear-off move
        for (Checker checker : getCurrentPlayerCheckers(player)) {
            int position = checker.getPosition();
            for (int die : diceValues) {
                int newPos = playerSymbol.equals("X") ? position - die : position + die;
                if ((playerSymbol.equals("X") && newPos < homeStart) ||
                        (playerSymbol.equals("O") && newPos > homeEnd)) {
                    return true; // A valid bear-off move is possible
                }
            }
        }

        return false; // No valid bear-off moves found
    }



    /**
     * Generates a list of all legal moves that the current player can make based on the dice values rolled.
     * @return A list of all possible legal moves the player can make.
     */
    public List<MoveOption> getListOfLegalMoves(Player player, List<Integer> diceValues) {
        System.out.println("Dice values: " + diceValues);

        Set<MoveOption> moves = new HashSet<>();

        // Handle checkers on the bar first
        if (areThereCheckersOnTheBar(player)) {
            System.out.println("Player has checkers on the bar.");
            moves.addAll(canWeMoveCheckersToBoard(diceValues, player));
        } else {
            // Generate moves for each checker
            for (Checker checker : getCurrentPlayerCheckers(player)) {
                moves.addAll(canWeMakeAMove(checker.getPosition(), diceValues, player));
            }

            // Handle bear-off logic
            if (canWeBearOff(player, diceValues)) {
                int homeStart = player.getSymbol().equals("X") ? 1 : 19;
                int homeEnd = player.getSymbol().equals("X") ? 6 : 24;

                for (Checker checker : getCurrentPlayerCheckers(player)) {
                    int position = checker.getPosition();
                    if (position >= homeStart && position <= homeEnd) {
                        for (int die : diceValues) {
                            int newPos = player.getSymbol().equals("X") ? position - die : position + die;
                            boolean validBearOff = (player.getSymbol().equals("X") && newPos < homeStart) ||
                                    (player.getSymbol().equals("O") && newPos > homeEnd);

                            if (validBearOff) {
                                MoveOption bearOffMove = new MoveOption(position, 25, List.of(die));
                                moves.add(bearOffMove);
                            }
                        }
                    }
                }
            }
        }

        // Remove all bear-off moves if canWeBearOff is false
        if (!canWeBearOff(player, diceValues)) {
            moves.removeIf(move -> move.getEndPos() == 25);
        }

        // Filter out illegal moves where there is no checker at the start position
        moves.removeIf(move -> {
            int startPos = move.getStartPos();
            ArrayList<Checker> startCheckers = board.getCheckersAt(startPos);
            return startCheckers.isEmpty() || !startCheckers.get(0).getSymbol().equals(player.getSymbol());
        });


        return new ArrayList<>(moves);
    }






//is it a double?
    private boolean isDoubles(List<Integer> diceValues) {
        if (diceValues.size() != 4)
            return false;
        int first = diceValues.get(0);
        for (int d : diceValues) {
            if (d != first)
                return false;
        }
        return true;
    }

    private List<Integer> generateDiceUsedForSum(int d, int sum) {
        List<Integer> used = new ArrayList<>();
        int count = sum / d;
        for (int i = 0; i < count; i++) {
            used.add(d);
        }
        return used;
    }


    private ArrayList<Checker> getCurrentPlayerCheckers(Player player) {

        return board.getCurrentPlayerCheckers(player);
    }

    private boolean areThereCheckersOnTheBar(Player player) {
        return !bar.getCheckers(player.getSymbol()).isEmpty();
    }

    /**
     * Generates a list of potential legal moves for a given checker starting at a specified position.
     * @return A list of all potential legal moves the checker can make from the given position.
     */

    public List<MoveOption> canWeMakeAMove(int start, List<Integer> diceValues, Player player) {
        Set<MoveOption> potentialMoves = new HashSet<>();
        String playerSymbol = player.getSymbol();
        int direction = playerSymbol.equals(constants.X) ? -1 : +1;
        int homeStart = playerSymbol.equals(constants.X) ? 1 : 19;
        int homeEnd = playerSymbol.equals(constants.X) ? 6 : 24;


        // Handle doubles logic
        if (isDoubles(diceValues)) {
            int d = diceValues.get(0); // Dice value for doubles
            int[] sums = {d, d * 2, d * 3, d * 4}; // Possible sums for doubles

            for (int sum : sums) {
                int newPos = start + (direction * sum); // Calculate new position

                // Handle bear-off for X and O when newPos goes out of bounds
                boolean validBearOff = (playerSymbol.equals(constants.X) && newPos < homeStart) ||
                        (playerSymbol.equals(constants.O) && newPos > homeEnd);

                if (validBearOff) {
                    MoveOption bearOffMove = new MoveOption(start, 25, generateDiceUsedForSum(d, sum));
                    potentialMoves.add(bearOffMove);
                } else if (isWithinBounds(newPos) && isMoveValid(start, newPos, player)) {
                    List<Integer> diceUsed = generateDiceUsedForSum(d, sum); // Generate dice used for this move
                    MoveOption move = new MoveOption(start, newPos, diceUsed);
                    potentialMoves.add(move);
                }
                /*else {
                    System.out.println("Invalid or out-of-bounds move: NewPos=" + newPos);
                }*/
            }
        } else {
            // Handle non-doubles case: single and combined dice moves

            // Single die moves
            for (int die : diceValues) {
                int newPos = start + (direction * die);

                if (isWithinBounds(newPos) && isMoveValid(start, newPos, player)) {
                    MoveOption move = new MoveOption(start, newPos, List.of(die));
                    potentialMoves.add(move);
                }
            }

            // Combined dice moves
            if (diceValues.size() >= 2) {
                for (int i = 0; i < diceValues.size(); i++) {
                    for (int j = i + 1; j < diceValues.size(); j++) {
                        int sum = diceValues.get(i) + diceValues.get(j);
                        int newPos = start + (direction * sum);

                        if (isWithinBounds(newPos) && isMoveValid(start, newPos, player)) {
                            List<Integer> diceUsed = Arrays.asList(diceValues.get(i), diceValues.get(j));
                            MoveOption move = new MoveOption(start, newPos, diceUsed);
                            potentialMoves.add(move);
                        }
                    }
                }
            }
        }

        return new ArrayList<>(potentialMoves);
    }

}
