package Board;

import Contract.*;

public class Board implements BoardInterface {

    private int boardSize;
    private Color currentTurn = Color.Black;
    private BoardCell[][] boardState;

    // TODO: Add unit tests
    public Board(BoardCell[][] boardState) {

        int white = 0;
        int black = 0;
        boardSize = boardState.length;
        for (int x = 0; x < boardSize; x++) {
            if(boardState[x].length != boardSize) {
                throw new RuntimeException("Board is not square!");
            }
            for (int y = 0; y < boardSize; y++) {
                switch (boardState[x][y]) {
                    case Black:
                        black++;
                        break;
                    case White:
                        white++;
                        break;
                }
            }
        }

        if(black > (white + 1) || white > (black + 1)) {
            throw new RuntimeException("Board is not balanced! There are to many white or black pieces!");
        }

        if(black > white) {
            currentTurn = Color.White;
        }

        this.boardState = boardState;

    }

    public Board(int boardSize) {
        this.boardSize = boardSize;
        boardState = new BoardCell[boardSize][boardSize];
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                boardState[x][y] = BoardCell.Empty;
            }
        }
    }

    public BoardCell getCell(int x, int y) {
        return boardState[x][y];
    }

    public int getSize() {
        return boardSize;
    }

    public Color getCurrentColor() {
        return currentTurn;
    }

    public void move(int x, int y) throws WrongMoveException {

        if(boardState[x][y] != BoardCell.Empty) {
            throw new WrongMoveException();
        }
        if(x >= boardSize || y >= boardSize) {
            throw new WrongMoveException();
        }

        switch (getCurrentColor()) {
            case Black:
                boardState[x][y] = BoardCell.Black;
                currentTurn = Color.White;
                break;
            case White:
                boardState[x][y] = BoardCell.White;
                currentTurn = Color.Black;
                break;
        }

    }

    public boolean hasWinner() {
        return false;
    }

    public Color getWinner() throws NoWinnerException {
        return null;
    }

}
