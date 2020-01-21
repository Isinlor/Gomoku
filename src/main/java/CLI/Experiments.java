package CLI;

import Board.Helpers.AllMovesSelector;
import Board.Helpers.ApproximateMoveSelector;
import Board.Helpers.ForcedMoveSelector;
import Board.Helpers.SortedMoveSelector;
import Board.SimpleGame;
import Contract.Game;
import Contract.MoveSelector;
import Contract.Player;
import Evaluation.*;
import Player.*;
import Tree.MCTSBest;
import Tree.MCTSDistribution;
import UI.Logger;

import java.util.ArrayList;

public class Experiments {

    private static int boardSize = 9;
    private static int games = 100;

    // defaultIterMCTS should take around defaultTimeMCTS
    private static int defaultIterMCTS = 100;
    private static double defaultTimeMCTS = 0.01;

    public static void main(String[] args) {

        Logger.enabled = false;
        System.out.println("Board size: " + boardSize);
        System.out.println("Default amount of iterations: " + defaultIterMCTS);
        System.out.println("Default amount of time: " + defaultIterMCTS);

        // You may also want to read: https://en.wikipedia.org/wiki/Statistical_significance
        what_is_significant_strength_difference();

        draws();
        all_vs_approximate_move_selector();
        evaluations();
        mcts_vs_minmax();

        alpha_beta_evaluation();

        // MCTS ablation study
        // See: https://stats.stackexchange.com/questions/380040/what-is-an-ablation-study-and-is-there-a-systematic-way-to-perform-it
        mcts_move_selector();
        mcts_smart_vs_quick_move_selector();
        mcts_rollouts();
        mcts_forced_rollouts();
        constant_options_mcts();

        // This is a tricky test. Requires code changes to run.
        // global_vs_local_approximate_move_selector();

    }

