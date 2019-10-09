package Player;

import Contract.Player;
import Evaluation.WinLossEvaluation;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Players {

    final static private Map<String, Player> players = new HashMap<>();

    static {
        players.put("human", new CliPlayer());
        players.put("random", new RandomPlayer());
        players.put("simpleton", new EvaluationPlayer(new WinLossEvaluation()));
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
