package Evaluation;

import Contract.*;

public class ExtendedWinLossEvaluation implements Evaluation {
    private BoardCell E = BoardCell.Empty;
    public double evaluate(ReadableBoard board) {
        if(board.hasWinner()) {
            return board.getWinner() == board.getCurrentColor() ? Win : Lost;
        }
        if(isLosing(board)) return -1;
        return 0;
    }
    private boolean isLosing(ReadableBoard board) {
        Move last = board.getLastMove();
        if(last == null) return false;
        int threats = 0;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                threats += countThreats(board, last.x + i, last.y + j);
                if(threats >= 2) return true;
            }
        }
        return false;
    }
    private int countThreats(ReadableBoard board, int x, int y) {
        Color opposite = board.getCurrentColor().getOpposite();
        BoardCell O = opposite == Color.White ? BoardCell.White : BoardCell.Black;
        if(!board.isOnBoard(x) || !board.isOnBoard(y) || board.getCell(x, y) != O) return 0;
        for (int x2 = -2; x2 <= 2; x2 += 4) {
            for (int y2 = -2; y2 <= 2; y2 += 4) {
                if(!board.isOnBoard(x + x2) || !board.isOnBoard(y + y2)) return 0;
            }
        }
        int threats = 0;
        boolean horizontal =
            (board.getCell(x - 2, y) == E || board.getCell(x - 2, y) == O) &&
            (board.getCell(x + 2, y) == E || board.getCell(x + 2, y) == O) &&
             board.getCell(x - 1, y) == O && board.getCell(x + 1, y) == O;
        if(horizontal) threats++;
        boolean vertical =
            (board.getCell(x, y - 2) == E || board.getCell(x, y - 2) == O) &&
            (board.getCell(x, y + 2) == E || board.getCell(x, y + 2) == O) &&
             board.getCell(x, y - 1) == O && board.getCell(x, y + 1) == O;
        if(vertical) threats++;
        boolean backslash =
            (board.getCell(x + 2, y - 2) == E || board.getCell(x + 2, y - 2) == O) &&
            (board.getCell(x - 2, y + 2) == E || board.getCell(x - 2, y + 2) == O) &&
             board.getCell(x + 1, y - 1) == O && board.getCell(x - 1, y + 1) == O;
        if(backslash) threats++;
        boolean forwardslash =
            (board.getCell(x - 2, y - 2) == E || board.getCell(x - 2, y - 2) == O) &&
            (board.getCell(x + 2, y + 2) == E || board.getCell(x + 2, y + 2) == O) &&
             board.getCell(x - 1, y - 1) == O && board.getCell(x + 1, y + 1) == O;
        if(forwardslash) threats++;
        return threats;
    }
}
