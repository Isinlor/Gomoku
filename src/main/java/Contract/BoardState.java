package Contract;

import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.Serializable;
import java.util.Arrays;

public class BoardState implements Serializable {

    private static final long serialVersionUID = 1;

    BoardCell[][] grid;
    Color currentPlayer;
    Move lastMove;

    public BoardState(BoardCell[][] grid, Color currentPlayer, Move lastMove) {
        this.grid = grid;
        this.currentPlayer = currentPlayer;
        this.lastMove = lastMove;
    }

    public BoardState() {
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

    public double[][][] toMultiDimensionalMatrix() {
        int channels = 2; //one for current player, one for other player
        double[][][] data = new double[channels][grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                BoardCell cell = grid[i][j];
                if (cell != BoardCell.Empty) {
                    int c = cell.getColor() == currentPlayer ? 0 : 1;
                    double value = 1;
                    //set to 10 if last move
                    //TODO set to 10 also for the last last move (2 moves ago)
                    if(i == lastMove.x && j == lastMove.y) {
                        value = 10;
                    }
                    data[c][i][j] = value;
                }
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
