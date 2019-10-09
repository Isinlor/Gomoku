package Contract;

public interface Evaluation {

    /**
     * Evaluate the board given the move from the perspective of the current player.
     * A good board for the current player should give a high evaluation.
     */
    double evaluate(ReadableBoard board, Move move);

}
