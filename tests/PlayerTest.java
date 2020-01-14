import Board.Helpers.AllMovesSelector;
import Board.*;
import Board.Helpers.ApproximateMoveSelector;
import Board.Helpers.MoveSelectors;
import Contract.*;
import Evaluation.WinLossEvaluation;
import Player.*;

public class PlayerTest extends SimpleUnitTest {
    public static void main(String[] args) {

        System.out.println("\n\nEvaluation Player Test\n");
        evaluationPlayerTest();

        System.out.println("\n\nMinMax Player Test\n");
        minMaxPlayerTest();

        System.out.println("\n\nNegamax Player Test\n");
        negamaxPlayerTest();

        System.out.println("\n\nMCTS Player Test\n");
        mctsPlayerTest();

        System.out.println("\n\nTimed Player Test\n");
        timedPlayerTest();

        System.out.println("\n\nThreat Player Test\n");
        threatPlayerTest();

    }

    private static void evaluationPlayerTest() {
        Player evaluationPlayer = Players.get("simpleton");
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
                evaluationPlayer
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
                evaluationPlayer
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
                evaluationPlayer
                    .getMove(board)
                    .isEqual(
                        new Move(1, 4)
                    )
            );

        });
    }

    private static void minMaxPlayerTest() {

        it("allows to play out a whole game with all moves selector", () -> {

            MinMaxPlayer minMaxPlayer = new MinMaxPlayer(
                new WinLossEvaluation(), new AllMovesSelector(), 3
            );

            SimpleBoard board = new SimpleBoard(5);

            Game game = new SimpleGame(minMaxPlayer);

            game.play(board);

            assertTrue(board.isGameFinished());

        });

        it("allows to play out a whole game with approximate moves selector", () -> {

            MinMaxPlayer minMaxPlayer = new MinMaxPlayer(
                new WinLossEvaluation(), new ApproximateMoveSelector(), 3
            );

            SimpleBoard board = new SimpleBoard(5);

            Game game = new SimpleGame(minMaxPlayer);

            game.play(board);

            assertTrue(board.isGameFinished());

        });

    }

    private static void negamaxPlayerTest() {

        it("allows to play out a whole game", () -> {

            Player player = Players.get("negamax3");

            SimpleBoard board = new SimpleBoard(5);

            Game game = new SimpleGame(player, player);

            game.play(board);

            assertTrue(board.isGameFinished());

        });

    }

    private static void mctsPlayerTest() {

        it("allows to play out a whole game", () -> {

            Player player = new MCTSPlayer(MoveSelectors.get("approximate"),0.01);

            SimpleBoard board = new SimpleBoard(5);

            Game game = new SimpleGame(player, player);

            game.play(board);

            assertTrue(board.isGameFinished());

        });

    }

    private static void timedPlayerTest() {

        it("allows to play out a whole game", () -> {

            TimedPlayer player = new TimedPlayer(Players.get("random"));

            SimpleBoard board = new SimpleBoard(5);

            Game game = new SimpleGame(player, player);

            game.play(board);

            assertTrue(board.isGameFinished());
            assertTrue(player.getMaxTimePerMove() >= 0);
            assertTrue(player.getMinTimePerMove() >= 0);
            assertTrue(player.getAverageTimePerMove() >= 0);

        });

    }

    private static void threatPlayerTest() {

        it("allows to play out a whole game", () -> {

            Player player = Players.get("threat");

            SimpleBoard board = new SimpleBoard(5);

            Game game = new SimpleGame(player, player);

            game.play(board);

            assertTrue(board.isGameFinished());

        });

    }

}
