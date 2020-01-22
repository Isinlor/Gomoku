import Board.Helpers.MoveSelectors;
import Contract.BoardState;
import Contract.Color;
import Distribution.DistributionTableMethod;
import NeuralNetwork.Train;
import NeuralNetwork.TrainingGame;
import NeuralNetwork.Model;
import Player.MCTSPlayer;
import Player.RandomPlayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.SamplingDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class NeuralNetworkTest extends SimpleUnitTest {
    public static void main(String[] args) {

        System.out.println("\n\nNeural Network Test\n");

        int boards = 3;
        int boardSize = 2;

        int boardVectorLength = boardSize * boardSize * 2;
        INDArray features = Nd4j.zeros(boards, boardVectorLength);
        INDArray labels = Nd4j.zeros(boards, 1);

        features.putRow(0, Nd4j.create(new float[]{
            0, 0, 0, 0,
            0, 0, 0, 0,
        }, new int[]{1, boardVectorLength}));
        labels.putRow(0, Nd4j.create(new float[]{0}, new int[]{1, 1}));

        features.putRow(1, Nd4j.create(new float[]{
            1, 0, 0, 0,
            0, 0, 0, 0,
        }, new int[]{1, boardVectorLength}));
        labels.putRow(1, Nd4j.create(new float[]{1}, new int[]{1, 1}));

        features.putRow(2, Nd4j.create(new float[]{
            0, 0, 0, 0,
            1, 0, 0, 0,
        }, new int[]{1, boardVectorLength}));
        labels.putRow(2, Nd4j.create(new float[]{-1}, new int[]{1, 1}));

        DataSet dataset = new DataSet(features, labels);
        DataSetIterator iterator = new SamplingDataSetIterator(dataset, 3, 1);

        it("overfits dumb dataset", () -> {
            MultiLayerNetwork model = Model.get(boardSize);
            assertEqual(model.output(features).toFloatVector(), new float[]{0,0,0}, 0.5);
            for (int i = 0; i < 25; i++) {
                model.fit(dataset);
            }
            assertEqual(model.output(features).toFloatVector(), new float[]{0,1,-1}, 0.02);
        });

        it("overfits dumb dataset using dataset SamplingDataSetIterator", () -> {
            MultiLayerNetwork model = Model.get(boardSize);
            assertEqual(model.output(features).toFloatVector(), new float[]{0,0,0}, 0.5);
            model.fit(iterator, 25);
            assertEqual(model.output(features).toFloatVector(), new float[]{0,1,-1}, 0.02);
            assertEqual(model.score(dataset), 0, 0.001);
        });

        xit("overfits simple games", () -> {

            int size = 7;
            RandomPlayer.random = new Random(123);
            DistributionTableMethod.rand = new Random(123);
            ArrayList<TrainingGame> trainingGames = new ArrayList<>();

            boolean hasBlackWinner = false;
            boolean hasWhiteWinner = false;
            TrainingGame blackWinnerGame = null;
            TrainingGame whiteWinnerGame = null;
            while(!hasBlackWinner || !hasWhiteWinner) {
                TrainingGame trainingGame = new TrainingGame(
                    new MCTSPlayer(MoveSelectors.get("all"), 10, true),
                    new MCTSPlayer(MoveSelectors.get("all"), 10, true),
                    size
                );
                if(!hasBlackWinner && trainingGame.getWinner() == Color.Black) {
                    trainingGames.add(trainingGame);
                    hasBlackWinner = true;
                    blackWinnerGame = trainingGame;
                }
                if(!hasWhiteWinner && trainingGame.getWinner() == Color.White) {
                    trainingGames.add(trainingGame);
                    hasWhiteWinner = true;
                    whiteWinnerGame = trainingGame;
                }
            }

            DataSet gamesDataset = Train.getDataSet(trainingGames);
            DataSetIterator gamesDatasetIterator = new SamplingDataSetIterator(
                gamesDataset,
                3, 1
            );

            MultiLayerNetwork model = Model.get(size);

            assertEqual(model.score(gamesDataset), 1, 2);
            model.fit(gamesDatasetIterator, 15);
            assertEqual(model.score(gamesDataset), 0, 0.1, "Score");

            BoardState blackWinningGame_BlackMove =
                blackWinnerGame.getHistory().get(blackWinnerGame.getHistory().size() - 2);
            BoardState blackWinningGame_WhiteMove =
                blackWinnerGame.getHistory().get(blackWinnerGame.getHistory().size() - 1);
            BoardState whiteWinningGame_BlackMove =
                whiteWinnerGame.getHistory().get(whiteWinnerGame.getHistory().size() - 1);
            BoardState whiteWinningGame_WhiteMove =
                whiteWinnerGame.getHistory().get(whiteWinnerGame.getHistory().size() - 2);
            
            assertColor(blackWinningGame_BlackMove.getCurrentPlayer(), Color.Black);
            assertColor(blackWinningGame_WhiteMove.getCurrentPlayer(), Color.White);
            assertColor(whiteWinningGame_BlackMove.getCurrentPlayer(), Color.Black);
            assertColor(whiteWinningGame_WhiteMove.getCurrentPlayer(), Color.White);

            assertEqual(
                model.output(
                    blackWinningGame_BlackMove.toINDArray()
                ).toFloatVector(),
                new float[]{1},
                0.5
            );

            assertEqual(
                model.output(
                    blackWinningGame_WhiteMove.toINDArray()
                ).toFloatVector(),
                new float[]{-1},
                0.5
            );

            assertEqual(
                model.output(
                    whiteWinningGame_BlackMove.toINDArray()
                ).toFloatVector(),
                new float[]{-1},
                0.5
            );

            assertEqual(
                model.output(
                    whiteWinningGame_WhiteMove.toINDArray()
                ).toFloatVector(),
                new float[]{1},
                0.5
            );

        });

    }
}
