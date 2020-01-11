package NeuralNetwork;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nadam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class Model {

    public static MultiLayerNetwork get(int boardSize) {

        int outputNum = 1; // number of output classes
        int batchSize = 10; // batch size for each epoch
        int rngSeed = 123; // random number seed for reproducibility
        double rate = 0.0015; // learning rate

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(rngSeed) //include a random seed for reproducibility
            .activation(Activation.RELU)
            .weightInit(WeightInit.XAVIER)
            .updater(new Nadam())
            .list()
            .layer(new DenseLayer.Builder() //create the first input layer.
                .nIn(boardSize * boardSize * 2)
                .nOut(100)
                .build())
            .layer(new DenseLayer.Builder() //create the second input layer
                .nIn(100)
                .nOut(100)
                .build())
            .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE) //create hidden layer
                .activation(Activation.TANH)
                .nOut(outputNum)
                .build())
            .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        return model;

    }

}
