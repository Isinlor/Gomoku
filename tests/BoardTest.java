import Board.*;
import Contract.BoardCell;
import Contract.Color;

public class BoardTest extends SimpleUnitTest {
    public static void main(String[] args) {

        final BoardCell B = BoardCell.Black;
        final BoardCell W = BoardCell.White;
        final BoardCell E = BoardCell.Empty;

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

        it("can check if there are 3 in a row from a certain position.", () -> {
            BoardCell[][] boardState = {
                    {E, E, W},
                    {B, B, B}, // check from center position
                    {W, E, W},
            };
            Board board = new Board(boardState);
            assertTrue(board.checkRows(1, 1, 3));
        });

        it("can check if there are NOT 3 in a row from a certain position.", () -> {
            BoardCell[][] boardState = {
                    {E, E, W},
                    {B, B, B},
                    {W, E, W}, // check from left bottom
            };
            Board board = new Board(boardState);
            assertTrue(!board.checkRows(0, 0, 3));
        });

        it("knows when a there are 3 horizontal tokens in a row with the same color", () -> {
            BoardCell[][] boardState = {
                    {B, E, E},
                    {E, B, B},
                    {W, W, W},
            };
            Board board = new Board(boardState);
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
            Board board = new Board(boardState);
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
            Board board = new Board(boardState);
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
            Board board = new Board(boardState);
            assertTrue(board.getWinner(4)==Color.White);
        });

        it("knows when a there are 4 diagonal tokens in a row with the same color", () -> {
            BoardCell[][] boardState = {
                    {E, E, E, E, E},
                    {E, E, E, E, B},
                    {E, W, E, B, W},
                    {W, W, B, E, E},
                    {E, B, E, E, E},
            };
            Board board = new Board(boardState);
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
            Board board = new Board(boardState);
            assertTrue(board.getWinner(4)==Color.Black);
        });

        }
}
