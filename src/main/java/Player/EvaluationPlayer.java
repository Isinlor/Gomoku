package Player;

import Board.*;
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

        SimpleBoard boardCopy = new SimpleBoard(board);
        for (Move move: moveSelector.getMoves(board)) {

            boardCopy.move(move);
            double moveEvaluation = evaluation.evaluate(boardCopy);
            boardCopy.revertLastMove();

            if(bestMove == null || moveEvaluation < bestEvaluation) {
                bestMove = move;
                bestEvaluation = moveEvaluation;
            }

            Logger.log(move + " " + moveEvaluation);

        }

        return bestMove;
    }
}
