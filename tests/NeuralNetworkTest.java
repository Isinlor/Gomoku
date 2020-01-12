import Board.Helpers.MoveSelectors;
import Contract.Color;
import NeuralNetwork.Train;
import NeuralNetwork.TrainingGame;
import NeuralNetwork.Model;
import Player.MCTSPlayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.SamplingDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;

import java.util.ArrayList;

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
            for (int i = 0; i < 10; i++) {
                model.fit(dataset);
            }
            assertEqual(model.output(features).toFloatVector(), new float[]{0,1,-1}, 0.02);
        });

        it("overfits dumb dataset using dataset SamplingDataSetIterator", () -> {
            MultiLayerNetwork model = Model.get(boardSize);
            assertEqual(model.output(features).toFloatVector(), new float[]{0,0,0}, 0.5);
            model.fit(iterator, 10);
            assertEqual(model.output(features).toFloatVector(), new float[]{0,1,-1}, 0.02);
            assertEqual(model.score(dataset), 0, 0.001);
        });

        it("overfits simple games", () -> {

            int size = 7;
            ArrayList<TrainingGame> trainingGames = new ArrayList<>();

            boolean hasBlackWinner = false;
            boolean hasWhiteWinner = false;
            while(!hasBlackWinner || !hasWhiteWinner) {
                TrainingGame trainingGame = new TrainingGame(
                    new MCTSPlayer(MoveSelectors.get("all"), 0.003),
                    new MCTSPlayer(MoveSelectors.get("all"), 0.003),
                    size
                );
                if(!hasBlackWinner && trainingGame.getWinner() == Color.Black) {
                    trainingGames.add(trainingGame);
                    hasBlackWinner = true;
                }
                if(!hasWhiteWinner && trainingGame.getWinner() == Color.White) {
                    trainingGames.add(trainingGame);
                    hasWhiteWinner = true;
                }
            }

            DataSet gamesDataset = Train.getDataSet(trainingGames);
            DataSetIterator gamesDatasetIterator = new SamplingDataSetIterator(
                gamesDataset,
                3, 1
            );

            MultiLayerNetwork model = Model.get(size);

            assertEqual(model.score(gamesDataset), 1, 2);
            model.fit(gamesDatasetIterator, 10);
            assertEqual(model.score(gamesDataset), 0, 0.1);

        });

    }
}
