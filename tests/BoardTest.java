import Board.*;
import Contract.BoardCell;
import Contract.Color;

public class BoardTest extends SimpleUnitTest {
    public static void main(String[] args) {

        it("creates simple board", () -> {
            new Board(15);
        });

        it("allows to retrieve board size", () -> {
            assertTrue(3 == new Board(3).getSize());
            assertTrue(10 == new Board(10).getSize());
            assertTrue(15 == new Board(15).getSize());
        });

        it("initializes board with empty cells", () -> {
            Board board = new Board(3);
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    assertTrue(board.getCell(i, j) == BoardCell.Empty);
                }
            }
        });

        it("allows to make a move", () -> {

            Board board = new Board(3);
            assertTrue(board.getCurrentColor() == Color.Black);

            board.move(0, 0);
            assertTrue(board.getCell(0, 0) == BoardCell.Black);
            assertTrue(board.getCurrentColor() == Color.White);

        });

    }
}
