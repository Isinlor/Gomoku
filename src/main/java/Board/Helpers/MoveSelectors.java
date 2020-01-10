package Board.Helpers;

import Contract.MoveSelector;
import Evaluation.Evaluations;
import Evaluation.NegamaxEvaluation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MoveSelectors {

    final static private Map<String, MoveSelector> moveSelectors = new HashMap<>();

    static {
        moveSelectors.put("all", new AllMovesSelector());
        moveSelectors.put("approximate", new ApproximateMoveSelector());
        for (int i = 1; i <= 7 ; i++) {
            moveSelectors.put("forced" + i, new ForcedMoveSelector(
                Evaluations.get("negamax" + i),
                i <= 3 ? get("approximate") : get("forced3")
            ));
        }
    }

    public static MoveSelector get(String name) {
        return moveSelectors.get(name);
    }

    public static Collection<MoveSelector> getAll() {
        return moveSelectors.values();
    }

    public static Collection<String> getNames() {
        return moveSelectors.keySet();
    }

}