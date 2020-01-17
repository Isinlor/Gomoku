package Evaluation;

import Contract.Evaluation;
import Contract.ReadableBoard;
import org.deeplearning4j.nn.modelimport.keras.KerasModelImport;
import org.deeplearning4j.nn.modelimport.keras.exceptions.InvalidKerasConfigurationException;
import org.deeplearning4j.nn.modelimport.keras.exceptions.UnsupportedKerasConfigurationException;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.io.ClassPathResource;

import java.io.IOException;

public class NeuralNetworkCNNEvaluation implements Evaluation {
    MultiLayerNetwork model;

    public NeuralNetworkCNNEvaluation(String modelName) {
        loadModel(modelName);
    }

    public void loadModel(String modelName) {
        String path = null;
        try {
            path = new ClassPathResource(modelName).getFile().getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            model = KerasModelImport.importKerasSequentialModelAndWeights(path);
//            model = KerasModelImport.importKerasModelAndWeights(path);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidKerasConfigurationException e) {
            e.printStackTrace();
        } catch (UnsupportedKerasConfigurationException e) {
            e.printStackTrace();
        }
    }
    public double evaluate(ReadableBoard board) {
        if(board.hasWinner()) {
            return board.getWinner() == board.getCurrentColor() ? Win : Lost;
        }
        int boardSize = 9;
        int channels = 2;
        double[][][][] multiDimensionalMatrix =  new double[1][channels][boardSize][boardSize];
        multiDimensionalMatrix[0] = board.getBoardState().toMultiDimensionalMatrix();
        INDArray input = Nd4j.create(multiDimensionalMatrix);
        final long start = System.currentTimeMillis();
        double prediction = model.output(input).getDouble(0);
        final long end = System.currentTimeMillis();
//        System.out.println(end-start);
        return prediction;
    }
}
