package Board.Helpers;

import Contract.MoveSelector;
import Evaluation.Evaluations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class MoveSelectors {

    final static private Map<String, MoveSelector> moveSelectors = new HashMap<>();

    static {
        moveSelectors.put("all", new AllMovesSelector());
        moveSelectors.put("approximate", new ApproximateMoveSelector());
        moveSelectors.put("forced3", new ForcedMoveSelector(
            Evaluations.get("negamax3"),
            get("approximate")
        ));
        moveSelectors.put("forced5", new ForcedMoveSelector(
            Evaluations.get("negamax5"),
            get("forced3")
        ));
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