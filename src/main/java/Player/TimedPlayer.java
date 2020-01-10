package Player;

import Contract.Move;
import Contract.Player;
import Contract.ReadableBoard;

public class TimedPlayer implements Player {

    private Player player;

    private double minTime = Double.POSITIVE_INFINITY;
    private double maxTime = Double.NEGATIVE_INFINITY;

    private int moveCounter;
    private double totalTime;

    public TimedPlayer(Player player) {
        this.player = player;
    }

    public Move getMove(ReadableBoard board) {
        double startTime = System.currentTimeMillis();
        Move move = player.getMove(board);
        double moveTime = System.currentTimeMillis() - startTime;
        maxTime = Math.max(maxTime, moveTime);
        minTime = Math.min(minTime, moveTime);
        totalTime += moveTime;
        moveCounter++;
        return move;
    }

    public double getAverageTimePerMove() {
        if(moveCounter == 0) {
            throw new RuntimeException("No move was registered. Avg time per move is unknown.");
        }
        return totalTime / moveCounter;
    }

    public double getMinTimePerMove() {
        return minTime;
    }

    public double getMaxTimePerMove() {
        return maxTime;
    }

}
