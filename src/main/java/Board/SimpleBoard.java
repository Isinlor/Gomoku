package Board;

import Contract.*;

import java.util.*;

public class SimpleBoard implements Board {

    private int boardSize;
    private BoardCell[][] boardState;
    protected Color currentTurn = Color.Black;

    private Move lastMove;
    private Color lastColor;

    private Color winner = null;

    private HashSet<Move> validMoves = new HashSet<>();

    public SimpleBoard(ReadableBoard board) {

        boardSize = board.getSize();
        currentTurn = board.getCurrentColor();
        lastMove = board.getLastMove();
        lastColor = board.getCurrentColor().getOpposite();
        winner = board.getWinner();

        this.boardState = new BoardCell[boardSize][boardSize];
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                BoardCell cell = board.getCell(x, y);
                this.boardState[x][y] = cell;
                if(cell == BoardCell.Empty) {
                    validMoves.add(new Move(x, y));
                }
            }
        }

    }

    // TODO: Add unit tests
    public SimpleBoard(BoardCell[][] boardState) {

        int white = 0;
        int black = 0;
        boardSize = boardState.length;
        this.boardState = new BoardCell[boardSize][boardSize];
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
                    case Empty:
                        validMoves.add(new Move(x, y));
                        break;
                }
                // make sure that board state is not mutable outside the class
                this.boardState[x][y] = boardState[x][y];
            }
        }

        if(black > (white + 1) || white > (black + 1)) {
            throw new RuntimeException("Board is not balanced! There are to many white or black pieces!");
        }

        if(black > white) {
            currentTurn = Color.White;
        }

        winner = getWinner(5);

    }

    public SimpleBoard(int boardSize) {
        this.boardSize = boardSize;
        boardState = new BoardCell[boardSize][boardSize];
        for (int x = 0; x < boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                boardState[x][y] = BoardCell.Empty;
                validMoves.add(new Move(x, y));
            }
        }
    }

    public void resetBoard(){
        validMoves = new HashSet<>();
        for (int x = 0; x <boardSize; x++) {
            for (int y = 0; y < boardSize; y++) {
                boardState[x][y]=BoardCell.Empty;
                validMoves.add(new Move(x, y));
            }
        }
        currentTurn = Color.Black;
        winner = null;
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

    public BoardState getBoardState() {
        return new BoardState(getCurrentColor(), boardState);
    }

    public int getSize() {
        return boardSize;
    }

    public Color getCurrentColor() {
        return currentTurn;
    }

    public Moves getValidMoves() {

        if(winner != null) {
            return new Moves(); // there are no valid moves if there is a winner
        }

        return new Moves(validMoves);

    }

    public boolean isValidMove(Move move) {
        if(winner != null) {
            return false;
        }

        if(move.x < 0 || move.y < 0 || move.x >= boardSize || move.y >= boardSize) {
            return false;
        }

        if(boardState[move.x][move.y] != BoardCell.Empty) {
            return false;
        }

        return true;
    }

    public Move getLastMove() {
        return lastMove;
    }

    public void move(Move move) throws WrongMoveException {

        if(winner != null) {
            throw new WrongMoveException("This board is finished! The game was won by: " + winner.name());
        }

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
                lastMove = move;
                lastColor = getCurrentColor();
                currentTurn = Color.White;
                break;
            case White:
                boardState[x][y] = BoardCell.White;
                lastMove = move;
                lastColor = getCurrentColor();
                currentTurn = Color.Black;
                break;
        }

        winner = getWinner(5);
        validMoves.remove(new Move(x, y));

    }

    public void revertMove(Move move) throws WrongMoveException {

        int x = move.x;
        int y = move.y;

        if(x < 0 || y < 0 || x >= boardSize || y >= boardSize) {
            throw new WrongMoveException(
                "Move x " + x + " y " + y + " is outside the board!\n" +
                    "Board size: " + getSize() + "; Board indexing starts at 0."
            );
        }

        if(boardState[x][y] == BoardCell.Empty) {
            throw new WrongMoveException("The cell x " + x + " y " + y + " is empty!");
        }

        switch (getCurrentColor()) {
            case Black:
                boardState[x][y] = BoardCell.Empty;
                currentTurn = Color.White;
                lastColor = null;
                lastMove = null;
                break;
            case White:
                boardState[x][y] = BoardCell.Empty;
                currentTurn = Color.Black;
                lastColor = null;
                lastMove = null;
                break;
        }

        winner = null;
        validMoves.add(new Move(x, y));

    }

    public SimpleBoard getWithMove(Move move) throws WrongMoveException {
        SimpleBoard newBoard = new SimpleBoard(this);
        newBoard.move(move);
        return newBoard;
    }

    public boolean isGameFinished() {
        return hasWinner() || isFull();
    }

    public boolean isFull() {
        return validMoves.isEmpty();
    }

    public boolean hasWinner() {
        return getWinner() != null;
    }

    public boolean hasWinner( int steps ) {
        return getWinner(steps)!=null;
    }

    public Color getWinner() {
        return winner;
    }

    //RETURNS WINNING COLOR OR NULL IF THERE'S A LOSS
    public Color getWinner(int steps) throws NoWinnerException {

        if (lastMove == null || lastColor == null) {
            return getWinnerByFullBoardCheck(steps);
        }

        int x = lastMove.x;
        int y = lastMove.y;
        for (int j = -1; j <= 1; j++) {
            for (int i = -1; i <= 1; i++) {

                if (!numberIsOnBoard(x + i) || !numberIsOnBoard(y + j) || (i == 0 && j == 0)) {
                    continue;
                }
                if (boardState[x + i][y + j].getColor() != lastColor) {
                    continue;
                }

                int counter = 2;
                int reverseCounter = 1;
                int stopCount = 0;
                while (counter + reverseCounter - 1 < steps && stopCount != 2) {
                    stopCount = 0;
                    if (numberIsOnBoard(x + counter * i) && numberIsOnBoard(y + counter * j)) {
                        if (boardState[x + (counter * i)][y + (counter * j)].getColor() == lastColor) {
                            counter++;
                        } else {
                            stopCount++;
                        }
                    } else {
                        stopCount++;
                    }
                    if (numberIsOnBoard(x - reverseCounter * i) && numberIsOnBoard(y - reverseCounter * j)) {
                        if (boardState[x - (reverseCounter * i)][y - (reverseCounter * j)].getColor() == lastColor) {
                            reverseCounter++;
                        } else {
                            stopCount++;
                        }
                    } else {
                        stopCount++;
                    }
                }
                if (counter + reverseCounter - 1 == steps) {
                    return lastColor;
                }
            }
        }
        return null;
    }

    public boolean numberIsOnBoard(int number) {
        return number >= 0 && number < boardSize;
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

    public Color getWinnerByFullBoardCheck(int steps) throws NoWinnerException {
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
        stringBuilder.append("Next Move: " + getCurrentColor() + "\n");
        stringBuilder.append("Last " + lastMove + "\n");
        stringBuilder.append("\tY ");
        for (int i = 0; i < getSize(); i++) {
            stringBuilder.append(i % 10 + " ");
        }
        stringBuilder.append("\nX\t  ");
        for (int i = 0; i < getSize(); i++) {
            stringBuilder.append("--");
        }
        stringBuilder.append("\n");
        for (int x = 0; x < getSize(); x++) {
            stringBuilder.append(x+"\t| ");
            for (int y = 0; y < getSize(); y++) {
                if(lastMove != null && lastMove.x == x && lastMove.y == y) {
                    stringBuilder.append("\u001b[1m\u001b[32m");
                }
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
                if(lastMove != null && lastMove.x == x && lastMove.y == y) {
                    stringBuilder.append("\u001b[0m");
                }
                stringBuilder.append(" ");
            }
            stringBuilder.append("|\n");
        }
        stringBuilder.append(" \t  ");
        for (int i = 0; i < getSize(); i++) {
            stringBuilder.append("--");
        }
        return stringBuilder.toString();
    }
}
