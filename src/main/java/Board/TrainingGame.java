package Board;

import Contract.Board;
import Contract.BoardCell;
import Contract.BoardState;
import Contract.Color;
import Player.MCTSPlayer;

import java.io.Serializable;
import java.util.ArrayList;

public class TrainingGame implements Serializable, Contract.Game  {

    private transient MCTSPlayer black;
    private transient MCTSPlayer white;
    public ArrayList history = new ArrayList<BoardState>();
    public Color winner;

    public TrainingGame(MCTSPlayer black, MCTSPlayer white) {
        this.black = black;
        this.white = white;
    }

    public ArrayList getHistory() {
        return history;
    }

    public Color getWinner() {
        return this.winner;
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
            history.add(new BoardState(board.getCurrentColor(), board.getBoardState()));
        }
        this.winner = board.getWinner();

    }

}
