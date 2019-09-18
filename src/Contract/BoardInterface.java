package Contract;

public interface BoardInterface {

    /**
     * Return the state of a board cell.
     *
     * We start counting at 0 from left down corner.
     *
     * 1 │
     * 0 │
     * y └────
     *   x 0  1
     *
     * @return The board cell state.
     */
    BoardCell getCell(int x, int y);

    /**
     * @return The size of the board.
     */
    int getSize();

    /**
     * @return Indicate whose turn it is.
     */
    Color getCurrentColor();

    /**
     * Allows to make a move. The move will be made by the current color.
     *
     * The move must be inside board.
     * The move must not be made on already taken cell.
     */
    void move(int x, int y) throws WrongMoveException;

    boolean hasWinner();

    Color getWinner() throws NoWinnerException;

}