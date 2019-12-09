package Player;

import Board.Helpers.ApproximateMoveSelector;
import Board.Helpers.ForcedMoveSelector;
import Board.Helpers.MoveSelectors;
import Contract.Evaluation;
import Contract.Player;
import Evaluation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Players {

    final static private Map<String, Player> players = new HashMap<>();

    static {
        players.put("human", new CliPlayer());
        players.put("random", new RandomPlayer(MoveSelectors.get("approximate")));
        players.put("random3", new RandomPlayer(MoveSelectors.get("forced3")));
        players.put("simpleton", new EvaluationPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate")));
        players.put("minmax1", new MinMaxPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate"), 1));
        players.put("minmax3", new MinMaxPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate"), 3));
        players.put("minmax5", new MinMaxPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate"), 5));
        players.put("minmax6", new MinMaxPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate"), 6));
        players.put("minmax7", new MinMaxPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate"), 7));
        players.put("negamax3", new EvaluationPlayer(Evaluations.get("negamax3"), MoveSelectors.get("approximate")));
        players.put("mcts1s",new MCTSPlayer(MoveSelectors.get("approximate"),1));
        players.put("mcts3s",new MCTSPlayer(MoveSelectors.get("approximate"),3));
        players.put("mcts10s",new MCTSPlayer(MoveSelectors.get("approximate"),10));
        players.put("mcts10s+",new MCTSPlayer(
            MoveSelectors.get("forced3"), MoveSelectors.get("approximate"), 10
        ));
        players.put("mcts30s",new MCTSPlayer(MoveSelectors.get("approximate"),30));
        players.put("mcts30s+",new MCTSPlayer(
            MoveSelectors.get("forced5"), MoveSelectors.get("approximate"), 30
        ));
        players.put("mcts60s",new MCTSPlayer(MoveSelectors.get("approximate"),60));
        players.put("mcts60s+",new MCTSPlayer(
            MoveSelectors.get("forced5"), MoveSelectors.get("approximate"), 60
        ));
        players.put("threat", new EvaluationPlayer(new ThreatSearchGlobal(), MoveSelectors.get("approximate")));
    }

    public static Player get(String name) {
        return players.get(name);
    }

    public static Collection<Player> getAll() {
        return players.values();
    }

    public static Collection<String> getNames() {
        return players.keySet();
    }

}
