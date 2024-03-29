import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Evaluation;
import Evaluation.NeuralNetworkEvaluation;

public class NeuralNetworkEvaluationTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nNeural Network Evaluation Test\n");

        it("allows to evaluate empty board based on saved model", () -> {
            Evaluation evaluation = new NeuralNetworkEvaluation("tests/resources/model.dl4j");
            BoardCell[][] boardState = {
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, B, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(Double.isFinite(evaluation.evaluate(board)));
        });
        it("allows to evaluate board favoring white from saved model", () -> {
            Evaluation evaluation = new NeuralNetworkEvaluation("tests/resources/model.dl4j");
            BoardCell[][] boardState = {
                {B, E, E, E, E, E, E, E, B},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, W, W, W, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {E, E, E, E, E, E, E, E, E},
                {B, E, E, E, E, E, E, E, B},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            assertTrue(Double.isFinite(evaluation.evaluate(board)));
        });
    }

}
