package CLI;

import Board.Helpers.ApproximateMoveSelector;
import Board.Helpers.ForcedMoveSelector;
import Board.Helpers.MoveSelectors;
import Board.SimpleGame;
import Contract.Game;
import Evaluation.WinLossEvaluation;
import NeuralNetwork.TrainingGame;
import Player.MCTSPlayer;
import Player.RandomPlayer;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenerateGames {

    private static List<TrainingGame> games;
    private static int max_training_games = 1000;
    private static String filePath = "src/main/resources/training_games_mcts800_forced3_approximate_forced1game.ser";
    private static int save_interval = 10;

    public static void main(String[] args) {

//        save games on exit signal
//        Runtime.getRuntime().addShutdownHook(new Thread() {
//            @Override
//            public void run() {save_games();};
//        });

        games = new ArrayList<>();

        if (args.length > 0) {
            filePath = args[0];
        }
        load_games();
        generate_games(1000);
        save_games();
    }

    private static void generate_games(int number_of_games) {
//        MCTSPlayer player2 = new MCTSPlayer(MoveSelectors.get("forced3"), MoveSelectors.get("approximate"), 400, true);
//        MCTSPlayer player2 = new MCTSPlayer(MoveSelectors.get("forced3"), MoveSelectors.get("approximate"), 400, true);
        List<Integer> list = Arrays.asList(new Integer[number_of_games]);
        //parallel loop
        list.parallelStream().forEach(i -> {
            Game forced1RandomGame = new SimpleGame(new RandomPlayer(
                    new ForcedMoveSelector(new WinLossEvaluation(), new ApproximateMoveSelector()
                    )));
            MCTSPlayer player = new MCTSPlayer(MoveSelectors.get("forced3"), MoveSelectors.get("approximate"), forced1RandomGame, 400, true);
            TrainingGame game = new TrainingGame(player, player,9);
            System.out.println("Game won by: " + game.getWinner());
            System.out.println("Number of turns: " + game.getHistory().size());
            games.add(game);
            if(games.size() % save_interval == 0) {
                save_games();
            }
        });

    }

    private static void load_games() {
        try {
            File file = new File(filePath);
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            games = (ArrayList<TrainingGame>) oi.readObject();
            oi.close();
            fi.close();
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static void save_games() {
        try {
            System.out.println("Saving " +  games.size() + " games");
            File file = new File(filePath);
            FileOutputStream fileOut = new FileOutputStream(file, false);
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(new ArrayList(games));
            out.close();
            fileOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
