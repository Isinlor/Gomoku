package Evaluation;

import Contract.Evaluation;
import Contract.ReadableBoard;

import java.util.Random;

public class RandomEvaluation implements Evaluation {
    public double evaluate(ReadableBoard board) {
        return new Random().nextDouble();
    }
}
