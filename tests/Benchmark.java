import Board.Helpers.ApproximateMoveSelector;
import Board.Helpers.ForcedMoveSelector;
import Board.Helpers.MoveSelectors;
import Board.Helpers.SortedMoveSelector;
import Board.SimpleBoard;
import CLI.Utils;
import Contract.*;
import Evaluation.*;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class Benchmark extends SimpleUnitTest {
    public static void main(String[] args) {

        benchBoardMoves(9);
        benchBoardMoves(15);
        benchForced7();

    }

    private static void benchBoardMoves(int boardSize) {
        ReadableBoard board = new SimpleBoard(boardSize);
        benchIt("new SimpleBoard(" + boardSize + ").getWithMove()", () -> {
            for (int i = 0; i < 100000; i++) {
                board.getWithMove(new Move(0, 0));
            }
        });

        benchIt("new SimpleBoard(" + boardSize + ").move().revertLastMove()", () -> {
            for (int i = 0; i < 10000; i++) {
                SimpleBoard boardCopy = new SimpleBoard(board);
                for (int j = 0; j < 10; j++) {
                    boardCopy.move(new Move(0, 0));
                    boardCopy.revertLastMove();
                }
            }
        });
    }

    private static void benchForced7() {

        BoardCell[][] boardState = {
            {W, E, E, E, E, E, W},
            {E, E, E, E, E, E, E},
            {E, E, E, B, E, E, E},
            {E, E, B, E, B, E, E},
            {E, E, E, B, E, E, E},
            {E, E, E, E, E, E, E},
            {W, E, E, E, E, E, E},
        };

        SimpleBoard board = new SimpleBoard(boardState);

        benchIt("prevents lost in more than 3 moves by 7 moves look ahead", () -> {

            MoveSelector forced7 = new ForcedMoveSelector(
                new NegamaxEvaluation(
                    new CountEvaluation(),
                    new SortedMoveSelector(new ApproximateMoveSelector(), new CountEvaluation()),
                    7
                ),
                MoveSelectors.get("forced3")
            );

            assertTrue(board.getCurrentColor() == Color.White);

            Collection<Move> moves = forced7.getMoves(board);

            assertEqual(moves.size(), 1);
            assertTrue(moves.contains(new Move(3, 3)));

        });

        benchIt("prevents lost in more than 3 moves by 7 moves look ahead", () -> {

            MoveSelector forced7 = MoveSelectors.get("forced7");

            assertTrue(board.getCurrentColor() == Color.White);

            Collection<Move> moves = forced7.getMoves(board);

            assertEqual(moves.size(), 1);
            assertTrue(moves.contains(new Move(3, 3)));

        });

    }

    public static void benchIt(String expectation, Runnable runnable) {
        long startTime = System.nanoTime();
        it(expectation, runnable);
        long endTime = System.nanoTime();
        System.out.println("  Duration: " + Utils.formatTime(
            TimeUnit.NANOSECONDS.toMillis(endTime - startTime)
        ));
        System.out.println();
    }

}
