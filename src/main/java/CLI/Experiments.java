package CLI;

import Board.Helpers.AllMovesSelector;
import Board.Helpers.ApproximateMoveSelector;
import Board.Helpers.ForcedMoveSelector;
import Board.SimpleGame;
import Contract.Game;
import Contract.MoveSelector;
import Evaluation.NegamaxEvaluation;
import Evaluation.WinLossEvaluation;
import Player.MCTSPlayer;
import Player.MinMaxPlayer;
import Player.RandomPlayer;

public class Experiments {

    private static int boardSize = 9;
    private static int games = 100;

    // defaultIterMCTS should take around defaultTimeMCTS
    private static int defaultIterMCTS = 100;
    private static double defaultTimeMCTS = 0.01;

    public static void main(String[] args) {

        all_vs_approximate_move_selector();
        mcts_vs_minmax();

        // MCTS ablation study
        // See: https://stats.stackexchange.com/questions/380040/what-is-an-ablation-study-and-is-there-a-systematic-way-to-perform-it
        mcts_move_selector();
        mcts_rollouts();

    }

    private static void mcts_vs_minmax() {

        System.out.println("\n\n\n\n\n\nMCTS vs. MinMax - What is better?");

        System.out.println("\nMinMax 1 vs. MCTS 100 iter");
        ComparePlayers.compare(
            boardSize, games,
            "MinMax 1",
            new MinMaxPlayer(new WinLossEvaluation(), new ApproximateMoveSelector(), 1),
            "MCTS 100 iter",
            new MCTSPlayer(new ApproximateMoveSelector(), defaultIterMCTS, true)
        );

        System.out.println("\nMinMax 1 vs. MCTS 350 iter");
        ComparePlayers.compare(
            boardSize, games,
            "MinMax 1",
            new MinMaxPlayer(new WinLossEvaluation(), new ApproximateMoveSelector(), 1),
            "MCTS 350 iter",
            new MCTSPlayer(new ApproximateMoveSelector(), 350, true)
        );

        System.out.println("\n" +
            "Comparing MinMax and MCTS is difficult.\n" +
            "Here we aim for about equal strength and look at time."
        );

        System.out.println("\nMinMax 2 vs. MCTS 500 iter (empirically set for about win ratio about 0.5)");
        ComparePlayers.compare(
            boardSize, games,
            "MinMax 2",
            new MinMaxPlayer(new WinLossEvaluation(), new ApproximateMoveSelector(), 2),
            "MCTS 500 iter",
            new MCTSPlayer(new ApproximateMoveSelector(), 500, true)
        );

        System.out.println("\nMinMax 3 vs. MCTS 1500 iter (empirically set for about win ratio about 0.5)");
        ComparePlayers.compare(
            boardSize, games,
            "MinMax 3",
            new MinMaxPlayer(new WinLossEvaluation(), new ApproximateMoveSelector(), 3),
            "MCTS 1500 iter",
            new MCTSPlayer(new ApproximateMoveSelector(), 1500, true)
        );

    }

