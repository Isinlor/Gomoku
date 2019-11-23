import Board.Helpers.ApproximateMoveSelector;
import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Move;

public class MoveSelectorTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nMove Selector Test\n");

        approximateMoveSelectorTest();

    }

    public static void approximateMoveSelectorTest() {

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

}
