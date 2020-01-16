import Board.SimpleBoard;
import Contract.BoardCell;
import Contract.Evaluation;
import Evaluation.NeuralNetworkCNNEvaluation;

public class NeuralNetworkCNNEvaluationTest extends SimpleUnitTest {

    public static void main(String[] args) {

        System.out.println("\n\nNeural Network CNN Evaluation Test\n");

        it("allows to evaluate empty board based on saved model", () -> {
            Evaluation evaluation = new NeuralNetworkCNNEvaluation("model.h5");
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
            Evaluation evaluation = new NeuralNetworkCNNEvaluation("model.h5");
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
            System.out.println("Evaluation score: " + evaluation.evaluate(board));
            assertTrue(evaluation.evaluate(board)>0);
        });

        it("allows to evaluate board favoring black from saved model", () -> {
            Evaluation evaluation = new NeuralNetworkCNNEvaluation("model.h5");
            BoardCell[][] boardState = {
                    {W, E, E, E, E, E, E, E, W},
                    {E, E, E, E, E, E, E, E, E},
                    {E, E, E, B, E, E, E, E, E},
                    {E, E, E, E, B, E, E, E, E},
                    {E, E, E, E, B, B, E, E, E},
                    {E, E, E, E, E, E, E, E, E},
                    {E, E, E, E, E, E, E, E, E},
                    {E, E, E, E, E, E, E, E, E},
                    {W, E, E, E, E, E, E, E, E},
            };
            SimpleBoard board = new SimpleBoard(boardState);
            System.out.println("Evaluation score: " + evaluation.evaluate(board));
            assertTrue(evaluation.evaluate(board)<0);
        });
    }

}
