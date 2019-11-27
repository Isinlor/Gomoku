package Board.Helpers;

import Contract.Evaluation;
import Contract.Move;
import Contract.MoveSelector;
import Contract.ReadableBoard;

import java.util.HashSet;
import java.util.Set;

public class ForcedMoveSelector implements MoveSelector {

    private Evaluation evaluation;
    private MoveSelector broaderMoveSelector;

    public ForcedMoveSelector(Evaluation evaluation, MoveSelector broaderMoveSelector) {
        this.evaluation = evaluation;
        this.broaderMoveSelector = broaderMoveSelector;
    }

    public Set<Move> getMoves(ReadableBoard board) {

        HashSet<Move> moves = new HashSet<>();
        HashSet<Move> winningMoves = new HashSet<>();
        for (Move move: broaderMoveSelector.getMoves(board)) {

            double result = evaluation.evaluate(board.getWithMove(move));

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
