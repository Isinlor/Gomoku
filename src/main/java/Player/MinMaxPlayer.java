package Player;

import Contract.*;
import Board.SimpleBoard;

public class MinMaxPlayer implements Player {

    private Evaluation evaluation;
    private MoveSelector moveSelector;
    private int depth;

    public MinMaxPlayer(Evaluation evaluation, MoveSelector moveSelector, int depth) {
        this.evaluation = evaluation;
        this.moveSelector = moveSelector;
        this.depth = depth;
    }

    public Move getMove(ReadableBoard givenBoard) {

        SimpleBoard board = new SimpleBoard(givenBoard);

        Move bestMove = null;
        double bestEvaluation = Double.NaN;

        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        for (Move move: moveSelector.getMoves(board)) {

            double moveEvaluation = min(board, move, depth, alpha, beta);
            if(bestMove == null || moveEvaluation > bestEvaluation) {
                bestMove = move;
                bestEvaluation = moveEvaluation;
                alpha = moveEvaluation;
                if(alpha >= beta) {
                    break;
                }
            }

        }

        return bestMove;

    }

    private double max(SimpleBoard board, Move move, int depth, double alpha, double beta) {

        board.move(move);

        if(depth == 0 || board.isGameFinished()) {
            double eval = evaluation.evaluate(board);
            board.revertMove(move);
            return eval;
        }

        Move bestNextMove = null;
        double bestEvaluation = Double.NaN;

        for (Move nextMove: moveSelector.getMoves(board)) {

            double nextMoveEvaluation = min(board, nextMove, depth - 1, alpha, beta);
            if(bestNextMove == null || nextMoveEvaluation > bestEvaluation) {
                bestNextMove = nextMove;
                bestEvaluation = nextMoveEvaluation;
                alpha = bestEvaluation;
                if(alpha >= beta || bestEvaluation == Evaluation.Win) {
                    break;
                }
            }

        }

        board.revertMove(move);

        return bestEvaluation;

    }

    private double min(SimpleBoard board, Move move, int depth, double alpha, double beta) {

        board.move(move);

        if(depth == 0 || board.isGameFinished()) {
            double eval = -evaluation.evaluate(board);
            board.revertMove(move);
            return eval;
        }

        Move bestNextMove = null;
        double bestEvaluation = Double.NaN;

        for (Move nextMove: moveSelector.getMoves(board)) {

            double nextMoveEvaluation = max(board, nextMove, depth - 1, alpha, beta);
            if(bestNextMove == null || nextMoveEvaluation < bestEvaluation) {
                bestNextMove = nextMove;
                bestEvaluation = nextMoveEvaluation;
                beta = bestEvaluation;
                if(alpha >= beta || bestEvaluation == Evaluation.Lost) {
                    break;
                }
            }

        }

        board.revertMove(move);

        return bestEvaluation;

    }

}
