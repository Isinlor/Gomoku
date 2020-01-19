package Evaluation;

import Contract.BoardCell;
import Contract.Color;
import Contract.Evaluation;
import Contract.ReadableBoard;

public class CountEvaluation implements Evaluation {

    public double evaluate(ReadableBoard board) {
        if(board.hasWinner()) {
            return board.getWinner() == board.getCurrentColor() ? Win : Lost;
        }
        int count = 0;
        for (int x = 2; x < board.getSize() - 2; x++) {
            for (int y = 2; y < board.getSize() - 2; y++) {
                int playerCount = count(board, board.getCurrentColor(), x, y);
                count += playerCount * playerCount * 2; // threats are more severe when you can use them
                count -= count(board, board.getCurrentColor().getOpposite(), x, y);
            }
        }
        return count;
    }

    private BoardCell E = BoardCell.Empty;
    public int count(ReadableBoard board, Color color, int x, int y) {

        BoardCell O = color == Color.White ? BoardCell.White : BoardCell.Black;
        if(board.getCell(x, y) != O) return 0;
        int count = 0;

        BoardCell L2 = board.getCell(x - 2, y);
        BoardCell R2 = board.getCell(x + 2, y);
        boolean horizontal =
            (L2 == E || L2 == O) && (R2 == E || R2 == O) &&
            board.getCell(x - 1, y) == O && board.getCell(x + 1, y) == O;
        if(horizontal && (L2 == O || R2 == O)) count++;
        if(horizontal) count++;

        BoardCell T2 = board.getCell(x, y - 2);
        BoardCell B2 = board.getCell(x, y + 2);
        boolean vertical =
            (T2 == E || T2 == O) && (B2 == E || B2 == O) &&
            board.getCell(x, y - 1) == O && board.getCell(x, y + 1) == O;
        if(vertical && (T2 == O || B2 == O)) count++;
        if(vertical) count++;

        BoardCell RB = board.getCell(x + 2, y - 2);
        BoardCell LT = board.getCell(x - 2, y + 2);
        boolean backslash =
            (RB == E || RB == O) && (LT == E || LT == O) &&
            board.getCell(x + 1, y - 1) == O && board.getCell(x - 1, y + 1) == O;
        if(backslash && (RB == O || LT == O)) count++;
        if(backslash) count++;

        BoardCell LB = board.getCell(x - 2, y - 2);
        BoardCell RT = board.getCell(x + 2, y + 2);
        boolean forwardslash =
            (LB == E || LB == O) && (RT == E || RT == O) &&
            board.getCell(x - 1, y - 1) == O && board.getCell(x + 1, y + 1) == O;
        if(forwardslash && (LB == O || RT == O)) count++;
        if(forwardslash) count++;

        return count;

    }
}
