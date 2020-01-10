package Evaluation;

import Contract.Evaluation;
import Contract.ReadableBoard;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;

import java.io.File;
import java.io.IOException;

public class NeuralNetworkEvaluation implements Evaluation {
    MultiLayerNetwork model;
    public NeuralNetworkEvaluation(String path) {
        try {
            model = MultiLayerNetwork.load(new File(path), false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public double evaluate(ReadableBoard board) {
        float[] boardStateVector = board.getBoardState().toVector();
        INDArray output = model.output(Nd4j.create(boardStateVector, new int[]{1, boardStateVector.length}));
        return output.getDouble(0, 0);
    }
}
