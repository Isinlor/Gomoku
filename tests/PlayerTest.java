import Board.Helpers.AllMovesSelector;
import Board.*;
import Contract.*;
import Evaluation.WinLossEvaluation;
import Player.*;

public class PlayerTest extends SimpleUnitTest {
    public static void main(String[] args) {

        System.out.println("\n\nEvaluation Player Test\n");
        evaluationPlayerTest();

        System.out.println("\n\nMinMax Player Test\n");
        minMaxPlayerTest();

    }

    private static void evaluationPlayerTest() {
        it("picks the best move for black - horizontal", () -> {

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

        it("picks the best move for black - vertical", () -> {

            BoardCell[][] boardState = {
                {B, W, E, E, E},
                {B, W, E, E, E},
                {B, W, E, E, E},
                {B, W, E, E, E},
                {E, E, E, E, E},
            };

            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.Black);

            assertTrue(
                new EvaluationPlayer(new WinLossEvaluation())
                    .getMove(board)
                    .isEqual(
                        new Move(4, 0)
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

    private static void minMaxPlayerTest() {

        it("allows to play out a whole game", () -> {

            MinMaxPlayer minMaxPlayer = new MinMaxPlayer(
                new WinLossEvaluation(), new AllMovesSelector(), 3
            );

            SimpleBoard board = new SimpleBoard(5);

            Game game = new SimpleGame(minMaxPlayer, minMaxPlayer);

            game.play(board);

            assertTrue(board.isGameFinished());

        });

    }

}
