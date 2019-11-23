package Player;

import Contract.*;

import java.util.*;

public class RandomPlayer implements Player {

    private MoveSelector moveSelector;

    public RandomPlayer(MoveSelector moveSelector) {
        this.moveSelector = moveSelector;
    }

    public Move getMove(ReadableBoard board) {

        Move move;
        Random random = new Random();

        List<Move> moves = new ArrayList<>(moveSelector.getMoves(board));

        return moves.get(random.nextInt(moves.size()));

    }

}
