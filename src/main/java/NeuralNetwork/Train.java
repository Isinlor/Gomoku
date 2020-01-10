package NeuralNetwork;

import Board.TrainingGame;
import Contract.BoardState;
import Contract.Game;
import org.deeplearning4j.datasets.iterator.INDArrayDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nadam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.primitives.Pair;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class Train {

    private static ArrayList<TrainingGame> loadGames(String filePath) {
        ArrayList games = new ArrayList<TrainingGame>();

        try {
            FileInputStream fileIn = new FileInputStream(filePath);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            TrainingGame game;
            while((game = (TrainingGame)in.readObject()) !=null ) {
                games.add(game);
            }
            in.close();
            fileIn.close();
        } catch (EOFException e) {

        } catch (IOException | ClassNotFoundException i)  {
            i.printStackTrace();
        }
        System.out.println("Loaded games: " + games.size());
        return games;
    }

    public static void main(String[] args) {
        String filePath = "src/main/resources/games.ser";
        if(args.length>0) {
            filePath = args[0];
        }
        ArrayList<TrainingGame> games = loadGames(filePath);

        ArrayList<Pair<INDArray, INDArray>> dataset = new ArrayList<>();
        for(TrainingGame game:games) {
            for(BoardState boardState: game.getHistory()) {
                {
                    int gameOutcome;
                    if(game.winner==null) {
                        gameOutcome = 0;
                    } else{
                        gameOutcome = game.winner==boardState.getCurrentPlayer() ? 1 : -1;
                    }
                    float [] boardStateVector = boardState.toVector();
                    Pair<INDArray, INDArray> pair = new Pair<INDArray, INDArray>(
                            Nd4j.create(boardStateVector, new int[]{1, boardStateVector.length}),
                            Nd4j.create(new float[]{gameOutcome}, new int[]{1, 1})
                    );
                    dataset.add(pair);
                }
            }
        }
        INDArrayDataSetIterator iterator = new INDArrayDataSetIterator(dataset, 15);

        final int boardSize = 9;
        int outputNum = 1; // number of output classes
        int batchSize = 10; // batch size for each epoch
        int rngSeed = 123; // random number seed for reproducibility
        int numEpochs = 500; // number of epochs to perform
        double rate = 0.0015; // learning rate

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(rngSeed) //include a random seed for reproducibility
            .activation(Activation.RELU)
            .weightInit(WeightInit.XAVIER)
            .updater(new Nadam())
            .l2(rate * 0.005) // regularize learning model
            .list()
            .layer(new DenseLayer.Builder() //create the first input layer.
                .nIn(boardSize * boardSize * 2)
                .nOut(100)
                .build())
            .layer(new DenseLayer.Builder() //create the second input layer
                .nIn(100)
                .nOut(100)
                .build())
            .layer(new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS) //create hidden layer
                .activation(Activation.TANH)
                .nOut(outputNum)
                .build())
            .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        model.setListeners(new ScoreIterationListener(25));  //print the score with every iteration

        model.fit(iterator, numEpochs);

    }
}
