package Player;

import Board.Helpers.AllMovesSelector;
import Board.Helpers.ApproximateMoveSelector;
import Contract.Player;
import Evaluation.WinLossEvaluation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Players {

    final static private Map<String, Player> players = new HashMap<>();

    static {
        players.put("human", new CliPlayer());
        players.put("random", new RandomPlayer(new ApproximateMoveSelector()));
        players.put("simpleton", new EvaluationPlayer(new WinLossEvaluation()));
        players.put("minmax3", new MinMaxPlayer(new WinLossEvaluation(), new ApproximateMoveSelector(), 3));
        players.put("minmax5", new MinMaxPlayer(new WinLossEvaluation(), new ApproximateMoveSelector(), 5));
        players.put("mcts1s",new MCTSPlayer(new ApproximateMoveSelector(),1));
        players.put("mcts3s",new MCTSPlayer(new ApproximateMoveSelector(),3));
        players.put("mcts10s",new MCTSPlayer(new ApproximateMoveSelector(),10));
        players.put("mcts60s",new MCTSPlayer(new ApproximateMoveSelector(),60));
    }

    public static Player getPlayer(String name) {
        return players.get(name);
    }

    public static Collection<Player> getPlayers() {
        return players.values();
    }

    public static Collection<String> getPlayerNames() {
        return players.keySet();
    }

}
