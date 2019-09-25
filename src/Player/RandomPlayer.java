package Player;

import Contract.*;

import java.util.Random;

public class RandomPlayer implements PlayerInterface {

    public Move getMove(ReadableBoard board) {

        Move move;
        Random random = new Random();

        do {
            move = new Move(random.nextInt(board.getSize()), random.nextInt(board.getSize()));
        } while (board.getCell(move.x, move.y) != BoardCell.Empty);

        return move;

    }

}
