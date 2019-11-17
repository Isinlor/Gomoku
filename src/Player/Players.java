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
        players.put("minmax", new MinMaxPlayer(new WinLossEvaluation(), new ApproximateMoveSelector()));
        players.put("mctsA",new MCTSPlayer(new ApproximateMoveSelector(),3)); //B
        players.put("mctsB",new MCTSPlayer(new ApproximateMoveSelector(),10)); //W
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
