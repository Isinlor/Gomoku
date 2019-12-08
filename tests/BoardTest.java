import Board.*;
import Contract.BoardCell;
import Contract.Color;
import Contract.Move;
import Contract.ReadableBoard;

public class BoardTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nBoard Test\n");

        it("creates simple board", () -> {
            new SimpleBoard(15);
        });

        it("allows to retrieve board size", () -> {
            assertTrue(3 == new SimpleBoard(3).getSize());
            assertTrue(10 == new SimpleBoard(10).getSize());
            assertTrue(15 == new SimpleBoard(15).getSize());
        });

        it("initializes board with empty cells", () -> {
            SimpleBoard board = new SimpleBoard(3);
            for (int i = 0; i < board.getSize(); i++) {
                for (int j = 0; j < board.getSize(); j++) {
                    assertTrue(board.getCell(i, j) == BoardCell.Empty);
                }
            }
        });

        it("allows to make a move", () -> {

            SimpleBoard board = new SimpleBoard(3);
            assertTrue(board.getCurrentColor() == Color.Black);

            board.move(new Move(0, 0));
            assertTrue(board.getCell(0, 0) == BoardCell.Black);
            assertTrue(board.getCurrentColor() == Color.White);

        });

        it("allows to create a new board with a move", () -> {

            SimpleBoard board = new SimpleBoard(3);
            assertTrue(board.getCurrentColor() == Color.Black);

            ReadableBoard newBoard = board.getWithMove(new Move(0, 0));

            assertTrue(board.getCell(0, 0) == BoardCell.Empty);
            assertTrue(board.getCurrentColor() == Color.Black);

            assertTrue(newBoard.getCell(0, 0) == BoardCell.Black);
            assertTrue(newBoard.getCurrentColor() == Color.White);

        });

        it("allows to display a board", () -> {
            BoardCell[][] boardState = {
                {B, E, W},
                {W, E, B},
                {E, E, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            // it just check that something is displayed
            assertTrue(!board.toString().isEmpty());
        });

        testBoardCell();
        testWinningConditions();

    }

    private static void testBoardCell() {
        it("gives back color from board cell", () -> {
            BoardCell cell = BoardCell.Black;
            Color color = cell.getColor();
            assertTrue(color == Color.Black);
        });

        it("give back no color if board cell is empty", () -> {
            BoardCell cell = BoardCell.Empty;
            Color color = cell.getColor();
            assertTrue(color==null);
        });
    }

    private static void testWinningConditions() {

        it("can check if there are 3 in a row from a certain position.", () -> {
            BoardCell[][] boardState = {
                    {E, E, W},
                    {B, B, B}, // check from center position
                    {W, E, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.checkRows(1, 1, 3));
        });

        it("can check if there are NOT 3 in a row from a certain position.", () -> {
            BoardCell[][] boardState = {
                    {E, E, W},
                    {B, B, B},
                    {W, E, W}, // check from left bottom
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(!board.checkRows(0, 0, 3));
        });

        it("knows when a there are 3 horizontal tokens in a row with the same color", () -> {
            BoardCell[][] boardState = {
                    {B, E, E},
                    {E, B, B},
                    {W, W, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.hasWinner(3));
        });

        it("knows when a there are 5 horizontal tokens in a row with the same color", () -> {
            BoardCell[][] boardState = {
                    {E, W, W, E, W},
                    {B, B, B, B, B}, //5 Blacks in a row
                    {E, W, E, W, E},
                    {E, E, E, E, E},
                    {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.hasWinner());
        });

        it("knows when a there are NOT 5 horizontal tokens in a row with the same color", () -> {
            BoardCell[][] boardState = {
                    {E, B, W, E, E},
                    {E, E, B, B, E}, //5 blacks in a row
                    {E, E, W, W, E},
                    {E, E, E, E, E},
                    {E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(!board.hasWinner());
        });

        it("knows when a there are 4 vertical tokens in a row with the same color", () -> {
            BoardCell[][] boardState = {
                    {E, E, E, E, E},
                    {E, E, E, W, E},
                    {E, B, B, W, B},
                    {E, B, E, W, E},
                    {E, E, E, W, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getWinner(4)== Color.White);
        });

        it("knows when a there are 4 diagonal tokens in a row with the same color", () -> {
            BoardCell[][] boardState = {
                    {E, E, E, E, E},
                    {E, E, E, E, B},
                    {E, W, E, B, W},
                    {W, W, B, E, E},
                    {E, B, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getWinner(4)==Color.Black);
        });

        it("knows when a there are 4 diagonal (other direction) tokens in a row with the same color", () -> {
            BoardCell[][] boardState = {
                    {B, E, E, E, E},
                    {E, B, E, E, E}, //5 Blacks in a row
                    {W, E, B, B, E},
                    {W, W, E, B, W},
                    {E, E, E, E, W},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(board.getWinner(4)==Color.Black);
        });

        it("correctly evaluates a game", () -> {

            SimpleBoard board = new SimpleBoard(5);

            assertTrue(!board.hasWinner());
            assertColor(board.getWinner(), null);
            assertColor(board.getCurrentColor(), B.getColor());

            board.move(new Move(0, 0));
            assertTrue(!board.hasWinner());
            assertColor(board.getWinner(), null);
            assertColor(board.getCurrentColor(), W.getColor());

            board.move(new Move(1, 0));
            assertTrue(!board.hasWinner());
            assertColor(board.getWinner(), null);
            assertColor(board.getCurrentColor(), B.getColor());

            // black in left most column (x=0)
            // white in next column (x=1)
            board.move(new Move(0, 1));
            board.move(new Move(1, 1));
            board.move(new Move(0, 2));
            board.move(new Move(1, 2));
            board.move(new Move(0, 3));
            board.move(new Move(1, 3));

            assertTrue(!board.hasWinner());
            assertColor(board.getWinner(), null);
            assertColor(board.getCurrentColor(), B.getColor());

            board.move(new Move(0, 4));
            assertColor(board.getWinner(), B.getColor());
            assertTrue(board.hasWinner());
            assertTrue(board.isGameFinished());

            board.revertMove(new Move(0, 4));
            assertTrue(!board.hasWinner());
            assertColor(board.getWinner(), null);
            assertColor(board.getCurrentColor(), B.getColor());

        });

    }
}
