package Player;

import Contract.*;
import Board.SimpleBoard;

public class MinMaxPlayer implements Player {

    private SimpleBoard board;
    private Evaluation evaluation;

    public MinMaxPlayer(Evaluation evaluation) {
        this.evaluation = evaluation;
    }

    public Move getMove(ReadableBoard board) {

        this.board = new SimpleBoard(board);

        Move bestMove = null;
        double bestEvaluation = Double.NaN;

        for (Move move: board.getValidMoves()) {

            double moveEvaluation = min(move, 3);
            if(bestMove == null || moveEvaluation > bestEvaluation) {
                bestMove = move;
                bestEvaluation = moveEvaluation;
            }

        }

        return bestMove;

    }

    public double max(Move move, int depth) {

        board.move(move);

        if(board.isGameFinished() || depth == 0) {
            double eval = evaluation.evaluate(board);
            board.revertMove(move);
            return eval;
        }

        Move bestNextMove = null;
        double bestEvaluation = Double.NaN;

        for (Move nextMove: board.getValidMoves()) {

            double nextMoveEvaluation = min(nextMove, depth - 1);
            if(bestNextMove == null || nextMoveEvaluation > bestEvaluation) {
                bestNextMove = nextMove;
                bestEvaluation = nextMoveEvaluation;
            }

        }

        board.revertMove(move);

        return bestEvaluation;

    }

    private double min(Move move, int depth) {

        board.move(move);

        if(board.isGameFinished() || depth == 0) {
            double eval = -evaluation.evaluate(board);
            board.revertMove(move);
            return eval;
        }

        Move bestNextMove = null;
        double bestEvaluation = Double.NaN;

        for (Move nextMove: board.getValidMoves()) {

            double nextMoveEvaluation = max(nextMove, depth - 1);
            if(bestNextMove == null || nextMoveEvaluation < bestEvaluation) {
                bestNextMove = nextMove;
                bestEvaluation = nextMoveEvaluation;
            }

        }

        board.revertMove(move);

        return bestEvaluation;

    }

}
