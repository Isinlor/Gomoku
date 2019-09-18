interface BoardInterface {

    /**
     * An array of BoardCells.
     *
     * We start counting at 0 from left down corner.
     *
     * 1 │
     * 0 │
     *   └────
     *    0  1
     *
     * @return The board state.
     */
    BoardCell[] getBoardState();

    /**
     * @return The size of the board.
     */
    int getBoardSize();

    /**
     * @return
     */
    Color getCurrentColor();

    void move(int x, int y) throws WrongMoveException;

    boolean hasWinner();

    Color getWinner() throws NoWinnerException;

}