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
        evaluations.put("winLoss+", new ExtendedWinLossEvaluation());
        for (int i = 1; i <= 7 ; i++) {
            evaluations.put("negamax" + i, new NegamaxEvaluation(
                get("winLoss"), new ApproximateMoveSelector(), i)
            );
        }
        for (int i = 1; i <= 7 ; i++) {
            evaluations.put("negamax" + i + "+", new NegamaxEvaluation(
                get("winLoss+"), new ApproximateMoveSelector(), i)
            );
        }
    }

    public static Evaluation get(String name) {
        if(!evaluations.containsKey(name)) throw new RuntimeException("No evaluation: " + name);
        return evaluations.get(name);
    }

    public static Collection<Evaluation> getAll() {
        return evaluations.values();
    }

    public static Collection<String> getNames() {
        return evaluations.keySet();
    }

}