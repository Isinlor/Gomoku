package Player;

import Contract.*;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class RandomPlayer implements Player {

    private MoveSelector moveSelector;

    public RandomPlayer(MoveSelector moveSelector) {
        this.moveSelector = moveSelector;
    }

    public Move getMove(ReadableBoard board) {

        Move move;
        Random random = new Random();

        List<Move> moves = moveSelector.getMoves(board);

        return moves.get(random.nextInt(moves.size()));

    }

}
