package Board.Helpers;

import Contract.BoardCell;
import Contract.Move;
import Contract.MoveSelector;
import Contract.ReadableBoard;

import java.util.*;

public class LocalApproximateMoveSelector implements MoveSelector {

    public Collection<Move> getMoves(ReadableBoard board) {
        return board.getApproximateValidMoves();
    }

}
