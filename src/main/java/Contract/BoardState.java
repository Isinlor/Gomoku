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
        float[] vector = new float[grid.length * grid.length * 2];
        Arrays.fill(vector, 0);
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid.length; j++) {
                if(grid[i][j] == BoardCell.Empty) continue;
                int position = grid[i][j].getColor() == currentPlayer ? 1 : 2;
                vector[i * j * position] = 1;
            }
        }
        return vector;
    }
}