    private static void draws() {
        System.out.println("\n\n\n\n\n\nHow likely are draws on different board sizes?");

        System.out.println("\nBoard: 5x5");
        ComparePlayers.compare(
            5, games,
            "negamax 3",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(
                        new WinLossEvaluation(), new ApproximateMoveSelector(), 3
                    ),
                    new ApproximateMoveSelector()
                )
            ),
            "negamax 3",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(
                        new WinLossEvaluation(), new ApproximateMoveSelector(), 3
                    ),
                    new ApproximateMoveSelector()
                )
            )
        );

        System.out.println("\nBoard: 7x7");
        ComparePlayers.compare(
            7, games,
            "negamax 3",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(
                        new WinLossEvaluation(), new ApproximateMoveSelector(), 3
                    ),
                    new ApproximateMoveSelector()
                )
            ),
            "negamax 3",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(
                        new WinLossEvaluation(), new ApproximateMoveSelector(), 3
                    ),
                    new ApproximateMoveSelector()
                )
            )
        );

        System.out.println("\nBoard: 9x9");
        ComparePlayers.compare(
            9, games,
            "negamax 3",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(
                        new WinLossEvaluation(), new ApproximateMoveSelector(), 3
                    ),
                    new ApproximateMoveSelector()
                )
            ),
            "negamax 3",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(
                        new WinLossEvaluation(), new ApproximateMoveSelector(), 3
                    ),
                    new ApproximateMoveSelector()
                )
            )
        );
    }

    private static void alpha_beta_evaluation() {

        System.out.println("\n\n\n\n\n\nWhat are the effects of evaluations on AlphaBeta pruning?");
        System.out.println("\nEspecially how does it affect speed?\n\n");

        alpha_beta_count_evaluation(2);
        alpha_beta_count_evaluation(3);

    }

    private static void alpha_beta_count_evaluation(int depth) {

        ComparePlayers.compare(
            boardSize, games,
            "minmax " + depth + " count, no sort",
            new MinMaxPlayer(
                new CountEvaluation(),
                new ApproximateMoveSelector(),
                depth
            ),
            "forced " + depth + " win loss",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(
                        new WinLossEvaluation(),
                        new ApproximateMoveSelector(),
                        depth
                    ),
                    new ApproximateMoveSelector()
                )
            )
        );

        ComparePlayers.compare(
            boardSize, games,
            "minmax " + depth + " count, sort",
            new MinMaxPlayer(
                new CountEvaluation(),
                new SortedMoveSelector(new ApproximateMoveSelector(), new CountEvaluation()),
                depth
            ),
            "forced " + depth + " win loss",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(
                        new WinLossEvaluation(),
                        new ApproximateMoveSelector(),
                        depth
                    ),
                    new ApproximateMoveSelector()
                )
            )
        );

        ComparePlayers.compare(
            boardSize, games,
            "minmax " + depth + " win loss, sort",
            new MinMaxPlayer(
                new WinLossEvaluation(),
                new SortedMoveSelector(new ApproximateMoveSelector(), new CountEvaluation()),
                depth
            ),
            "forced " + depth + " win loss",
            new RandomPlayer(
                new ForcedMoveSelector(
                    new NegamaxEvaluation(
                        new WinLossEvaluation(),
                        new ApproximateMoveSelector(),
                        depth
                    ),
                    new ApproximateMoveSelector()
                )
            )
        );

    }

    private static void evaluations() {

        System.out.println("\n\n\n\n\n\nDifferent evaluation functions - What is better?");

        ForcedMoveSelector forced1MoveSelector = new ForcedMoveSelector(
            new WinLossEvaluation(),
            new ApproximateMoveSelector()
        );
        Player forced1RandomPlayer = new RandomPlayer(forced1MoveSelector);

        System.out.println("\n\nWin Loss Evaluation");
        ComparePlayers.compare(
            boardSize, games,
            "win loss",
            new SamplingEvaluationPlayer(new WinLossEvaluation(), new ApproximateMoveSelector()),
            "random",
            new RandomPlayer(new ApproximateMoveSelector())
        );

        ComparePlayers.compare(
            boardSize, games,
            "win loss",
            new SamplingEvaluationPlayer(new WinLossEvaluation(), new ApproximateMoveSelector()),
            "extended 1 win loss",
            new SamplingEvaluationPlayer(new ExtendedWinLossEvaluation(1), new ApproximateMoveSelector())
        );

        ComparePlayers.compare(
            boardSize, games,
            "win loss",
            new SamplingEvaluationPlayer(new WinLossEvaluation(), new ApproximateMoveSelector()),
            "extended 2 win loss",
            new SamplingEvaluationPlayer(new ExtendedWinLossEvaluation(2), new ApproximateMoveSelector())
        );

        ComparePlayers.compare(
            boardSize, games,
            "win loss",
            new SamplingEvaluationPlayer(new WinLossEvaluation(), new ApproximateMoveSelector()),
            "threat search",
            new SamplingEvaluationPlayer(new ThreatSearchGlobal(), new ApproximateMoveSelector())
        );

        ComparePlayers.compare(
            boardSize, games,
            "win loss",
            new SamplingEvaluationPlayer(new WinLossEvaluation(), new ApproximateMoveSelector()),
            "negamax 2 win loss",
            new EvaluationPlayer(
                new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 2),
                new ApproximateMoveSelector()
            )
        );

        ComparePlayers.compare(
            boardSize, games,
            "negamax 2 win loss sampling",
            new SamplingEvaluationPlayer(
                new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 2),
                new ApproximateMoveSelector()
            ),
            "negamax 2 win loss the best",
            new EvaluationPlayer(
                new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 2),
                new ApproximateMoveSelector()
            )
        );

        System.out.println("\n\nCount Evaluation");
        ComparePlayers.compare(
            boardSize, games,
            "count",
            new SamplingEvaluationPlayer(new CountEvaluation(), new ApproximateMoveSelector()),
            "win loss",
            new SamplingEvaluationPlayer(new WinLossEvaluation(), new ApproximateMoveSelector())
        );

        ComparePlayers.compare(
            boardSize, games,
            "count",
            new SamplingEvaluationPlayer(new CountEvaluation(), new ApproximateMoveSelector()),
            "threat",
            new SamplingEvaluationPlayer(new ThreatSearchGlobal(), new ApproximateMoveSelector())
        );

        ComparePlayers.compare(
            boardSize, games,
            "count",
            new SamplingEvaluationPlayer(new CountEvaluation(), new ApproximateMoveSelector()),
            "negamax 1 win loss",
            new SamplingEvaluationPlayer(
                new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 1),
                new ApproximateMoveSelector()
            )
        );

        ComparePlayers.compare(
            boardSize, games,
            "negamax 1 count",
            new SamplingEvaluationPlayer(
                new NegamaxEvaluation(new CountEvaluation(), new ApproximateMoveSelector(), 1),
                new ApproximateMoveSelector()
            ),
            "negamax 1 win loss",
            new SamplingEvaluationPlayer(
                new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 1),
                new ApproximateMoveSelector()
            )
        );

        System.out.println("\n\nThreat Search Evaluation");
        ComparePlayers.compare(
            boardSize, games,
            "threat search",
            new SamplingEvaluationPlayer(new ThreatSearchGlobal(), new ApproximateMoveSelector()),
            "random",
            new RandomPlayer(new ApproximateMoveSelector())
        );

        ComparePlayers.compare(
            boardSize, games,
            "threat search",
            new SamplingEvaluationPlayer(new ThreatSearchGlobal(), new ApproximateMoveSelector()),
            "extended 1 win loss",
            new SamplingEvaluationPlayer(new ExtendedWinLossEvaluation(1), new ApproximateMoveSelector())
        );

        System.out.println("\n\nExtended Win Loss Evaluation");
        ComparePlayers.compare(
            boardSize, games,
            "extended 1 win loss",
            new SamplingEvaluationPlayer(new ExtendedWinLossEvaluation(1), new ApproximateMoveSelector()),
            "extended 2 win loss",
            new SamplingEvaluationPlayer(new ExtendedWinLossEvaluation(2), new ApproximateMoveSelector())
        );
        ComparePlayers.compare(
            boardSize, games,
            "extended 1 win loss",
            new SamplingEvaluationPlayer(new ExtendedWinLossEvaluation(1), new ApproximateMoveSelector()),
            "extended 3 win loss",
            new SamplingEvaluationPlayer(new ExtendedWinLossEvaluation(3), new ApproximateMoveSelector())
        );


        System.out.println("\n\nCNN Evaluation");
        ComparePlayers.compare(
                boardSize, games,
                "CNN",
                new SamplingEvaluationPlayer(new NeuralNetworkCNNEvaluation("model_mcts400_forced3_all_converged.h5"), new ApproximateMoveSelector()),
                "win loss",
                new SamplingEvaluationPlayer(new WinLossEvaluation(), new ApproximateMoveSelector())
        );

        ComparePlayers.compare(
                boardSize, games,
                "CNN",
                new SamplingEvaluationPlayer(new NeuralNetworkCNNEvaluation("model_mcts400_forced3_all_converged.h5"), new ApproximateMoveSelector()),
                "count",
                new SamplingEvaluationPlayer(new CountEvaluation(), new ApproximateMoveSelector())
        );

    }

    private static void mcts_smart_vs_quick_move_selector() {

        System.out.println("\n\n\n\n\n\n" +
            "Is split between smart and quick move selector worthwhile for MCTS?\n"
        );

        Game allRandomGame = new SimpleGame(new RandomPlayer(new AllMovesSelector()));
        MoveSelector approximateMoveSelector = new ApproximateMoveSelector();
        MoveSelector forced1MoveSelector = new ForcedMoveSelector(
            new NegamaxEvaluation(new WinLossEvaluation(), new ApproximateMoveSelector(), 1),
            new ApproximateMoveSelector()
        );

        System.out.println("\nForced 1 vs. Forced 1 + approximate, the same amount of iterations");
        ComparePlayers.compare(
            boardSize, games,
            "forced 1 + forced 1",
            new MCTSPlayer(
                forced1MoveSelector, forced1MoveSelector,
                allRandomGame, defaultIterMCTS, true
            ),
            "forced 1 + approximate",
            new MCTSPlayer(
                forced1MoveSelector, approximateMoveSelector,
                allRandomGame, defaultIterMCTS, true
            )
        );

        System.out.println("\nForced 1 vs. Forced 1 + approximate, the same amount of time");
        ComparePlayers.compare(
            boardSize, games,
            "forced 1 + forced 1",
            new MCTSPlayer(
                forced1MoveSelector, forced1MoveSelector,
                allRandomGame, defaultTimeMCTS, false
            ),
            "forced 1 + approximate",
            new MCTSPlayer(
                forced1MoveSelector, approximateMoveSelector,
                allRandomGame, defaultTimeMCTS, false
            )
        );

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

        System.out.println("\nApproximate vs. Forced 1, the same amount of iterations");
        ComparePlayers.compare(
            boardSize, games,
            "approximate",
            new MCTSPlayer(new ApproximateMoveSelector(), allRandomGame, defaultIterMCTS, true),
            "forced 1",
            new MCTSPlayer(forced1MoveSelector, allRandomGame, defaultIterMCTS, true)
        );

        System.out.println("\nApproximate vs. Forced 1, the same amount of time");
        ComparePlayers.compare(
            boardSize, games,
            "approximate",
            new MCTSPlayer(new ApproximateMoveSelector(), allRandomGame, defaultTimeMCTS, false),
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

    private static void constant_options_mcts(){
        System.out.println("\n\n\n\n\n\nHow do different constants in the UCT formula for MCTS effect the MCTS Strength?");
        System.out.println("All constants must be bigger than 0. sqrt(2) Optimal according to Kocsis and Szepesv");
        //L. Kocsis, C. Szepesvari, and J. Willemson. Improved Monte-Carlo search. Univ. Tartu, Estonia, Tech., 2006.
        Game allRandomGame = new SimpleGame(new RandomPlayer(new AllMovesSelector()));
        ComparePlayers.compare(
            boardSize, games,
            "pick always best, c = 0",
            new MCTSPlayer(new ApproximateMoveSelector(), allRandomGame, new MCTSBest(0), defaultTimeMCTS, false),
            "random sampling, c = 0",
            new MCTSPlayer(new ApproximateMoveSelector(), allRandomGame, new MCTSDistribution(0), defaultTimeMCTS, false)
        );
        ComparePlayers.compare(
            boardSize, games,
            "pick always best, c = sqrt(2)",
            new MCTSPlayer(new ApproximateMoveSelector(), allRandomGame, new MCTSBest(), defaultTimeMCTS, false),
            "random sampling, c = sqrt(2)",
            new MCTSPlayer(new ApproximateMoveSelector(), allRandomGame, new MCTSDistribution(), defaultTimeMCTS, false)
        );
        ComparePlayers.compare(
                boardSize, games,
                "c = 0",
                new MCTSPlayer(new ApproximateMoveSelector(), defaultTimeMCTS, 0),
                "c = sqrt(2)",
                new MCTSPlayer(new ApproximateMoveSelector(), defaultTimeMCTS)
        );
        ComparePlayers.compare(
                boardSize, games,
                "c = 0.5",
                new MCTSPlayer(new ApproximateMoveSelector(), defaultTimeMCTS, 0.5),
                "c = sqrt(2)",
                new MCTSPlayer(new ApproximateMoveSelector(), defaultTimeMCTS)
        );


    }

}