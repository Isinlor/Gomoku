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
        players.put("simpleton", new EvaluationPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate")));
        players.put("minmax1", new MinMaxPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate"), 1));
        players.put("minmax3", new MinMaxPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate"), 3));
        players.put("minmax5", new MinMaxPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate"), 5));
        players.put("minmax6", new MinMaxPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate"), 6));
        players.put("minmax7", new MinMaxPlayer(Evaluations.get("winLoss"), MoveSelectors.get("approximate"), 7));
        players.put("negamax3", new EvaluationPlayer(Evaluations.get("negamax3"), MoveSelectors.get("approximate")));
        players.put("mcts0.01s",new MCTSPlayer(MoveSelectors.get("approximate"),0.01));
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
        for (int i = 1; i <= 7; i++) {
            players.put("random" + i, new RandomPlayer(MoveSelectors.get("forced" + i)));
        }
        players.put("mcts400",new MCTSPlayer(
                MoveSelectors.get("forced3"), MoveSelectors.get("approximate"), 400, true
        ));
        players.put("mcts1600",new MCTSPlayer(
                MoveSelectors.get("forced3"), MoveSelectors.get("approximate"), 1600, true
        ));
        players.put("cnn", new EvaluationPlayer(new NeuralNetworkCNNEvaluation("model.h5"), MoveSelectors.get("approximate")));
        players.put("minmax_cnn1", new MinMaxPlayer(new NeuralNetworkCNNEvaluation("model.h5"), MoveSelectors.get("approximate"), 1));
//        players.put("minmax_cnn1-v2", new MinMaxPlayer(new NeuralNetworkCNNEvaluation("model_mcts400_forced3_all.h5"), MoveSelectors.get("approximate"), 1));

    }

    public static Player get(String name) {
        if(!players.containsKey(name)) throw new RuntimeException("No player: " + name);
        return players.get(name);
    }

    public static Collection<Player> getAll() {
        return players.values();
    }

    public static Collection<String> getNames() {
        return players.keySet();
    }

}
