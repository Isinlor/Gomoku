package Evaluation;

import Contract.*;

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
        return count * count * (count > 0 ? 1 : -1);
    }

    private BoardCell E = BoardCell.Empty;
    public int count(ReadableBoard board, Color color, int x, int y) {

        BoardCell O = color == Color.White ? BoardCell.White : BoardCell.Black;
        if(board.getCell(x, y) != O) return 0;
        int count = 0;

        if(board.getCell(x - 1, y) == O && board.getCell(x + 1, y) == O) {
            BoardCell L2 = board.getCell(x - 2, y);
//        BoardCell L3 = board.isOnBoard(x - 3) ? board.getCell(x - 3, y) : null;
            BoardCell R2 = board.getCell(x + 2, y);
//        BoardCell R3 = board.isOnBoard(x + 3) ? board.getCell(x + 3, y) : null;
            boolean horizontal =
                (
                    ((L2 == E || L2 == O) && (R2 == E || R2 == O)) ||
                    ((L2 != E && R2 == O) || (L2 == O && R2 != E))
                );
            if(horizontal && ((L2 == O && R2 == E) || (R2 == O && L2 == E))) count++;
            if(horizontal) count++;
        }

        if(board.getCell(x, y - 1) == O && board.getCell(x, y + 1) == O) {
            BoardCell T2 = board.getCell(x, y - 2);
//        BoardCell T3 = board.isOnBoard(y - 3) ? board.getCell(x, y - 3) : null;
            BoardCell B2 = board.getCell(x, y + 2);
//        BoardCell B3 = board.isOnBoard(y + 3) ? board.getCell(x, y + 3) : null;
            boolean vertical =
                (
                    ((T2 == E || T2 == O) && (B2 == E || B2 == O)) || // (e|0)ooo(e/o)
                    ((T2 != E && B2 == O) || (T2 == O && B2 != E))    // (x|e)oooo|oooo(x|e)
                ); // ooo
            if(vertical && ((T2 == O && B2 == E) || (B2 == O && T2 == E))) count++; // ooooe
            if(vertical) count++;
        }

        if(board.getCell(x + 1, y - 1) == O && board.getCell(x - 1, y + 1) == O) {
            BoardCell RB = board.getCell(x + 2, y - 2);
            BoardCell LT = board.getCell(x - 2, y + 2);
            boolean backslash =
                (
                    ((RB == E || RB == O) && (LT == E || LT == O)) ||
                    ((RB != E && LT == O) || (RB == O && LT != E))
                );
            if(backslash && ((RB == O && LT == E) || (LT == O && RB == E))) count++;
            if(backslash) count++;
        }

        if(board.getCell(x - 1, y - 1) == O && board.getCell(x + 1, y + 1) == O) {
            BoardCell LB = board.getCell(x - 2, y - 2);
            BoardCell RT = board.getCell(x + 2, y + 2);
            boolean forwardslash =
                (
                    ((LB == E || LB == O) && (RT == E || RT == O)) ||
                    ((LB != E && RT == O) || (LB == O && RT != E))
                );
            if(forwardslash && ((LB == O && RT == E) || (RT == O && LB == E))) count++;
            if(forwardslash) count++;
        }

        return count;

    }
}
