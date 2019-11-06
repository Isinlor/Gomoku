package Player;

import Contract.*;
import Board.SimpleBoard;

public class MinMaxPlayer implements Player {

    private SimpleBoard board;
    private Evaluation evaluation;
    private MoveSelector moveSelector;

    public MinMaxPlayer(Evaluation evaluation, MoveSelector moveSelector) {
        this.evaluation = evaluation;
        this.moveSelector = moveSelector;
    }

    public Move getMove(ReadableBoard board) {

        this.board = new SimpleBoard(board);

        Move bestMove = null;
        double bestEvaluation = Double.NaN;

        for (Move move: moveSelector.getMoves(board)) {

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

        for (Move nextMove: moveSelector.getMoves(board)) {

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

        for (Move nextMove: moveSelector.getMoves(board)) {

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
