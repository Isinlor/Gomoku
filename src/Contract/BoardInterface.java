package Contract;

public interface BoardInterface {

    /**
     * An array of BoardCells.
     *
     * We start counting at 0 from left down corner.
     *
     * 1 │
     * 0 │
     * y └────
     *   x 0  1
     *
     * @return The board cells indexed as [x][y].
     */
    BoardCell[][] getCells();

    /**
     * @return The size of the board.
     */
    int getSize();

    /**
     * @return
     */
    Color getCurrentTurn();

    void move(int x, int y) throws WrongMoveException;

    boolean hasWinner();

    Color getWinner() throws NoWinnerException;

}