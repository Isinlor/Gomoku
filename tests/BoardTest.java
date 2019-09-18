import Board.*;
import Contract.BoardCell;

public class BoardTest extends SimpleUnitTest {
    public static void main(String[] args) {

        it("creates simple board", () -> {
            new Board(15);
        });

        it("allows to retrieve board size", () -> {
            assertTrue(15 == new Board(15).getSize());
        });

        it("initializes board with empty cells", () -> {
            Board board = new Board(15);
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    assertTrue(board.getCells()[i][j] == BoardCell.Empty);
                }
            }
        });

    }
}
