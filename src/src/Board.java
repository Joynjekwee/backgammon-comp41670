public class Board {
    private Checker[] board;


    public Board() {

        board = new Checker[24];
        initialize();
    }

    // Initializes the board with some example checkers
    private void initialize() {
        for (int i = 0; i < 24; i++) {
            board[i] = null;  // Null means no checkers on this point
        }


        board[0] = new Checker("X", 2);   // 2 White checkers on point 1
        board[5] = new Checker("X", 5);   // 5 White checkers on point 6
        board[7] = new Checker("X", 3);   // 3 White checkers on point 8

        board[11] = new Checker("O", 3);  // 5 Black checkers on point 12
        board[18] = new Checker("O", 5);  // 5 Black checkers on point 19
        board[23] = new Checker("O", 2);  // 2 Black checkers on point 24

    }

    // Display the current state of the board
    public void display() {
        System.out.println(" 13--14--15--16--17--18  BAR 19--20--21--22--23--24  OFF");

        for (int j = 0; j < 5; j++) {
            System.out.print(" ");
            for (int i = 12; i < 18; i++) {
                System.out.print(getCheckerDisplay(board[i]) + "   ");
            }
            System.out.print("BAR ");
            for (int i = 18; i < 24; i++) {
                System.out.print(getCheckerDisplay(board[i]) + "   ");
            }

            System.out.println();

        }
        for(int j = 0; j < 5; j++) {
            System.out.print(" ");
            for (int i = 11; i >= 6; i--) {
                System.out.print(getCheckerDisplay(board[i]) + "   ");
            }
            System.out.print("BAR ");
            for (int i = 5; i >= 0; i--) {
                System.out.print(getCheckerDisplay(board[i]) + "   ");
            }

            System.out.println();
        }


        System.out.println(" 12--11--10--09--08--07  BAR 06--05--04--03--02--01  OFF");

        System.out.println("\n"); // New line after full board
    }

    private String getCheckerDisplay(Checker checker) {
        return checker != null ? checker.toString() : "|";
    }
}
