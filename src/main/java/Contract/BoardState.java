package Contract;

import java.io.Serializable;

public class BoardState implements Serializable {
    Color currentPlayer;
    BoardCell[][] grid;

    public BoardState(Color currentPlayer, BoardCell[][] grid) {
        this.currentPlayer = currentPlayer;
        this.grid = grid;
    }
}
