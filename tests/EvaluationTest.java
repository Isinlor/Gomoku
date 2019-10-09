import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Evaluation.WinLossEvaluation;

public class EvaluationTest extends SimpleUnitTest {

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
                new WinLossEvaluation().evaluate(board, new Move(0,0)) == 0.0
            );
        });

        it("gives max score to the winner", () -> {
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
                new WinLossEvaluation().evaluate(board, new Move(0, 4)) == Double.MAX_VALUE
            );
        });

    }

}
