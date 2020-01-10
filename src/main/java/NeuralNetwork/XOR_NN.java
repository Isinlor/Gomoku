package NeuralNetwork;

import org.datavec.api.records.reader.BaseRecordReader;
import org.datavec.api.util.RecordUtils;
import org.deeplearning4j.datasets.iterator.INDArrayDataSetIterator;
import org.deeplearning4j.datasets.iterator.RandomDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.conf.layers.OutputLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.deeplearning4j.optimize.listeners.ScoreIterationListener;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.iter.INDArrayIterator;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Nadam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.primitives.Pair;

import java.util.ArrayList;

public class XOR_NN {
    public static void main(String[] args) {

        // XOR
        // 0 0 | 0
        // 0 1 | 1
        // 1 0 | 1
        // 1 1 | 0

        int numSamples = 4;
        int sampleLength = 2;
        int labelLength = 1;

        INDArray samples = Nd4j.create(
            new float[]{
                0, 0,
                0, 1,
                1, 0,
                1, 1
            },
            new int[]{numSamples, sampleLength}
        );

        INDArray labels = Nd4j.create(
            new float[]{
                0, 1, 1, 0
            },
            new int[]{numSamples, labelLength}
        );

        Nadam optimizer = new Nadam();
        optimizer.setLearningRate(0.012);
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
            .seed(123) //include a random seed for reproducibility
            .activation(Activation.RELU)
            .weightInit(WeightInit.XAVIER)
            .updater(optimizer)
            .list()
            .layer(new DenseLayer.Builder()
                .nIn(2)
                .nOut(2)
                .build())
            .layer(new OutputLayer.Builder(LossFunctions.LossFunction.SQUARED_LOSS)
                .activation(Activation.SIGMOID)
                .nOut(1)
                .build())
            .build();

        MultiLayerNetwork model = new MultiLayerNetwork(conf);

        model.setListeners(new ScoreIterationListener(1));

        model.init();

        float[][][] dataset = {
            {{0, 0}, {0}},
            {{0, 1}, {1}},
            {{1, 0}, {1}},
            {{1, 1}, {0}},
        };

        ArrayList<Pair<INDArray, INDArray>> list = new ArrayList<>();
        for (float[][] example: dataset) {
            Pair<INDArray, INDArray> pair = new Pair<INDArray, INDArray>(
                Nd4j.create(example[0], new int[]{1, sampleLength}),
                Nd4j.create(example[1], new int[]{1, labelLength})
            );
            list.add(pair);
        }

        INDArrayDataSetIterator iterator = new INDArrayDataSetIterator(list, 1);
        model.fit(iterator, 100);
//
//        DataSet ds = new DataSet(samples, labels);
//        INDArray output = model.output(
//            Nd4j.create(
//                new float[]{
//                    0, 0,
//                    0, 1,
//                    1, 0,
//                    1, 1
//                },
//                new int[]{numSamples, sampleLength}
//            )
//        );
//        System.out.println(output);

        System.out.println(model.evaluate(iterator));

    }
}
