package Board.Helpers;

import Contract.BoardCell;
import Contract.Move;
import Contract.MoveSelector;
import Contract.ReadableBoard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public class ApproximateMoveSelector implements MoveSelector {
    public Collection<Move> getMoves(ReadableBoard board) {

        HashSet<Move> moves = new HashSet<>();
        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {

                // select moves around non empty fields
                if(board.getCell(x, y) != BoardCell.Empty) {
                    int[] modifiers = {-1, 0, 1};
                    for (int i: modifiers) {
                        for (int j: modifiers) {
                            Move move = new Move(x+i, y+j);
                            if(board.isValidMove(move)) {
                                moves.add(move);
                            }
                        }
                    }
                }

            }
        }

        if(moves.isEmpty()) {
            return board.getValidMoves();
        }

        return moves;

    }
}
