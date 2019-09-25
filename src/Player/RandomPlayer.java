package Player;

import Contract.BoardCell;
import Contract.BoardInterface;
import Contract.Move;
import Contract.PlayerInterface;

import java.util.Random;

public class RandomPlayer implements PlayerInterface {

    public Move getMove(BoardInterface board) {

        Move move;
        Random random = new Random();

        do {
            move = new Move(random.nextInt(board.getSize()), random.nextInt(board.getSize()));
        } while (board.getCell(move.x, move.y) != BoardCell.Empty);

        return move;

    }

}
