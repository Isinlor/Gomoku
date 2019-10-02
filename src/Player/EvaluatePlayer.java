package Player;

import Contract.*;

public class EvaluatePlayer implements Player {

    private Evaluation evaluation;

    EvaluatePlayer(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Move getMove(ReadableBoard board) {
        Move bestMove = null;
        double bestEvaluation = Double.MIN_VALUE;
        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                if(board.getCell(x, y) == BoardCell.Empty) {
                    Move move = new Move(x, y);
                    double moveEvaluation = evaluation.evaluate(board.getWithMove(new Move(x, y)));
                    if(moveEvaluation >= bestEvaluation) {
                        bestMove = move;
                        bestEvaluation = moveEvaluation;
                    }
                }
            }
        }
        return bestMove;
    }
}
