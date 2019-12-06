package Board.Helpers;

import Contract.Move;
import Contract.MoveSelector;
import Contract.ReadableBoard;

import java.util.Collection;

public class AllMovesSelector implements MoveSelector {
    public Collection<Move> getMoves(ReadableBoard board) {
        return board.getValidMoves().getCopy();
    }
}
