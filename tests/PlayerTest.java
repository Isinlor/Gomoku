import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Evaluation.WinLossEvaluation;
import Player.EvaluatePlayer;

public class PlayerTest extends SimpleUnitTest {
    public static void main(String[] args) {

        System.out.println("\n\nPlayer Test\n");

        it("picks the best move", () -> {

            BoardCell[][] boardState = {
                {B, B, B, B, E},
                {W, W, W, W, E},
                {B, W, E, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };

            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.Black);

            assertTrue(
                new EvaluatePlayer(new WinLossEvaluation())
                    .getMove(board)
                    .isEqual(
                        new Move(0, 4)
                    )
            );

        });

    }
}
