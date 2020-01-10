package Board;

import Contract.Board;
import Contract.Player;

public class SimpleGame implements Contract.Game {

    private Player black;
    private Player white;

    public SimpleGame(Player black, Player white) {
        this.black = black;
        this.white = white;
    }

    public void play(Board board) {

        while(!board.isGameFinished()) {
            switch (board.getCurrentColor()) {
                case Black:
                    board.move(black.getMove(board));
                    break;
                case White:
                    board.move(white.getMove(board));
                    break;
            }
        }

    }

}
