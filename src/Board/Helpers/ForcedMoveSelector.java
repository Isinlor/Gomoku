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
        for (Move move: broaderMoveSelector.getMoves(board)) {

            double evaluation = this.evaluation.evaluate(board.getWithMove(move));

            if(evaluation == Evaluation.Lost) {
                moves = new HashSet<>();
                moves.add(move);
                return moves;
            }

            if(evaluation != Evaluation.Win) {
                moves.add(move);
            }

        }

        if(moves.isEmpty()) {
            return broaderMoveSelector.getMoves(board);
        }

        return moves;

    }

}
