public class Board {
    private String[] board;

    public Board() {
        board = new String[24];
        initialize();
    }

    // Initializes the board with some example checkers
    private void initialize() {
        for (int i = 0; i < board.length; i++) {
            board[i] = "| |";  // Empty points
        }

        // Set some initial checkers (for demonstration purposes)
        board[0] = "|W2|";  // 2 white checkers
        board[5] = "|B5|";  // 5 black checkers
        board[7] = "|W3|";  // 3 white checkers
        board[11] = "|B5|"; // 5 black checkers
        board[12] = "|W5|"; // 5 white checkers
        board[16] = "|B3|"; // 3 black checkers
        board[18] = "|W5|"; // 5 white checkers
        board[23] = "|B2|"; // 2 black checkers
    }

    // Display the current state of the board
    public void display() {
        System.out.println("Backgammon Board:");
        for (int i = 0; i < board.length; i++) {
            System.out.print(board[i] + " ");
            if (i == 11) {
                System.out.println(); // New line after first half of the board
            }
        }
        System.out.println("\n"); // New line after full board
    }
}
