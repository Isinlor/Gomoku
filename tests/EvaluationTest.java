import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Evaluation.WinLossEvaluation;

public class EvaluationTest extends SimpleUnitTest {

    final static BoardCell B = BoardCell.Black;
    final static BoardCell W = BoardCell.White;
    final static BoardCell E = BoardCell.Empty;

    public static void main(String[] args) {

        System.out.println("\n\nEvaluation Test\n");

        it("gives 0 score to board with no winner and loser", () -> {
            BoardCell[][] boardState = {
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(
                new WinLossEvaluation().evaluate(board) == 0.0
            );
        });

        it("gives min score to the loser", () -> {
            BoardCell[][] boardState = {
                {W, W, W, W, W},
                {B, B, B, B, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.Black);
            assertTrue(
                new WinLossEvaluation().evaluate(board) == Double.MIN_VALUE
            );
        });

        it("gives max score to the winner", () -> {
            BoardCell[][] boardState = {
                {B, B, B, B, B},
                {W, W, W, W, E},
                {W, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.Black);
            assertTrue(
                new WinLossEvaluation().evaluate(board) == Double.MAX_VALUE
            );
        });

    }

}
