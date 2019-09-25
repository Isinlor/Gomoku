package Board;

import Contract.*;
import javafx.scene.control.Cell;

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
        try {
            return boardState[x][y];
        } catch (Exception e) {
            String message = "" +
                "Cell x " + x + " y " + y + " requested. " +
                "Board size " + getSize() + ".";
            throw new RuntimeException(
                message, e
            );
        }
    }

    public int getSize() {
        return boardSize;
    }

    public Color getCurrentColor() {
        return currentTurn;
    }

    public boolean isValidMove(Move move) {
        if(move.x < 0 || move.y < 0 || move.x >= boardSize || move.y >= boardSize) {
            return false;
        }

        if(boardState[move.x][move.y] != BoardCell.Empty) {
            return false;
        }

        return true;
    }

    public void move(Move move) throws WrongMoveException {

        int x = move.x;
        int y = move.y;
        
        if(!isValidMove(move)) {
            if(x < 0 || y < 0 || x >= boardSize || y >= boardSize) {
                throw new WrongMoveException(
                    "Move x " + x + " y " + y + " is outside the board!\n" +
                        "Board size: " + getSize() + "; Board indexing starts at 0."
                );
            }

            if(boardState[x][y] != BoardCell.Empty) {
                throw new WrongMoveException("The cell x " + x + " y " + y + " is not empty!");
            }
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
        return hasWinner(5);
    }

    public boolean checkRows(int x, int y){
        return checkRows(x, y, 5);
    }

    public boolean checkRows(int x, int y, int steps) {
        int boardSize = this.getSize();
        if(x >= boardSize || y >= boardSize) {
            throw new RuntimeException("Can't check at that position, the location is out of the board!");
        }
        if(boardState[x][y]==BoardCell.Empty) {
            return false;
        }
        return checkHorizontalRow(x, y, steps) || checkVerticalRow(x, y, steps) || checkDiagonalRowBLTR(x,y,steps) || checkDiagonalRowTRBL(x,y,steps);
    }

    private boolean checkHorizontalRow(int x, int y, int steps) {
        BoardCell color = boardState[x][y];
        int rowLength = 1;

        for (int column = y-1; column >= 0 && column >= y - steps; column--) {
            if (boardState[x][column] == color) {
                rowLength++;
                if (rowLength == steps) return true;
            }else {
                break;
            }
        }
        for (int column = y+1; column < getSize() && column < y + steps ; column++) {
            if (boardState[x][column] == color) {
                rowLength++;
                if (rowLength == steps) return true;
            }else {
                break;
            }
        }
        return false;
    }

    private boolean checkVerticalRow(int x, int y, int steps) {
        BoardCell color = boardState[x][y];
        int rowLength = 1;

        for (int row = x-1; row >= 0 && row >= x - steps; row--) {
            if (boardState[row][y] == color) {
                rowLength++;
                if (rowLength == steps) return true;
            }else {
                break;
            }
        }
        for (int row = x+1; row < getSize() && row < x + steps ; row++) {
            if (boardState[row][y] == color) {
                rowLength++;
                if (rowLength == steps) return true;
            }else {
                break;
            }
        }
        return false;
    }

    //Check the diagonal row from bottom, left to top, right
    private boolean checkDiagonalRowBLTR(int x, int y, int steps) {
        BoardCell color = boardState[x][y];
        int rowLength = 1;

        for (int step = 1; (x - step) >= 0 && (x - step) >= (x - steps) && (y-step) >= 0 && (y-steps) >= (y - steps); step++) {
            if (boardState[x - step][y - step] == color) {
                rowLength++;
                if (rowLength == steps) return true;
            }else {
                break;
            }
        }
        for (int step = 1; (x + step) < getSize() && (x + step) < (x + steps) && (y + step) < getSize() && (y + step) < (y + steps); step++) {
            if (boardState[x + step][y + step] == color) {
                rowLength++;
                if (rowLength == steps) return true;
            }else {
                break;
            }
        }
        return false;
    }

    //Check the diagonal row from top, right to bottom, left
    private boolean checkDiagonalRowTRBL(int x, int y, int steps) {
        BoardCell color = boardState[x][y];
        int rowLength = 1;
        for (int step = 1; (x - step) >= 0 && (x - step) >= (x - steps) && (y + step) < getSize() && (y + step) < (y + steps); step++) {
            if (boardState[x - step][y + step] == color) {
                rowLength++;
                if (rowLength == steps) return true;
            }else {
                break;
            }
        }
        for (int step = 1; (x + step) < getSize() && (x + step) < (x + steps) && (y-step) >= 0 && (y-steps) >= (y - steps); step++) {
            if (boardState[x + step][y - step] == color) {
                rowLength++;
                if (rowLength == steps) return true;
            }else {
                break;
            }
        }
        return false;
    }

    public boolean hasWinner( int steps ) {
        return getWinner(steps)!=null;
    }

    public Color getWinner() throws NoWinnerException {
        return getWinner(5);
    }

    public Color getWinner(int steps) throws NoWinnerException {
        for (int x = 0; x < this.getSize(); x++) {
            for (int y = 0; y < this.getSize(); y++) {
                if (this.checkRows(x, y, steps)) {
                    return boardState[x][y].getColor();
                }
            }
        }
        return null;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Board (" + getSize() + "x" + getSize() + "):\n");
        stringBuilder.append("Current color: " + getCurrentColor() + "\n");
        for (int x = 0; x < getSize(); x++) {
            for (int y = 0; y < getSize(); y++) {
                switch (getCell(x, y)) {
                    case Black:
                        stringBuilder.append("B");
                        break;
                    case White:
                        stringBuilder.append("W");
                        break;
                    case Empty:
                        stringBuilder.append(" ");
                        break;
                }
                stringBuilder.append(" ");
            }
            stringBuilder.append("\n");
        }
        return stringBuilder.toString();
    }
}
