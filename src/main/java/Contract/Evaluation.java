package Contract;

public interface Evaluation {

     final static double Win = Double.MAX_VALUE;
     final static double Lost = -Double.MAX_VALUE;

    /**
     * Evaluate the board given the move from the perspective of the current player.
     * A good board for the current player should give a high evaluation.
     */
    double evaluate(ReadableBoard board);

}
