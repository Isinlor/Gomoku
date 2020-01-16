package NeuralNetwork;

import Contract.*;
import Player.MCTSPlayer;
import Board.SimpleBoard;

import java.io.Serializable;
import java.util.ArrayList;

public class TrainingGame implements Serializable  {

    private transient MCTSPlayer black;
    private transient MCTSPlayer white;
    public ArrayList<ExtendedBoardState> history = new ArrayList<ExtendedBoardState>();
    public Color winner;

    public TrainingGame(MCTSPlayer black, MCTSPlayer white, int boardSize) {
        this.black = black;
        this.white = white;
        play(new SimpleBoard(boardSize));
    }

    public ArrayList<ExtendedBoardState> getHistory() {
        return history;
    }

    public Color getWinner() {
        return this.winner;
    }

    private void play(Board board) {

        while(!board.isGameFinished()) {
            MCTSPlayer player = null;
            switch (board.getCurrentColor()) {
                case Black:
                    player = black;
                    break;
                case White:
                    player = white;
                    break;
            }
            board.move(player.getMove(board));

            history.add(new ExtendedBoardState(board.getBoardState(), player.getLatestWeightedMoves()));
        }
        this.winner = board.getWinner();

    }

}
