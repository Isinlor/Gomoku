import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Evaluation.WinLossEvaluation;

public class WinLossEvaluationTest extends SimpleUnitTest {

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

        it("gives min score to the white loser; horizontal", () -> {
            BoardCell[][] boardState = {
                {B, B, B, B, B},
                {W, W, W, W, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(
                new WinLossEvaluation().evaluate(board) == -Double.MAX_VALUE
            );
        });

        it("gives min score to the white loser; vertical", () -> {
            BoardCell[][] boardState = {
                {B, W, E, E, E},
                {B, W, E, E, E},
                {B, W, E, E, E},
                {B, W, E, E, E},
                {B, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(
                new WinLossEvaluation().evaluate(board) == -Double.MAX_VALUE
            );
        });

        it("gives min score to the black loser; horizontal", () -> {
            BoardCell[][] boardState = {
                {B, W, B, E, E},
                {B, W, E, E, E},
                {B, W, E, E, E},
                {B, W, E, E, E},
                {E, W, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.Black);
            assertTrue(
                new WinLossEvaluation().evaluate(board) == -Double.MAX_VALUE
            );
        });

    }

}
