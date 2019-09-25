package Contract;

public interface MutableBoard {

    /**
     * Allows to make a move. The move will be made by the current color.
     *
     * The move must be inside board.
     * The move must not be made on already taken cell.
     */
    void move(Move move) throws WrongMoveException;

    boolean isValidMove(Move move);

}
