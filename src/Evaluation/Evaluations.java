package Evaluation;

import Board.Helpers.ApproximateMoveSelector;
import Contract.Evaluation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Evaluations {

    final static private Map<String, Evaluation> evaluations = new HashMap<>();

    static {
        evaluations.put("winLoss", new WinLossEvaluation());
        evaluations.put("negamax3", new NegamaxEvaluation(
            get("winLoss"), new ApproximateMoveSelector(), 3)
        );
        evaluations.put("negamax5", new NegamaxEvaluation(
            get("winLoss"), new ApproximateMoveSelector(), 5)
        );
        evaluations.put("negamax7", new NegamaxEvaluation(
            get("winLoss"), new ApproximateMoveSelector(), 7)
        );
    }

    public static Evaluation get(String name) {
        return evaluations.get(name);
    }

    public static Collection<Evaluation> getAll() {
        return evaluations.values();
    }

    public static Collection<String> getNames() {
        return evaluations.keySet();
    }

}