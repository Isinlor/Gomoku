package Board;

import Contract.BoardInterface;
import Contract.GameInterface;
import Contract.PlayerInterface;

public class Game implements GameInterface {

    private PlayerInterface black;
    private PlayerInterface white;

    public Game(PlayerInterface black, PlayerInterface white) {
        this.black = black;
        this.white = white;
    }

    public void play(BoardInterface board) {

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
