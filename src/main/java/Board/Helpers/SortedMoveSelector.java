package Board.Helpers;

import Contract.Evaluation;
import Contract.Move;
import Contract.MoveSelector;
import Contract.ReadableBoard;

import java.util.*;

public class SortedMoveSelector implements MoveSelector {

    private MoveSelector moveSelector;
    private Evaluation evaluation;

    public SortedMoveSelector(MoveSelector moveSelector, Evaluation evaluation) {
        this.moveSelector = moveSelector;
        this.evaluation = evaluation;
    }

    public Collection<Move> getMoves(ReadableBoard board) {
        return MoveSorter.sort(board, moveSelector.getMoves(board), evaluation);
    }

}