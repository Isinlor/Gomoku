package Board.Helpers;

import Board.SimpleBoard;
import Contract.Evaluation;
import Contract.Move;
import Contract.MoveSelector;
import Contract.ReadableBoard;

import java.util.Collection;
import java.util.HashSet;

public class ForcedMoveSelector implements MoveSelector {

    private Evaluation evaluation;
    private MoveSelector broaderMoveSelector;

    public ForcedMoveSelector(Evaluation evaluation, MoveSelector broaderMoveSelector) {
        this.evaluation = evaluation;
        this.broaderMoveSelector = broaderMoveSelector;
    }

    public Collection<Move> getMoves(ReadableBoard board) {

        HashSet<Move> moves = new HashSet<>();
        HashSet<Move> winningMoves = new HashSet<>();
        SimpleBoard boardCopy = new SimpleBoard(board);
        for (Move move: broaderMoveSelector.getMoves(board)) {

            boardCopy.move(move);
            double result = evaluation.evaluate(boardCopy);
            boardCopy.revertLastMove();

            if(result == Evaluation.Lost) {
                winningMoves.add(move);
                continue;
            }

            if(result != Evaluation.Win) {
                moves.add(move);
            }

        }

        if(!winningMoves.isEmpty()) {
            return winningMoves;
        }

        if(moves.isEmpty()) {
            return broaderMoveSelector.getMoves(board);
        }

        return moves;

    }

}
