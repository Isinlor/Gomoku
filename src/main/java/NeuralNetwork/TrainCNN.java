package NeuralNetwork;

import Contract.BoardState;
import org.deeplearning4j.datasets.iterator.INDArrayDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.SamplingDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.primitives.Pair;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;

public class TrainCNN {

    private static List<TrainingGame> loadGames(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fi = new FileInputStream(file);
            ObjectInputStream oi = new ObjectInputStream(fi);
            Queue<TrainingGame> games = (Queue<TrainingGame>) oi.readObject();
            oi.close();
            fi.close();
            System.out.println("Loaded games: " + games.size());
            return new ArrayList(games);

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }

        return null;
    }

    //    private static ArrayList<Pair<INDArray, INDArray>> generateDataset(List<TrainingGame> games) {
    private static DataSet generateDataset(List<TrainingGame> games) {
        ArrayList<Pair<INDArray, INDArray>> dataset = new ArrayList<>();
        INDArray features = Nd4j.zeros(games.size(), 9, 9, 2);
        INDArray labels = Nd4j.zeros(games.size(), 1);

        int row = 0;
        for (TrainingGame game : games.subList(0, 10)) {
            for (BoardState boardState : game.getHistory()) {
                int gameOutcome;
                if (game.winner == null) {
                    gameOutcome = 0;
                } else {
                    gameOutcome = game.winner == boardState.getCurrentPlayer() ? 1 : -1;
                }
                for (int r = 0; r < 4; r++) {

                    int[][][] boardStateMatrix = boardState.toMultiDimensionalMatrix();

                    INDArray boardStateArray = Nd4j.createFromArray(boardStateMatrix);
                    Pair<INDArray, INDArray> pair = new Pair<INDArray, INDArray>(
                            boardStateArray,
                            Nd4j.create(new float[]{gameOutcome}, new int[]{1, 1})
                    );
                    dataset.add(pair);
                    features.putRow(row, boardStateArray);
                    labels.putRow(row, Nd4j.create(new float[]{gameOutcome}, new int[]{1, 1}));
                    boardState.rotate90degrees();
                    row++;
                }
            }
        }

        //shuffle to get random order of training instances
//        Collections.shuffle(dataset);
//        return dataset;
        return new DataSet(features, labels);
    }


    public static void main(String[] args) throws IOException {
        String filePath = "src/main/resources/training_games.ser";
        if (args.length > 0) {
            filePath = args[0];
        }

        List<TrainingGame> games = loadGames(filePath);
//        List<Pair<INDArray, INDArray>> dataset = generateDataset(games);
        DataSet dataset = generateDataset(games);
//        INDArrayDataSetIterator iterator = new INDArrayDataSetIterator(dataset, 512);
//        System.out.println("Training instances: " + iterator.next().getFeatures().shapeInfoToString());
        SamplingDataSetIterator iterator = new SamplingDataSetIterator(dataset, 512, (int) games.size()/512);

        final int boardSize = 9;
        int numEpochs = 50; // number of epochs to perform
        MultiLayerNetwork model = ModelCNN.get(boardSize);
        model.init();

        model.setListeners(new ScoreIterationListener(10));  //print the score with every iteration
        model.setListeners(new EvaluativeListener(iterator, 10, model.evaluateRegression(iterator)));  //print the score with every iteration

        model.fit(iterator, numEpochs);

        model.save(new File("src/main/resources/model_cnn.dl4j"), true);
        System.out.println(model.output(iterator));

    }

}
