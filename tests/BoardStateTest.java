import Contract.BoardCell;
import Contract.BoardState;
import Contract.Color;

import java.util.Arrays;

public class BoardStateTest extends SimpleUnitTest {
    public static void main(String[] args) {

        System.out.println("\n\nBoard State Test\n");

        it("correctly converts empty state to vector for current black player", () -> {
            BoardCell[][] grid = {
                {E, E},
                {E, E}
            };
            BoardState state = new BoardState(Color.Black, grid);
            assertTrue(Arrays.equals(state.toVector(), new float[]{
                0, 0, 0, 0,
                0, 0, 0, 0,
            }));
        });
        it("correctly converts simple state 1 to vector for current black player", () -> {
            BoardCell[][] grid = {
                {B, E},
                {E, E}
            };
            BoardState state = new BoardState(Color.Black, grid);
            assertTrue(Arrays.equals(state.toVector(), new float[]{
                1, 0, 0, 0,
                0, 0, 0, 0,
            }));
        });
        it("correctly converts simple state 2 to vector for current black player", () -> {
            BoardCell[][] grid = {
                {E, E},
                {E, B}
            };
            BoardState state = new BoardState(Color.Black, grid);
            assertTrue(Arrays.equals(state.toVector(), new float[]{
                0, 0, 0, 1,
                0, 0, 0, 0,
            }));
        });
        it("correctly converts simple state 3 to vector for current black player", () -> {
            BoardCell[][] grid = {
                {B, B},
                {B, B}
            };
            BoardState state = new BoardState(Color.Black, grid);
            assertTrue(Arrays.equals(state.toVector(), new float[]{
                1, 1, 1, 1,
                0, 0, 0, 0,
            }));
        });
        it("correctly converts simple state 4 to vector for current black player", () -> {
            BoardCell[][] grid = {
                {W, W},
                {W, W}
            };
            BoardState state = new BoardState(Color.Black, grid);
            assertTrue(Arrays.equals(state.toVector(), new float[]{
                0, 0, 0, 0,
                1, 1, 1, 1,
            }));
        });
        it("correctly converts simple state 1 to vector for current white player", () -> {
            BoardCell[][] grid = {
                {B, E},
                {E, E}
            };
            BoardState state = new BoardState(Color.White, grid);
            assertTrue(Arrays.equals(state.toVector(), new float[]{
                0, 0, 0, 0,
                1, 0, 0, 0,
            }));
        });
        it("correctly converts simple state 2 to vector for current white player", () -> {
            BoardCell[][] grid = {
                {E, E},
                {E, B}
            };
            BoardState state = new BoardState(Color.White, grid);
            assertTrue(Arrays.equals(state.toVector(), new float[]{
                0, 0, 0, 0,
                0, 0, 0, 1,
            }));
        });
        it("correctly converts simple state 3 to vector for current white player", () -> {
            BoardCell[][] grid = {
                {B, B},
                {B, B}
            };
            BoardState state = new BoardState(Color.White, grid);
            assertTrue(Arrays.equals(state.toVector(), new float[]{
                0, 0, 0, 0,
                1, 1, 1, 1,
            }));
        });
        it("correctly converts simple state 4 to vector for current white player", () -> {
            BoardCell[][] grid = {
                {W, W},
                {W, W}
            };
            BoardState state = new BoardState(Color.White, grid);
            assertTrue(Arrays.equals(state.toVector(), new float[]{
                1, 1, 1, 1,
                0, 0, 0, 0,
            }));
        });
    }
}