    private static void all_vs_approximate_move_selector() {

        System.out.println("\n\n\n\n\n\nApproximate Moves vs. All Moves - What is better?");

        System.out.println("\nRandom");
        ComparePlayers.compare(
            boardSize, games,
            "all moves",
            new RandomPlayer(new AllMovesSelector()),
            "approximate moves",
            new RandomPlayer(new ApproximateMoveSelector())
        );

        System.out.println("\nMinMax 2");
        ComparePlayers.compare(
            boardSize, games,
            "all moves",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(new WinLossEvaluation(), new AllMovesSelector(), 2),
                    new AllMovesSelector()
                )
            ),
            "approximate moves",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 2),
                    new ApproximateMoveSelector()
                )
            )
        );

        Game allRandomGame = new SimpleGame(new RandomPlayer(new AllMovesSelector()));
        Game approximateRandomGame = new SimpleGame(new RandomPlayer(new ApproximateMoveSelector()));

        System.out.println("\nMCTS 100 iter");
        ComparePlayers.compare(
            boardSize, games,
            "all moves",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultIterMCTS, true),
            "approximate moves",
            new MCTSPlayer(new ApproximateMoveSelector(), approximateRandomGame, defaultIterMCTS, true)
        );

    }

    private static void mcts_move_selector() {

        System.out.println("\n\n\n\n\n\n" +
            "How move selectors affect MCTS?\n" +
            "We compare move selectors: all, approximate, forced 1\n" +
            "The rollout is the same in all comparisons - all rollout."
        );

        Game allRandomGame = new SimpleGame(new RandomPlayer(new AllMovesSelector()));
        MoveSelector forced1MoveSelector = new ForcedMoveSelector(
            new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 1),
            new ApproximateMoveSelector()
        );

        System.out.println("\nAll vs. Approximate, the same amount of iterations");
        ComparePlayers.compare(
            boardSize, games,
            "all moves",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultIterMCTS, true),
            "approximate moves",
            new MCTSPlayer(new ApproximateMoveSelector(), allRandomGame, defaultIterMCTS, true)
        );

        System.out.println("\nAll vs. Approximate, the same amount of time");
        ComparePlayers.compare(
            boardSize, games,
            "all moves",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultTimeMCTS, false),
            "approximate moves",
            new MCTSPlayer(new ApproximateMoveSelector(), allRandomGame, defaultTimeMCTS, false)
        );

        System.out.println("\nAll vs. Forced 1, the same amount of iterations");
        ComparePlayers.compare(
            boardSize, games,
            "all moves",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultIterMCTS, true),
            "forced 1",
            new MCTSPlayer(forced1MoveSelector, allRandomGame, defaultIterMCTS, true)
        );

        System.out.println("\nAll vs. Forced 1, the same amount of time");
        ComparePlayers.compare(
            boardSize, games,
            "all moves",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultTimeMCTS, false),
            "forced 1",
            new MCTSPlayer(forced1MoveSelector, allRandomGame, defaultTimeMCTS, false)
        );

    }

    private static void mcts_rollouts() {

        System.out.println("\n\n\n\n\n\n" +
            "How rollouts affect MCTS?\n" +
            "We compare 3 rollouts: fully random, sampled from approximate move, sampled from forced 1 moves\n" +
            "The move selector is the same in all comparisons - all move selector."
        );

        Game allRandomGame = new SimpleGame(new RandomPlayer(new AllMovesSelector()));
        Game approximateRandomGame = new SimpleGame(new RandomPlayer(new ApproximateMoveSelector()));
        Game forced1RandomGame = new SimpleGame(new RandomPlayer(
            new ForcedMoveSelector(new WinLossEvaluation(), new ApproximateMoveSelector()
        )));

        System.out.println("\nAll vs. Approximate rollout, same amount of rollouts");
        ComparePlayers.compare(
            boardSize, games,
            "all rollout",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultIterMCTS, true),
            "approximate rollout",
            new MCTSPlayer(new AllMovesSelector(), approximateRandomGame, defaultIterMCTS, true)
        );

        System.out.println("\nAll vs. Approximate rollout, same amount of time");
        ComparePlayers.compare(
            boardSize, games,
            "all rollout",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultTimeMCTS, false),
            "approximate rollout",
            new MCTSPlayer(new AllMovesSelector(), approximateRandomGame, defaultTimeMCTS, false)
        );

        System.out.println("\nWeak vs. Strong rollout, same amount of rollouts");
        ComparePlayers.compare(
            boardSize, games,
            "all rollout",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultIterMCTS, true),
            "forced 1 rollout",
            new MCTSPlayer(new AllMovesSelector(), forced1RandomGame, defaultIterMCTS, true)
        );

        System.out.println("\nWeak vs. Strong rollout, same amount of time");
        ComparePlayers.compare(
            boardSize, 10, // answer seems clear even in 10 games
            "all rollout",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultTimeMCTS, false),
            "forced 1 rollout",
            new MCTSPlayer(new AllMovesSelector(), forced1RandomGame, defaultTimeMCTS, false)
        );

    }

}