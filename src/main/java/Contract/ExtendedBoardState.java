package Contract;

import java.util.Arrays;
import java.util.HashMap;

public class ExtendedBoardState extends BoardState {

    private static final long serialVersionUID = 1;
    HashMap<Move, Double> lastWeightedMoves;

    public ExtendedBoardState(BoardCell[][] grid, Color currentPlayer, Move lastMove, HashMap<Move, Double> lastWeightedMoves) {
        super(grid, currentPlayer, lastMove);
        this.lastWeightedMoves = lastWeightedMoves;
    }

    public ExtendedBoardState(BoardState boardState, HashMap<Move, Double> lastWeightedMoves) {
        super(boardState.grid, boardState.currentPlayer, boardState.lastMove);
        this.lastWeightedMoves = lastWeightedMoves;
    }

    public double[] toPolicyVector() {
        int cells = grid.length * grid.length;
        double[] vector = new double[cells];
        Arrays.fill(vector, 0);

        for (Move move : lastWeightedMoves.keySet()) {
            vector[move.x * grid.length + move.y] = lastWeightedMoves.get(move);
        }

        return vector;
    }

}
