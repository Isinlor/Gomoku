package Board.Helpers;

import Contract.BoardCell;
import Contract.Move;
import Contract.MoveSelector;
import Contract.ReadableBoard;

import java.util.*;

public class ApproximateMoveSelector implements MoveSelector {

    private final int[][] modifiers = {
        {-1, -1}, {-1, 0}, {-1,  1},
        { 0, -1},          { 0,  1},
        { 1, -1}, { 1, 0}, { 1,  1},
    };

    public Collection<Move> getMoves(ReadableBoard board) {
        return board.getApproximateValidMoves();
    }

}
