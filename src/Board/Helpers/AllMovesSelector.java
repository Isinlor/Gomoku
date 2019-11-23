package Board.Helpers;

import Contract.Move;
import Contract.MoveSelector;
import Contract.ReadableBoard;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class AllMovesSelector implements MoveSelector {
    public Set<Move> getMoves(ReadableBoard board) {
        return board.getValidMoves();
    }
}
