package Evaluation;

import Contract.Evaluation;
import Contract.Move;
import Contract.ReadableBoard;

/**
 * The most simple evaluation possible:
 * -> Win is good, loose is bad.
 */
public class WinLossEvaluation implements Evaluation {
    public double evaluate(ReadableBoard board) {
        if(board.hasWinner()) {
            return board.getWinner() == board.getCurrentColor() ? Win : Lost;
        }
        return 0;
    }
}
