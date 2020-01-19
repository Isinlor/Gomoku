import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Contract.Evaluation;
import Contract.Move;
import Evaluation.ExtendedWinLossEvaluation;
import Evaluation.WinLossEvaluation;

public class WinLossEvaluationTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nWin Loss Evaluation Test\n");

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

        System.out.println("\n\nQuick Win Loss Evaluation Test\n");

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
                new ExtendedWinLossEvaluation(1).evaluate(board) == 0.0
            );
        });

        it("gives -1 score to the white loser; 4 in row", () -> {
            BoardCell[][] boardState = {
                {W, E, E, E, E, E, W},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, B, B, E, B, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {W, E, E, E, E, E, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            board.move(new Move(3, 3));
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(
                new ExtendedWinLossEvaluation(1).evaluate(board) == -1
            );
        });

        it("gives -1 score to the black loser; 4 in row", () -> {
            BoardCell[][] boardState = {
                {B, E, E, E, E, E, B},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, W, W, E, W, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {B, E, E, E, E, E, B},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            board.move(new Move(3, 3));
            assertTrue(board.getCurrentColor() == Color.Black);
            assertTrue(
                new ExtendedWinLossEvaluation(1).evaluate(board) == -1
            );
        });

        it("gives 0 score to the white; 4 in row next to edge", () -> {
            BoardCell[][] boardState = {
                {W, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, W},
                {B, B, B, E, E, E, E},
                {E, E, E, E, E, E, W},
                {E, E, E, E, E, E, E},
                {W, E, E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            board.move(new Move(3, 3));
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(
                new ExtendedWinLossEvaluation(1).evaluate(board) == 0
            );
        });

        it("gives 0 score to the white; blocked 4 in row", () -> {
            BoardCell[][] boardState = {
                {W, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, B, B, E, B, W, W},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {W, E, E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            board.move(new Move(3, 3));
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(
                new ExtendedWinLossEvaluation(1).evaluate(board) == 0
            );
        });

        it("gives 0 score to the white; blocked 4 in row", () -> {
            BoardCell[][] boardState = {
                {W, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {W, B, B, E, B, E, W},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {W, E, E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            board.move(new Move(3, 3));
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(
                new ExtendedWinLossEvaluation(1).evaluate(board) == 0
            );
        });

        it("gives -1 score to the white; blocked 4 in row + 3 in row", () -> {
            BoardCell[][] boardState = {
                {W, E, E, E, E, E, W},
                {E, E, E, E, E, E, E},
                {E, E, E, B, E, E, E},
                {W, B, B, E, B, E, W},
                {E, E, E, B, E, E, E},
                {E, E, E, E, E, E, E},
                {W, E, E, E, E, E, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            board.move(new Move(3, 3));
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(
                new ExtendedWinLossEvaluation(1).evaluate(board) == -1
            );
        });

        it("gives -1 score to the white loser; straight cross", () -> {
            BoardCell[][] boardState = {
                {W, E, E, E, E, E, W},
                {E, E, E, E, E, E, E},
                {E, E, E, B, E, E, E},
                {E, E, B, E, B, E, E},
                {E, E, E, B, E, E, E},
                {E, E, E, E, E, E, E},
                {W, E, E, E, E, E, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            board.move(new Move(3, 3));
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(
                new ExtendedWinLossEvaluation(1).evaluate(board) == -1
            );
        });

        it("gives -1 score to the white loser; straight cross", () -> {
            BoardCell[][] boardState = {
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, E, W, B, W, E, E},
                {E, E, B, E, B, E, E},
                {E, E, W, B, W, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            board.move(new Move(3, 3));
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(
                new ExtendedWinLossEvaluation(1).evaluate(board) == -1
            );
        });

        it("gives -1 score to the white loser; slash cross", () -> {
            BoardCell[][] boardState = {
                {E, E, E, W, E, E, E},
                {E, E, E, E, E, E, E},
                {E, E, B, E, B, E, E},
                {W, E, E, E, E, E, W},
                {E, E, B, E, B, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, W, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            board.move(new Move(3, 3));
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(
                new ExtendedWinLossEvaluation(1).evaluate(board) == -1
            );
        });

    }

}
