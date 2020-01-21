import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Evaluation.ExtendedWinLossEvaluation;
import Evaluation.WinLossEvaluation;

public class WinLossEvaluationTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nWin Loss Evaluation Test\n");
        winLossTest();

        System.out.println("\n\nExtended Win Loss Evaluation Test\n");
        extendedWinLossTest();

    }



    private static void winLossTest() {

        it("gives 0 score to board with no winner and loser", () -> {
            BoardCell[][] boardState = {
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertEqual(
                new WinLossEvaluation().evaluate(board), 0.0, 0.1
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
            assertEqual(
                new WinLossEvaluation().evaluate(board), -Double.MAX_VALUE, 0.1
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
            assertEqual(
                new WinLossEvaluation().evaluate(board), -Double.MAX_VALUE, 0.1
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
            assertEqual(
                new WinLossEvaluation().evaluate(board), -Double.MAX_VALUE, 0.1
            );
        });
    }

    private static void extendedWinLossTest() {

        it("gives 0 score to board with no winner and loser", () -> {
            BoardCell[][] boardState = {
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), 0.0, 0.1
            );
        });

        it("gives -4 score to the white loser; 4 in row", () -> {
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
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -4, 0.1
            );
        });

        it("gives -4 score to the black loser; 4 in row", () -> {
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
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -4, 0.1
            );
        });

        it("gives -1 score to the white; 4 in row next to edge", () -> {
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
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -1, 0.1
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
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -1, 0.1
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
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -1, 0.1
            );
        });

        it("gives -4 score to the white; blocked 4 in row + 3 in row", () -> {
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
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -4, 0.1
            );
        });

        it("gives -4 score to the white loser; straight cross", () -> {
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
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -4, 0.1
            );
        });

        it("gives -4 score to the white loser; straight cross", () -> {
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
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -4, 0.1
            );
        });

        it("gives -4 score to the white loser; slash cross", () -> {
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
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -4, 0.1
            );
        });

        it("gives -16 score to the white loser that does not respond; 2 last moves", () -> {
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

            // finish cross
            assertTrue(board.getCurrentColor() == Color.Black);
            board.move(new Move(3, 3));

            // from white perspective
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -4, 0.1
            );
            assertEqual(
                new ExtendedWinLossEvaluation(2).evaluate(board), -4, 0.1
            );

            // bad response
            assertTrue(board.getCurrentColor() == Color.White);
            board.move(new Move(1, 1));

            // from black perspective the game is won
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), 0, 0.1
            );
            assertEqual(
                new ExtendedWinLossEvaluation(2).evaluate(board), 16, 0.1
            );

            // progress to finish
            assertTrue(board.getCurrentColor() == Color.Black);
            board.move(new Move(3, 1));

            // from white perspective
            assertEqual(
                new ExtendedWinLossEvaluation(1).evaluate(board), -1, 0.1
            );
            assertEqual(
                new ExtendedWinLossEvaluation(2).evaluate(board), -1, 0.1
            );
            assertEqual(
                new ExtendedWinLossEvaluation(3).evaluate(board), -10, 0.1
            );

        });
    }

}
