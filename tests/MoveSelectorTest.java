import Board.Helpers.ApproximateMoveSelector;
import Board.Helpers.ForcedMoveSelector;
import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Contract.MoveSelector;
import Evaluation.NegamaxEvaluation;
import Evaluation.WinLossEvaluation;

import java.util.Set;

public class MoveSelectorTest extends SimpleUnitTest {

    public static void main(String[] args) {

        approximateMoveSelectorTest();
        forcedMoveSelectorTest();

    }

    public static void approximateMoveSelectorTest() {

        System.out.println("\n\nApproximate Move Selector Test\n");

        it("returns all moves around a piece", () -> {

            BoardCell[][] boardState = {
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, B, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);

            assertEqual(new ApproximateMoveSelector().getMoves(board).size(), 8);

        });

        it("returns all moves around pieces", () -> {

            BoardCell[][] boardState = {
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, W, B, B, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);

            assertEqual(new ApproximateMoveSelector().getMoves(board).size(), 12);

        });

        it("returns all moves following made moves", () -> {

            SimpleBoard board = new SimpleBoard(5);

            board.move(new Move(0, 0));
            assertEqual(new ApproximateMoveSelector().getMoves(board).size(), 3);

            board.move(new Move(4, 4));
            assertEqual(new ApproximateMoveSelector().getMoves(board).size(), 6);

            board.revertMove(new Move(0, 0));
            assertEqual(new ApproximateMoveSelector().getMoves(board).size(), 3);

            board.move(new Move(2, 2));
            assertEqual(new ApproximateMoveSelector().getMoves(board).size(), 10);

            SimpleBoard boardCopy = new SimpleBoard(board);
            assertEqual(new ApproximateMoveSelector().getMoves(boardCopy).size(), 10);

            boardCopy.resetBoard();
            assertEqual(new ApproximateMoveSelector().getMoves(boardCopy).size(), 25);
            assertEqual(new ApproximateMoveSelector().getMoves(board).size(), 10);

        });

    }

    public static void forcedMoveSelectorTest() {

        System.out.println("\n\nForced Move Selector Test\n");

        MoveSelector selector = new ForcedMoveSelector(
            new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 3),
            new ApproximateMoveSelector()
        );

        it("returns all moves when there is no threat", () -> {

            BoardCell[][] boardState = {
                {E, E, E, E, E},
                {E, E, E, E, E},
                {E, E, B, E, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);

            assertEqual(selector.getMoves(board).size(), 8);

        });

        it("returns two moves for white that do not lose in next 3 rounds", () -> {

            BoardCell[][] boardState = {
                {W, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, E, B, B, B, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {W, E, E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.White);

            Set<Move> moves = selector.getMoves(board);
            assertEqual(selector.getMoves(board).size(), 2);
            assertTrue(selector.getMoves(board).contains(new Move(3, 1)));
            assertTrue(selector.getMoves(board).contains(new Move(3, 5)));

        });

        it("returns two moves for black that do not lose in next 3 rounds", () -> {

            BoardCell[][] boardState = {
                {B, E, E, E, E, E, B},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {E, E, W, W, W, E, E},
                {E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E},
                {B, E, E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.Black);

            Set<Move> moves = selector.getMoves(board);
            assertEqual(selector.getMoves(board).size(), 2);
            assertTrue(selector.getMoves(board).contains(new Move(3, 1)));
            assertTrue(selector.getMoves(board).contains(new Move(3, 5)));

        });

        it("returns the only move that does not lose for black", () -> {

            BoardCell[][] boardState = {
                {W, W, W, W, E},
                {E, E, E, E, E},
                {B, B, B, E, E},
                {B, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getCurrentColor() == Color.Black);

            assertEqual(selector.getMoves(board).size(), 1);
            assertTrue(selector.getMoves(board).contains(new Move(0, 4)));

        });

        it("returns the winning move for black", () -> {

            BoardCell[][] boardState = {
                {W, W, W, W, E},
                {E, E, E, E, E},
                {B, B, B, B, E},
                {E, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);

            assertEqual(selector.getMoves(board).size(), 1);
            assertTrue(board.getCurrentColor() == Color.Black);
            assertTrue(selector.getMoves(board).contains(new Move(2, 4)));

        });

        it("returns the winning move for white", () -> {

            BoardCell[][] boardState = {
                {W, W, W, W, E},
                {E, E, E, E, E},
                {B, B, B, B, E},
                {B, E, E, E, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);

            assertEqual(selector.getMoves(board).size(), 1);
            assertTrue(board.getCurrentColor() == Color.White);
            assertTrue(selector.getMoves(board).contains(new Move(0, 4)));

        });

        it("still returns moves if game is hopeless", () -> {

            BoardCell[][] boardState = {
                {W, W, W, W, B},
                {E, B, E, B, E},
                {B, B, B, B, E},
                {W, B, W, W, E},
                {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);

            assertTrue(board.getCurrentColor() == Color.White);
            assertEqual(selector.getMoves(board).size(), 10);

        });

    }

}
