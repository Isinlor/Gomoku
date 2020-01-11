import NeuralNetwork.Model;
import NeuralNetwork.Train;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.dataset.api.iterator.SamplingDataSetIterator;
import org.nd4j.linalg.factory.Nd4j;
import org.slf4j.LoggerFactory;

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

    }
}
