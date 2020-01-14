package Player;

import Contract.*;

import java.util.*;

public class RandomPlayer implements Player {

    public static Random random = new Random();

    private MoveSelector moveSelector;

    public RandomPlayer(MoveSelector moveSelector) {
        this.moveSelector = moveSelector;
    }

    public Move getMove(ReadableBoard board) {

        Move move;
        
        List<Move> moves = new ArrayList<>(moveSelector.getMoves(board));

        return moves.get(random.nextInt(moves.size()));

    }

}
