package Contract;

public interface BoardInterface extends MutableBoard {

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

    boolean hasWinner();
    boolean hasWinner(int steps);

    Color getWinner() throws NoWinnerException;

}