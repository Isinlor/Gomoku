package Evaluation;

import Contract.*;
import Board.*;

import java.util.Collection;

public class NegamaxEvaluation implements Evaluation {

    private Evaluation evaluation;
    private MoveSelector moveSelector;
    private int depth;

    public NegamaxEvaluation(Evaluation evaluation, MoveSelector moveSelector, int depth) {
        this.evaluation = evaluation;
        this.moveSelector = moveSelector;
        this.depth = depth;
    }

    public double evaluate(ReadableBoard board) {
        return evaluate(
            new SimpleBoard(board), depth,
            Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY
        );
    }

    public double evaluate(SimpleBoard board, int depth, double alpha, double beta) {

        if(depth == 0 || board.isGameFinished()) {
            return evaluation.evaluate(board);
        }

        Collection<Move> moves = moveSelector.getMoves(board);
        double value = Double.NEGATIVE_INFINITY;
        for (Move move: moves) {
            board.move(move);
            value = Math.max(value, -evaluate(board, depth - 1, -beta, -alpha));
            board.revertLastMove();
            alpha = Math.max(alpha, value);
            if(alpha >= beta || value == Win) {
                break;
            }
        }

        return value;

    }

}
