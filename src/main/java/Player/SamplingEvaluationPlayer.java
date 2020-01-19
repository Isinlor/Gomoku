package Player;

import Distribution.*;
import Board.*;
import Contract.*;
import UI.Logger;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class SamplingEvaluationPlayer implements Player {

    private Evaluation evaluation;
    private MoveSelector moveSelector;

    public SamplingEvaluationPlayer(Evaluation evaluation, MoveSelector moveSelector) {
        this.evaluation = evaluation;
        this.moveSelector = moveSelector;
    }

    public Move getMove(ReadableBoard board) {

        double minWeight = Double.POSITIVE_INFINITY;
        Map<Move, Double> weightedMoves = new LinkedHashMap<>();
        SimpleBoard boardCopy = new SimpleBoard(board);
        Collection<Move> moves = moveSelector.getMoves(board);
        for (Move move: moves) {

            boardCopy.move(move);
            double moveEvaluation = evaluation.evaluate(boardCopy);
            boardCopy.revertLastMove();

            if(moveEvaluation == Evaluation.Win) {
                continue;
            }

            if(moveEvaluation == Evaluation.Lost) {
                return move;
            }

            weightedMoves.put(move, moveEvaluation);
            minWeight = Math.min(minWeight, moveEvaluation);

        }

        if(weightedMoves.isEmpty()) return moves.iterator().next();

        if(minWeight < 0) {
            Map<Move, Double> positivelyWeightedMoves = new LinkedHashMap<>();
            for (Move move: weightedMoves.keySet()) {
                positivelyWeightedMoves.put(move, weightedMoves.get(move) - minWeight);
            }
            return new DistributionTableMethod<Move>(positivelyWeightedMoves).sample();
        }

        return new DistributionTableMethod<Move>(weightedMoves).sample();

    }

}
