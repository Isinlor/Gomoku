package Player;

import Contract.*;

public class EvaluatePlayer implements Player {

    private Evaluation evaluation;

    public EvaluatePlayer(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Move getMove(ReadableBoard board) {
        Move bestMove = null;
        double worstEvaluation = Double.MAX_VALUE;
        for (int x = 0; x < board.getSize(); x++) {
            for (int y = 0; y < board.getSize(); y++) {
                if(board.getCell(x, y) == BoardCell.Empty) {
                    Move move = new Move(x, y);
                    // because the evaluation is from perspective of the other color
                    // the best move is the one with the worst evaluation
                    double moveEvaluation = evaluation.evaluate(board.getWithMove(new Move(x, y)));
                    if(moveEvaluation <= worstEvaluation) {
                        bestMove = move;
                        worstEvaluation = moveEvaluation;
                    }
                }
            }
        }
        return bestMove;
    }
}
