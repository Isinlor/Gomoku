package Board;

import Board.Helpers.ApproximateMoveSelector;
import Contract.*;

import java.util.*;

public class SimpleBoard implements Board {

    private int boardSize;
    private BoardCell[][] boardState;
    protected Color currentTurn = Color.Black;

    private Color winner = null;

    private HashSet<Move> validMoves = new HashSet<>();

    private LinkedHashSet<Move> approximateValidMoves = new LinkedHashSet<>();

    private int madeMovesCounter;
    private Move[] moveHistory;

    public SimpleBoard(ReadableBoard board) {

        boardSize = board.getSize();
        currentTurn = board.getCurrentColor();
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

        moveHistory = new Move[boardSize * boardSize];
        for (int i = 0; i < board.getMadeMovesCounter(); i++) {
            appendMoveHistory(board.getMove(i));
        }

//        if(!isEmpty()) {
//            approximateValidMoves = new LinkedHashSet<>(new GlobalApproximateMoveSelector().getMoves(this));
//        }else{
//            approximateValidMoves = new LinkedHashSet<>();
//        }

    }

    private boolean isEmpty() {
        return validMoves.size() == boardSize * boardSize;
    }

    public int getMadeMovesCounter() {
        return madeMovesCounter;
    }

    public Move getMove(int index) {
        return moveHistory[index];
    }

    private void appendMoveHistory(Move move) {
        moveHistory[madeMovesCounter] = move;
        madeMovesCounter++;
    }

    private void truncateMoveHistory() {
        moveHistory[madeMovesCounter - 1] = null;
        madeMovesCounter--;
    }

    // TODO: Add unit tests
    public SimpleBoard(BoardCell[][] boardState) {

        int white = 0;
        int black = 0;
        boardSize = boardState.length;
        moveHistory = new Move[boardSize * boardSize];
        this.boardState = new BoardCell[boardSize][boardSize];
        for (int x = 0; x < boardSize; x++) {
            if(boardState[x].length != boardSize) {
                throw new RuntimeException("Board is not square!");
            }
            for (int y = 0; y < boardSize; y++) {
                switch (boardState[x][y]) {
                    case Black:
                        black++;
                        appendMoveHistory(new Move(x, y));
                        break;
                    case White:
                        appendMoveHistory(new Move(x, y));
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

        winner = getWinnerByFullBoardCheck(5);

//        if(black+white>0) {
//            approximateValidMoves = new LinkedHashSet<>(new GlobalApproximateMoveSelector().getMoves(this));
//        }

    }

    public SimpleBoard(int boardSize) {
        this.boardSize = boardSize;
        moveHistory = new Move[boardSize * boardSize];
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
        moveHistory = new Move[boardSize * boardSize];
        approximateValidMoves = new LinkedHashSet<>();
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
        return new BoardState(boardState, getCurrentColor(), getLastMove());
    }

    public int getSize() {
        return boardSize;
    }

    public Color getCurrentColor() {
        return currentTurn;
    }

    public Moves getValidMoves() {

        if(hasWinner()) {
            return new Moves(); // there are no valid moves if there is a winner
        }

        return new Moves(validMoves);

    }

    public Collection<Move> getApproximateValidMoves() {

        return new ApproximateMoveSelector().getMoves(this);

//        if(hasWinner()) {
//            return new ArrayList<>(); // there are no valid moves if there is a winner
//        }
//
//        if(approximateValidMoves.isEmpty()){
//            return getValidMoves().getCopy(); // the copy is necessary to avoid concurrent access error
//        }
//
//        return (Collection<Move>) approximateValidMoves.clone();
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
        if(madeMovesCounter == 0) return null;
        return moveHistory[madeMovesCounter - 1];
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
                currentTurn = Color.White;
                break;
            case White:
                boardState[x][y] = BoardCell.White;
                currentTurn = Color.Black;
                break;
        }

        appendMoveHistory(move);
        winner = getWinner(5);
        validMoves.remove(move);
//        updateApproximateMovesAfterMakingMove(move);
    }

    private final int[][] modifiers = {
            {-1, -1}, {-1, 0}, {-1,  1},
            { 0, -1},          { 0,  1},
            { 1, -1}, { 1, 0}, { 1,  1},
    };

    private Collection <Move> getSurroundingMoves(Move move) {
        Collection<Move> moves = new ArrayList<>();
        for (int[] modifier: modifiers) {
            int i = modifier[0];
            int j = modifier[1];
            if(
                move.x + i >= 0 && move.y + j >= 0 &&
                move.x + i < boardSize && move.y + j < boardSize &&
                this.getCell(move.x + i, move.y + j) == BoardCell.Empty
            ) {
                moves.add(new Move(move.x + i, move.y + j));
            }
        }
        return moves;
    }

    private Collection <Move> getSurroundingPieces(Move move) {
        Collection<Move> moves = new ArrayList<>();
        for (int[] modifier: modifiers) {
            int i = modifier[0];
            int j = modifier[1];
            if(
                move.x + i >= 0 && move.y + j >= 0 &&
                move.x + i < boardSize && move.y + j < boardSize &&
                this.getCell(move.x + i, move.y + j) != BoardCell.Empty
            ) {
                moves.add(new Move(move.x + i, move.y + j));
            }
        }
        return moves;
    }

    private boolean hasSurroundingPieces(Move move){
        return !this.getSurroundingPieces(move).isEmpty();
    }

    private void updateApproximateMovesAfterMakingMove(Move move) {
        approximateValidMoves.addAll(this.getSurroundingMoves(move));
        approximateValidMoves.remove(move);
   }

    private void updateApproximateMovesAfterRevertingMove(Move move) {

        if(validMoves.size()==boardSize * boardSize) {
            approximateValidMoves = new LinkedHashSet<>();
            return;
        }

        if(hasSurroundingPieces(move)){
            approximateValidMoves.add(move);
        }

        for (Move surroundingMove: this.getSurroundingMoves(move)) {
            if(!hasSurroundingPieces(surroundingMove)){
                approximateValidMoves.remove(surroundingMove);
            }
        }

    }

    public void revertLastMove() {
        revertMove(getLastMove());
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
                break;
            case White:
                boardState[x][y] = BoardCell.Empty;
                currentTurn = Color.Black;
                break;
        }

        winner = null;
        validMoves.add(move);
        truncateMoveHistory();
//        updateApproximateMovesAfterRevertingMove(move);
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

        Color lastColor = currentTurn.getOpposite();
        Move lastMove = getLastMove();

        int x = lastMove.x;
        int y = lastMove.y;

        for (int j = -1; j <= 1; j++) {
            for (int i = -1; i <= 1; i++) {

                if (!isOnBoard(x + i) || !isOnBoard(y + j) || (i == 0 && j == 0)) {
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
                    if (isOnBoard(x + counter * i) && isOnBoard(y + counter * j)) {
                        if (boardState[x + (counter * i)][y + (counter * j)].getColor() == lastColor) {
                            counter++;
                        } else {
                            stopCount++;
                        }
                    } else {
                        stopCount++;
                    }
                    if (isOnBoard(x - reverseCounter * i) && isOnBoard(y - reverseCounter * j)) {
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

    public boolean isOnBoard(int index) {
        return index >= 0 && index < boardSize;
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
        stringBuilder.append("Last " + getLastMove() + "\n");
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
                if(getLastMove() != null && getLastMove().x == x && getLastMove().y == y) {
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
                if(getLastMove() != null && getLastMove().x == x && getLastMove().y == y) {
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
