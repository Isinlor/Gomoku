package NeuralNetwork;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nadam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class Train {
    public static void main(String[] args) {

        //number of rows and columns in the input pictures
        final int boardSize = 9;
        int outputNum = 1; // number of output classes
        int batchSize = 10; // batch size for each epoch
        int rngSeed = 123; // random number seed for reproducibility
        int numEpochs = 2; // number of epochs to perform
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
                .activation(Activation.SIGMOID)
                .nOut(outputNum)
                .build())
            .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

//        model.setListeners(new ScoreIterationListener(100));  //print the score with every iteration
//
//        log.info("Train model....");
//        model.fit(mnistTrain, numEpochs);
//
//        log.info("Evaluate model....");
//        Evaluation eval = model.evaluate(mnistTest);

    }
}
