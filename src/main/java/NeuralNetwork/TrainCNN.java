package NeuralNetwork;

import Contract.BoardState;
import Protobuf.DatasetProtos;
import com.google.common.primitives.Floats;
import com.google.common.primitives.Ints;
import com.google.gson.Gson;
import org.deeplearning4j.datasets.iterator.INDArrayDataSetIterator;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.optimize.listeners.EvaluativeListener;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.linalg.api.buffer.DataBuffer;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.SamplingDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.primitives.Pair;

import java.io.*;
import java.util.*;

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
    private static Dataset generateDataset(List<TrainingGame> games) {
        Dataset dataset = new Dataset();
        DatasetProtos.DataSet.Builder datasetBuilder = DatasetProtos.DataSet.newBuilder();

//        ArrayList<Pair<INDArray, INDArray>> dataset = new ArrayList<>();
        INDArray features = Nd4j.zeros(games.size(), 9, 9, 2);
        INDArray labels = Nd4j.zeros(games.size(), 1);

        int row = 0;
        for (TrainingGame game : games.subList(0,1000)) {
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
                    DatasetProtos.DataInstance.Builder dataInstance = DatasetProtos.DataInstance.newBuilder();

                    float[] uniformPolicy = new float[boardStateMatrix.length * boardStateMatrix.length];
                    Arrays.fill(uniformPolicy, 1/uniformPolicy.length);
                    int[] flattenedState = Arrays.stream(boardStateMatrix)
                            .flatMap(Arrays::stream)
                            .flatMapToInt(Arrays::stream)
                            .toArray();

                    dataInstance.addAllPolicy(Floats.asList(uniformPolicy));
                    dataInstance.addAllState(Ints.asList(flattenedState));
                    dataInstance.setValue(gameOutcome);

                    dataset.add(boardStateMatrix, gameOutcome);

                    datasetBuilder.addData(dataInstance.build());
//                    features.putRow(row, boardStateArray);
//                    labels.putRow(row, Nd4j.create(new float[]{gameOutcome}, new int[]{1, 1}));
                    boardState.rotate90degrees();
                    row++;
                }
            }
        }

        FileOutputStream output = null;
        try {
            output = new FileOutputStream("src/main/resources/dataset.bin");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            datasetBuilder.build().writeTo(output);
        } catch (IOException e) {
            e.printStackTrace();
        }


        //shuffle to get random order of training instances
//        Collections.shuffle(dataset);
        return dataset;
//        return new DataSet(features, labels);
    }


    public static void main(String[] args) throws IOException {
        String resourcePath = "src/main/resources/";
        String filePathIn = resourcePath + "training_games.ser";
        String filePathOutGames = resourcePath + "training_games.json";
        String filePathOutDataset = resourcePath +"dataset.json";
        if (args.length > 0) {
            filePathIn = args[0];
        }
        Gson gson = new Gson();

        List<TrainingGame> games = loadGames(filePathIn);
//        gson.toJson(games, new FileWriter(filePathOutGames));
//        List<Pair<INDArray, INDArray>> dataset = generateDataset(games);
        Dataset dataset = generateDataset(games);

//        gson.toJson(dataset, new FileWriter(filePathOutDataset));

//        System.out.print("Bla bla bla"+ dataset.getLabels());
//        INDArrayDataSetIterator iterator = new INDArrayDataSetIterator(dataset, 512);
//        System.out.println("Training instances: " + iterator.next().getFeatures().shapeInfoToString());
//        SamplingDataSetIterator iterator = new SamplingDataSetIterator(dataset, 512, (int) games.size()/512);
//
//        final int boardSize = 9;
//        int numEpochs = 50; // number of epochs to perform
//        MultiLayerNetwork model = ModelCNN.get(boardSize);
//        model.init();
//
//        model.setListeners(new ScoreIterationListener(10));  //print the score with every iteration
//        model.setListeners(new EvaluativeListener(iterator, 10, model.evaluateRegression(iterator)));  //print the score with every iteration
//        System.out.print("Fit before");
//
//        model.fit(iterator, numEpochs);
//        System.out.print("Fit after");
//
//
//        model.save(new File("src/main/resources/model_cnn.dl4j"), true);
//        System.out.println(model.output(iterator));

    }

}
