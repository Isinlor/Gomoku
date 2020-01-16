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

import java.util.ArrayList;

public class Experiments {

    private static int boardSize = 9;
    private static int games = 100;

    // defaultIterMCTS should take around defaultTimeMCTS
    private static int defaultIterMCTS = 100;
    private static double defaultTimeMCTS = 0.01;

    public static void main(String[] args) {

        System.out.println("Board size: " + boardSize);
        System.out.println("Default amount of iterations: " + defaultIterMCTS);
        System.out.println("Default amount of time: " + defaultIterMCTS);

        // You may also want to read: https://en.wikipedia.org/wiki/Statistical_significance
        what_is_significant_strength_difference();

        all_vs_approximate_move_selector();
        mcts_vs_minmax();

        // MCTS ablation study
        // See: https://stats.stackexchange.com/questions/380040/what-is-an-ablation-study-and-is-there-a-systematic-way-to-perform-it
        mcts_move_selector();
        mcts_rollouts();
        mcts_forced_rollouts();

        // This is a tricky test. Requires code changes to run.
        // global_vs_local_approximate_move_selector();

    }

    private static void what_is_significant_strength_difference() {

        System.out.println("\nWhat is significant difference in wins between players?");

        System.out.println("Differences in wins between equal players in " + games + " games.");
        ArrayList<Integer> differences = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Results results = ComparePlayers.compare(
                boardSize, games,
                "fully random",
                new RandomPlayer(new AllMovesSelector()),
                "fully random",
                new RandomPlayer(new AllMovesSelector()),
                false
            );
            int winDifference = Math.abs(results.winA - results.winB);
            System.out.print(winDifference + "\t" + (i % 10 == 9 ? "\n" : ""));
            differences.add(winDifference);
        }

        int sum = 0;
        int max = 0;
        for (Integer diff: differences) {
            sum += diff;
            max = Math.max(max, diff);
        }
        float mean_absolute_deviation = sum / (float)differences.size();

        System.out.println("On average you can expect " + mean_absolute_deviation + " difference in wins between equal players.");
        System.out.println("Max observed difference " + max + " in wins between equal players.");
        System.out.println("In other words, differences of that size are not necessarily significant.");

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

        System.out.println("\n\nAll vs. Approximate rollout\n\n");

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

        System.out.println("\n\nAll vs. Forced 1 rollout\n\n");

        System.out.println("\nAll vs. Forced 1 rollout, same amount of rollouts");
        ComparePlayers.compare(
            boardSize, games,
            "all rollout",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultIterMCTS, true),
            "forced 1 rollout",
            new MCTSPlayer(new AllMovesSelector(), forced1RandomGame, defaultIterMCTS, true)
        );

        System.out.println("\nAll vs. Forced 1 rollout, same amount of time");
        ComparePlayers.compare(
            boardSize, games,
            "all rollout",
            new MCTSPlayer(new AllMovesSelector(), allRandomGame, defaultTimeMCTS, false),
            "forced 1 rollout",
            new MCTSPlayer(new AllMovesSelector(), forced1RandomGame, defaultTimeMCTS, false)
        );

        System.out.println("\n\nApproximate vs. Forced 1 rollout\n\n");

        System.out.println("\nApproximate vs. Forced 1 rollout, same amount of rollouts");
        ComparePlayers.compare(
            boardSize, games,
            "approximate rollout",
            new MCTSPlayer(new AllMovesSelector(), approximateRandomGame, defaultIterMCTS, true),
            "forced 1 rollout",
            new MCTSPlayer(new AllMovesSelector(), forced1RandomGame, defaultIterMCTS, true)
        );

        System.out.println("\nApproximate vs. Forced 1 rollout, same amount of time");
        ComparePlayers.compare(
            boardSize, games,
            "approximate rollout",
            new MCTSPlayer(new AllMovesSelector(), approximateRandomGame, defaultTimeMCTS, false),
            "forced 1 rollout",
            new MCTSPlayer(new AllMovesSelector(), forced1RandomGame, defaultTimeMCTS, false)
        );

        System.out.println("\n\nForced 1 rollout appears to be the best.\n\n");
    }

    private static void mcts_forced_rollouts() {

        System.out.println("\n\nIs Forced 2 rollout better than Forced 1 rollout?\n\n");

        Game forced1WinLossRandomGame = new SimpleGame(new RandomPlayer(
            new ForcedMoveSelector(
                new WinLossEvaluation(),
                new ApproximateMoveSelector()
            )
        ));

        Game forced1NegamaxRandomGame = new SimpleGame(new RandomPlayer(
            new ForcedMoveSelector(
                new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 1),
                new ApproximateMoveSelector()
            )
        ));

        Game forced2RandomGame = new SimpleGame(new RandomPlayer(
            new ForcedMoveSelector(
                new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 2),
                new ApproximateMoveSelector()
            )
        ));

        System.out.println("\nFirst of all, what is the impact of different implementations on Forced 1 rollout?");
        System.out.println("\nForced 1 WinLoss vs. Forced 1 Negamax rollout, same amount of time");
        ComparePlayers.compare(
            boardSize, games,
            "forced 1 based on winloss rollout",
            new MCTSPlayer(new AllMovesSelector(), forced1WinLossRandomGame, defaultTimeMCTS, false),
            "forced 1 based on negamax rollout",
            new MCTSPlayer(new AllMovesSelector(), forced1NegamaxRandomGame, defaultTimeMCTS, false)
        );
        System.out.println("\nAs expected, negamax implementation seems slower and therefore appears to be weaker.");
        System.out.println("\nUnfortunately, Forced 2 can be setup only with negamax evaluation.");

        System.out.println("\nForced 1 vs. Forced 2 rollout, same amount of time");
        ComparePlayers.compare(
            boardSize, games,
            "forced 1 winloss rollout",
            new MCTSPlayer(new AllMovesSelector(), forced1WinLossRandomGame, defaultTimeMCTS, false),
            "forced 2 negamax rollout",
            new MCTSPlayer(new AllMovesSelector(), forced2RandomGame, defaultTimeMCTS, false)
        );

        System.out.println("\nForced 1 vs. Forced 2 rollout, same amount of time, but both get double default time");
        ComparePlayers.compare(
            boardSize, games,
            "forced 1 winloss rollout",
            new MCTSPlayer(new AllMovesSelector(), forced1WinLossRandomGame, defaultTimeMCTS * 2, false),
            "forced 2 negamax rollout",
            new MCTSPlayer(new AllMovesSelector(), forced2RandomGame, defaultTimeMCTS * 2, false)
        );
        System.out.println("\nForced 2 seems to be significantly weaker than Forced 1, no matter the amount of time.");

    }

    @SuppressWarnings("unused")
    private static void global_vs_local_approximate_move_selector() {
        System.out.println("\n\n\n\n\n\nGlobal vs. Local Approximate Move Selector - What is faster?");
        System.out.println("(This is probably not interesting for the paper as it is strictly implementation specific.)");
        System.out.println(
            "Testing this appears to be very tricky.\n" +
            "Most importantly, global version should not be punished by board maintaining data for local version.\n" +
            "You will need to modify the simple board code to run this test!!!"
        );

        System.out.println("\nMinMax 2");
        ComparePlayers.compare(
            boardSize, games * 10,
            "Global Approx. Move Selector",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 1),
                    new ApproximateMoveSelector()
                )
            ),
            "Local Approx. Move Selector",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 1),
                    new ApproximateMoveSelector()
                )
            )
        );
    }

}