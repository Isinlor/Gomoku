import Board.Helpers.ApproximateMoveSelector;
import Board.SimpleBoard;
import Contract.BoardCell;

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

    }

}
