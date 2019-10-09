package Player;

import Contract.*;

public class EvaluationPlayer implements Player {

    private Evaluation evaluation;

    public EvaluationPlayer(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Move getMove(ReadableBoard board) {
        Move bestMove = null;
        double bestEvaluation = Double.NaN;

        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                if(board.getCell(x, y) == BoardCell.Empty) {

                    Move move = new Move(x, y);

                    // because the evaluation is from perspective of the other color
                    // the best move is the one with the worst evaluation
                    double moveEvaluation = evaluation.evaluate(board, new Move(x, y));
                    if(bestMove == null || moveEvaluation > bestEvaluation) {
                        bestMove = move;
                        bestEvaluation = moveEvaluation;
                    }

                }
            }
        }

        return bestMove;
    }
}
