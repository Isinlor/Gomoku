package NeuralNetwork;

import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.ConvolutionLayer;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.conf.layers.SubsamplingLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Nadam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

public class ModelCNN {

    public static MultiLayerNetwork get(int boardSize) {

        int outputNum = 1; // number of output classes
        int channels = 2;
        int batchSize = 10; // batch size for each epoch
        int rngSeed = 123; // random number seed for reproducibility
        double rate = 0.0015; // learning rate

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(rngSeed) //include a random seed for reproducibility
            .activation(Activation.RELU)
            .weightInit(WeightInit.XAVIER)
            .updater(new Nadam())
            .list()
            .layer(0, convInit("cnn1", channels, 32 ,  new int[]{5, 5}, new int[]{1, 1}, new int[]{0, 0}, 0))
            .layer(1, maxPool("maxpool1", new int[]{2,2}))
            .layer(2, conv3x3("cnn2", 64, 0))
            .layer(3, conv3x3("cnn3", 64,1))
            .layer(4, maxPool("maxpool2", new int[]{2,2}))
            .layer(5, new DenseLayer.Builder().activation(Activation.RELU)
                    .nOut(512).dropOut(0.5).build())
            .layer(6, new OutputLayer.Builder(LossFunctions.LossFunction.MSE) //create hidden layer
                .activation(Activation.TANH)
                .nOut(outputNum)
                .build())
            .setInputType(InputType.convolutional(boardSize, boardSize, channels))
            .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);
        model.init();

        return model;

    }

    static private ConvolutionLayer convInit(String name, int in, int out, int[] kernel, int[] stride, int[] pad, double bias) {
        return new ConvolutionLayer.Builder(kernel, stride, pad).name(name).nIn(in).nOut(out).biasInit(bias).build();
    }

    static private ConvolutionLayer conv3x3(String name, int out, double bias) {
        return new ConvolutionLayer.Builder(new int[]{3,3}, new int[] {1,1}, new int[] {1,1}).name(name).nOut(out).biasInit(bias).build();
    }

    static private SubsamplingLayer maxPool(String name, int[] kernel) {
        return new SubsamplingLayer.Builder(kernel, new int[]{2,2}).name(name).build();
    }

}
