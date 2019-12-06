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

        int boardSize = board.getSize();
        Collection<Move> moves = new ArrayDeque<>();
        for (Move move: board.getValidMoves()) {
            for (int[] modifier: modifiers) {
                int i = modifier[0];
                int j = modifier[1];
                if(
                    move.x + i >= 0 && move.y + j >= 0 &&
                    move.x + i < boardSize && move.y + j < boardSize &&
                    board.getCell(move.x + i, move.y + j) != BoardCell.Empty
                ) {
                    moves.add(move);
                    break;
                }
            }
        }

        if(moves.isEmpty()) {
            return board.getValidMoves().getCopy();
        }

        return moves;

    }
}
