package Contract;

import java.io.Serializable;
import java.util.Arrays;

public class BoardState implements Serializable {
    Color currentPlayer;
    BoardCell[][] grid;

    public BoardState(Color currentPlayer, BoardCell[][] grid) {
        this.currentPlayer = currentPlayer;
        this.grid = grid;
    }

    public Color getCurrentPlayer() {
        return currentPlayer;
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
}
