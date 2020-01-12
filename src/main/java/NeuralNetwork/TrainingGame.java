package NeuralNetwork;

import Contract.Board;
import Contract.BoardCell;
import Contract.BoardState;
import Contract.Color;
import Player.MCTSPlayer;
import Board.SimpleBoard;

import java.io.Serializable;
import java.util.ArrayList;

public class TrainingGame implements Serializable  {

    private transient MCTSPlayer black;
    private transient MCTSPlayer white;
    public ArrayList<BoardState> history = new ArrayList<BoardState>();
    public Color winner;

    public TrainingGame(MCTSPlayer black, MCTSPlayer white, int boardSize) {
        this.black = black;
        this.white = white;
        play(new SimpleBoard(boardSize));
    }

    public ArrayList<BoardState> getHistory() {
        return history;
    }

    public Color getWinner() {
        return this.winner;
    }

    private void play(Board board) {

        while(!board.isGameFinished()) {
            switch (board.getCurrentColor()) {
                case Black:
                    board.move(black.getMove(board));
                    break;
                case White:
                    board.move(white.getMove(board));
                    break;
            }
            history.add(board.getBoardState());
        }
        this.winner = board.getWinner();

    }

}
