import Board.Helpers.MoveSelectors;
import Board.SimpleBoard;
import CLI.Utils;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Contract.MoveSelector;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class Benchmark extends SimpleUnitTest {
    public static void main(String[] args) {
        MoveSelector forced7 = MoveSelectors.get("forced7");

        benchIt("prevents lost in more than 3 moves by 7 moves look ahead", () -> {

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
