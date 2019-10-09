package Evaluation;

import Contract.Evaluation;
import Contract.Move;
import Contract.ReadableBoard;

/**
 * The most simple evaluation possible:
 * -> Win is good, loose is bad.
 */
public class WinLossEvaluation implements Evaluation {
    public double evaluate(ReadableBoard board, Move move) {
        board = board.getWithMove(move);
        if(board.hasWinner()) {
            return board.getWinner() != board.getCurrentColor() ? Double.MAX_VALUE : -Double.MAX_VALUE;
        }
        return 0;
    }
}
