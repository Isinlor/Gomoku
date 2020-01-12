package Player;

import Contract.*;
import UI.Logger;

public class EvaluationPlayer implements Player {

    private Evaluation evaluation;
    private MoveSelector moveSelector;

    public EvaluationPlayer(Evaluation evaluation, MoveSelector moveSelector) {
        this.evaluation = evaluation;
        this.moveSelector = moveSelector;
    }

    public Move getMove(ReadableBoard board) {
        Move bestMove = null;
        double bestEvaluation = Double.POSITIVE_INFINITY;

        for (Move move: moveSelector.getMoves(board)) {

            double moveEvaluation = evaluation.evaluate(board.getWithMove(move));
            if(bestMove == null || moveEvaluation < bestEvaluation) {
                bestMove = move;
                bestEvaluation = moveEvaluation;
            }

            Logger.log(move + " " + moveEvaluation);

        }

        return bestMove;
    }
}
