package Board.Helpers;

import Board.*;
import Contract.*;

import java.util.*;

public class MoveSorter {

    public static List<Move> sort(ReadableBoard board, Collection<Move> moves, Evaluation evaluation) {
        Map<Move, Double> evaluations = new HashMap<>();
        SimpleBoard boardCopy = new SimpleBoard(board);
        for (Move move: moves) {
            boardCopy.move(move);
            evaluations.put(move, evaluation.evaluate(boardCopy));
            boardCopy.revertLastMove();
        }
        if(moves instanceof List) {
            List<Move> moveList = (List<Move>)moves;
            return sort(moveList, evaluations);
        }
        return sort(moves, evaluations);
    }

    public static List<Move> sort(Collection<Move> moves, Map<Move, Double> evaluations) {
        return sort(new ArrayList<>(moves), evaluations);
    }

    public static List<Move> sort(List<Move> moves, Map<Move, Double> evaluations) {
        moves.sort(new Comparator<Move>() {
            public int compare(Move a, Move b) {
                double aEval = evaluations.get(a);
                double bEval = evaluations.get(b);
                if (aEval == bEval) return 0;
                return aEval > bEval ? 1 : -1;
            }
        });
        return moves;
    }

}