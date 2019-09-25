package Contract;

public interface ReadableBoard {

    /**
     * Return the state of a board cell.
     *
     * We start counting at 0 from left top corner.
     *
     *   x 0 1
     * y ┌────
     * 0 │
     * 1 │
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

    boolean hasWinner();

    Color getWinner() throws NoWinnerException;

    boolean isValidMove(Move move);

}
