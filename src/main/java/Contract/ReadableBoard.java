package Contract;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import Board.Moves;

public interface ReadableBoard {

    int getMadeMovesCounter();

    Move getMove(int index);

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
     * @return The board cell states.
     */
    BoardState getBoardState();

    /**
     * @return The size of the board.
     */
    int getSize();

    /**
     * @return Indicate whose turn it is.
     */
    Color getCurrentColor();

    boolean isGameFinished();

    boolean hasWinner();

    Color getWinner() throws NoWinnerException;

    Collection<Move> getApproximateValidMoves();

    Moves getValidMoves();

    boolean isValidMove(Move move);

    boolean isOnBoard(int index);

    Move getLastMove();

    /**
     * Returns *new board* with the given move.
     *
     * The changes to the new board will not affect the old board.
     */
    ReadableBoard getWithMove(Move move) throws WrongMoveException;

}
