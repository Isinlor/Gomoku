import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Evaluation.WinLossEvaluation;
import Player.EvaluationPlayer;

public class PlayerTest extends SimpleUnitTest {
    public static void main(String[] args) {

        System.out.println("\n\nPlayer Test\n");

        it("picks the best move for black", () -> {

            BoardCell[][] boardState = {
                {B, B, B, B, E},
                {W, W, W, W, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };

            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.Black);

            assertTrue(
                new EvaluationPlayer(new WinLossEvaluation())
                    .getMove(board)
                    .isEqual(
                        new Move(0, 4)
                    )
            );

        });

        it("picks the best move for white", () -> {

            BoardCell[][] boardState = {
                {B, B, B, B, E},
                {W, W, W, W, E},
                {B, E, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };

            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.White);

            assertTrue(
                new EvaluationPlayer(new WinLossEvaluation())
                    .getMove(board)
                    .isEqual(
                        new Move(1, 4)
                    )
            );

        });

    }
}
