package Board;

import Contract.*;

public class Board implements BoardInterface {

    public BoardCell[] getBoardState() {
        return new BoardCell[0];
    }

    public int getBoardSize() {
        return 0;
    }

    public Color getCurrentColor() {
        return null;
    }

    public void move(int x, int y) throws WrongMoveException {

    }

    public boolean hasWinner() {
        return false;
    }

    public Color getWinner() throws NoWinnerException {
        return null;
    }

}
