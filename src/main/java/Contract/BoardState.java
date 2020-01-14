package Contract;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class BoardState implements Serializable {

    private static final long serialVersionUID = 4352191259830654682L;

    Color currentPlayer;
    BoardCell[][] grid;

    public BoardState(Color currentPlayer, BoardCell[][] grid) {
        this.currentPlayer = currentPlayer;
        this.grid = grid;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public void rotate90degrees()  {
        grid = this.rotateClockWise(grid);
    }

    private BoardCell[][] rotateClockWise(BoardCell[][] matrix) {
        int size = matrix.length;
        BoardCell[][] rotated = new BoardCell[size][size];
        for (int i = 0; i < size; ++i)
            for (int j = 0; j < size; ++j)
                rotated[i][j] = matrix[size - j - 1][i];
        return rotated;
    }

    public int[][][] toMultiDimensionalMatrix() {

        int channels = 2; //one for current player, one for other player
        int[][][] data = new int[grid.length][grid.length][channels];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                BoardCell cell = grid[i][j];
                int[] vector = new int[]{0, 0};
                if (cell != BoardCell.Empty) {
                    int k = cell.getColor() == currentPlayer ? 0 : 1;
                    vector[k] = 1;
                }
                data[i][j] = vector;
            }
        }

        return data;
    }

    public float[] toVector() {

        int cells = grid.length * grid.length;
        float[] vector = new float[cells * 2];
        Arrays.fill(vector, 0);

        int counter = 0;
        for (BoardCell[] row: grid) {
            for (BoardCell cell: row) {
                if (cell != BoardCell.Empty) {
                    int next = cell.getColor() == currentPlayer ? 0 : cells;
                    vector[counter + next] = 1;
                }
                counter++;
            }
        }

        return vector;
    }

    public INDArray toINDArray() {
        float[] vector = toVector();
        return Nd4j.create(vector, new int[]{1, vector.length});
    }

}
