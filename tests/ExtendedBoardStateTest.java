import Contract.*;

import java.util.Arrays;
import java.util.HashMap;

public class ExtendedBoardStateTest extends SimpleUnitTest {
    public static void main(String[] args) {

        System.out.println("\n\nBoard State Test\n");

        it("correctly converts empty last weighted moves to policy", () -> {
            BoardCell[][] grid = {
                {E, E},
                {E, E}
            };
            HashMap<Move, Double> lastWeightedMoves = new HashMap<>();
            ExtendedBoardState state = new ExtendedBoardState(grid, Color.Black, null, lastWeightedMoves);
            assertTrue(Arrays.equals(state.toPolicyVector(), new double[]{
                0, 0, 0, 0,
            }));
        });

        it("correctly converts last weighted moves to policy", () -> {
            BoardCell[][] grid = {
                    {E, E},
                    {E, E}
            };
            HashMap<Move, Double> lastWeightedMoves = new HashMap<>();
            lastWeightedMoves.put(new Move(1,1),1d);
            ExtendedBoardState state = new ExtendedBoardState(grid, Color.Black, null, lastWeightedMoves);
            assertTrue(Arrays.equals(state.toPolicyVector(), new double[]{
                    0, 0, 0, 1,
            }));
        });
        it("correctly converts last weighted moves to policy 3 by 3 grid", () -> {
            BoardCell[][] grid = {
                    {E, E, E},
                    {E, E, E},
                    {E, E, E}
            };
            HashMap<Move, Double> lastWeightedMoves = new HashMap<>();
            lastWeightedMoves.put(new Move(2,1),0.5d);
            lastWeightedMoves.put(new Move(2,2),0.5d);
            ExtendedBoardState state = new ExtendedBoardState(grid, Color.Black, null, lastWeightedMoves);
            assertTrue(Arrays.equals(state.toPolicyVector(), new double[]{
                    0, 0, 0, 0, 0, 0, 0, 0.5, 0.5
            }));
        });
    }
}
