import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PipCalculator {

    private Map<Integer, ArrayList<Checker>> board;
    private Constants constants;

    public PipCalculator(Map<Integer, ArrayList<Checker>> board) {
        this.board = board;
        this.constants = new Constants();
    }

    private int getPipNumber(int position, Player player) {
        if(player.getSymbol().equals(constants.X)) {
            return position;
        } else {
          //  if(24-position == 0) return 1;
            return 25-position;
        }

    }

    private void displayPipNumbers(int start, int end, Player player, boolean reverse) {
        int count = 0;
        for (int i = start; (reverse ? i >= end : i <= end); i += (reverse ? -1 : 1)) {
            int pipNumber = getPipNumber(i, player);
            count++;
            if (pipNumber < 10) {
                System.out.print(constants.zero + pipNumber);
            } else {
                System.out.print(pipNumber);
            }
            if (count < 6) {
                System.out.print("--");
            }
        }
    }

    public void displayTopPipNumbers(Player player) {
        System.out.print(constants.space);
        displayPipNumbers(13, 18, player, false);
        System.out.print("  BAR   ");
        displayPipNumbers(19, 24, player, false);
        System.out.print("  OFF");
        System.out.println();
    }

    public void displayBottomPipNumbers(Player player) {
        System.out.print(" ");
        displayPipNumbers(12, 7, player, true);
        System.out.print("  BAR   ");
        displayPipNumbers(6, 1, player, true);
        System.out.print("  OFF");
        System.out.println();
    }

    public int calculateTotalPipCount(Player player) {
        int totalPipCount = 0;
        String playerSymbol = player.getSymbol();

        for (Map.Entry<Integer, ArrayList<Checker>> entry : board.entrySet()) {
            int position = entry.getKey();
            List<Checker> checkers = entry.getValue();
            int point = checkers.size();

            if (point > 0 && checkers.get(0).getSymbol().equals(playerSymbol)) {
                int pipScore = playerSymbol.equals("X") ? (25 - position) * point : (position) * point;
                totalPipCount += pipScore;
            }
        }

        return totalPipCount;
    }

    public void displayTotalPipCounts(Player playerX, Player playerO) {
        int totalPipCountX = calculateTotalPipCount(playerX);
        int totalPipCountO = calculateTotalPipCount(playerO);

        System.out.println("Total pip score for " + playerX.getName() + ": " + totalPipCountX);
        System.out.println("Total pip score for " + playerO.getName() + ": " + totalPipCountO);
    }
}
